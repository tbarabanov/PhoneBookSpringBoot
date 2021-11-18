package com.kohls.cpe.darkside.repository;

import com.kohls.cpe.darkside.entity.Contact;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@Sql({"/sql/data.sql"})
@ActiveProfiles("test")
public class ContactsRepositorySqlIT {

    @Autowired
    private ContactsRepository contactsRepository;

    @ParameterizedTest
    @CsvSource({"IVAN, DORN"})
    @DisplayName("find all by firstName with native query")
    void whenInitializedBySql_thenFindsByFirstName(String firstName, String lastName) {
        final List<Contact> actualContacts = contactsRepository.findAll(firstName);
        assertThat(actualContacts).
                extracting("firstName",
                        "lastName").
                containsExactly(tuple(firstName, lastName));
    }
}
