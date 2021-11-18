package com.kohls.cpe.darkside.repository;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.kohls.cpe.darkside.entity.Contact;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
@ActiveProfiles("test")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
@DatabaseSetup("/dbunit/data.xml")
public class ContactsRepositoryDbUnitIT {

    @Autowired
    private ContactsRepository contactsRepository;

    @ParameterizedTest
    @CsvSource({"IVAN, DORN"})
    @DisplayName("find all by firstName with native query")
    void whenInitializedByDbUnit_thenFindsByFirstName(String firstName, String lastName) {
        final List<Contact> actualContacts = contactsRepository.findAll(firstName);
        assertThat(actualContacts).
                extracting("firstName",
                        "lastName").
                containsExactly(tuple(firstName, lastName));
    }

    @ParameterizedTest
    @MethodSource("lastNameAndExpectedValues")
    @DisplayName("find all by lastName for inferred query with a long method name")
    void whenInitializedByDbUnit_thenFindsByLastName(String lastName, Tuple... expected) {
        final List<Contact> actualContacts = contactsRepository.findAllByLastNameOrderByFirstNameAscLastNameAsc(lastName);
        assertThat(actualContacts).
                extracting("firstName",
                        "lastName").
                containsExactly(expected);
    }

    @ParameterizedTest
    @CsvSource({"IVAN, DORN", "TOM, YAM", "DUNG, YAM"})
    @DisplayName("find all by firstName and lastName for custom JPQL query")
    void whenInitializedByDbUnit_thenFindsByFirstNameAndLastName(String firstName, String lastName) {
        final List<Contact> actualContacts = contactsRepository.findAll(firstName, lastName);
        assertThat(actualContacts).
                extracting("firstName",
                        "lastName").
                containsExactly(tuple(firstName, lastName));
    }

    static Stream<Arguments> lastNameAndExpectedValues() {
        return Stream.of(
                arguments("YAM", new Tuple[]{
                        tuple("DUNG", "YAM"),
                        tuple("TOM", "YAM")
                }),
                arguments("DORN", new Tuple[]{
                        tuple("IVAN", "DORN")
                }));
    }
}
