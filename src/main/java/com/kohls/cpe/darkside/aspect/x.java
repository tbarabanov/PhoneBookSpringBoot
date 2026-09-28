package ru.tbank.crypto.platform.core.indexer.integration.support;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface Admin {

    ObjectMapper OBJECT_MAPPER =
        JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();

    static Admin connect(String url) {
        return Feign.builder()
            .contract(new SpringMvcContract())
            .encoder(new JacksonEncoder(OBJECT_MAPPER))
            .decoder(new JacksonDecoder(OBJECT_MAPPER))
            .target(Admin.class, url);
    }

    @GetMapping("/imposters/{port}")
    GetImposterResponse getImposter(@PathVariable(name = "port") int port);

    @DeleteMapping("/imposters/{port}/savedRequests")
    void deleteSavedRequests(@PathVariable(name = "port") int port);

    record GetImposterResponse(
        int port,
        String protocol,
        String name,
        String recordRequests,
        int numberOfRequests,
        List<LoggedRpcRequest> requests) {

        public GetImposterResponse {
            if (requests == null) {
                requests = List.of();
            }
        }

    }

    record LoggedRpcRequest(
        String peer, String path, Map<String, Object> value, Metadata metadata, Instant timestamp) {

        public LoggedRpcRequest {
            if (value == null) {
                value = Map.of();
            }
            if (metadata == null) {
                metadata = new Metadata(new Headers(Map.of()));
            }
        }

    }

    record Metadata(Headers initial) {

        public Metadata {
            if (initial == null) {
                initial = new Headers(Map.of());
            }
        }

    }

    record Headers(@JsonValue Map<String, List<String>> values) {

        @JsonCreator
        public Headers {
            if (values == null) {
                values = Map.of();
            }
        }

    }

}
