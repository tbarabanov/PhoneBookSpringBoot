package com.kohls.cpe.darkside.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kohls.cpe.darkside.entity.Contact;
import com.kohls.cpe.darkside.repository.ContactsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PhoneBookControllerAllLayersIT {

    static final String USER = "user";
    static final String PASSWORD = "$ecret";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContactsRepository contactsRepository;

    @Test
    void createContactWorksThroughAllLayers() throws Exception {
        final String firstName = "John";
        final String lastName = "Wayne";

        final Contact contact = Contact.builder().firstName(firstName).lastName(lastName).build();

        mvc.
                perform(post("/api/v1/contacts/").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(contact)).
                        with(httpBasic(USER, PASSWORD))).andExpect(status().isCreated());

        assertThat(contactsRepository.findAll(firstName, lastName)).
                extracting("firstName", "lastName").
                containsExactly(tuple(contact.getFirstName(), contact.getLastName()));
    }
}
