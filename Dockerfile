FROM openjdk:18-jdk-alpine3.13

COPY ./target/PhoneBook-1.0-SNAPSHOT.jar /user/phone-book/
COPY ./phone_book.sh /user/phone-book/

WORKDIR /user/phone-book/

ENTRYPOINT ["./phone_book.sh"]
