package ru.tcsbank.pg.health.check;

import static ru.tcsbank.pg.health.util.ExceptionUtils.uncheck;
import static ru.tcsbank.pg.health.util.NettySSLContextUtils.createSSLContext;

import io.grpc.ManagedChannel;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.channel.ChannelOption;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.util.StringUtils;
import ru.tcsbank.pg.health.HealthCheckProperties;

public class GrpcHealthCheck extends AbstractHealthIndicator {

  private final HealthCheckProperties properties;
  private final ManagedChannel managedChannel;

  public GrpcHealthCheck(HealthCheckProperties properties) {
    this.properties = properties;
    URI target = properties.getUrl();

    NettyChannelBuilder channelBuilder =
        NettyChannelBuilder.forAddress(target.getHost(), target.getPort())
            .withOption(
                ChannelOption.CONNECT_TIMEOUT_MILLIS,
                Math.toIntExact(properties.getConnectionTimeout()));

    this.managedChannel =
        (properties.isUseTLS()
                ? channelBuilder
                    .negotiationType(NegotiationType.TLS)
                    .sslContext(uncheck(() -> createSSLContext(properties)))
                : channelBuilder.usePlaintext())
            .build();
  }

  @Override
  protected void doHealthCheck(Health.Builder builder) throws Exception {
    HealthGrpc.HealthBlockingStub blockingStub =
        HealthGrpc.newBlockingStub(managedChannel)
            .withDeadlineAfter(properties.getTimeout(), TimeUnit.MILLISECONDS);

    String service = Objects.requireNonNullElse(properties.getUrl().getPath(), "");
    service = StringUtils.trimLeadingCharacter(service, '/');

    HealthCheckRequest request = HealthCheckRequest.newBuilder().setService(service).build();
    HealthCheckResponse.ServingStatus servingStatus = blockingStub.check(request).getStatus();

    switch (servingStatus) {
      case SERVING -> builder.up();
      case NOT_SERVING -> builder.outOfService();
      case UNKNOWN -> builder.unknown();
      default -> builder.down();
    }

    builder.withDetail(
        "GRPC", String.format("%s serving status %s", properties.getUrl(), servingStatus));
  }
}
