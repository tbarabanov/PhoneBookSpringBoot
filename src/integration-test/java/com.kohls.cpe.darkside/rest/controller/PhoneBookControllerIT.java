package com.kohls.cpe.darkside.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kohls.cpe.darkside.entity.Contact;
import com.kohls.cpe.darkside.entity.PhoneNumber;
import com.kohls.cpe.darkside.service.PhoneBookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PhoneBookController.class)
@ActiveProfiles("test")
class PhoneBookControllerIT {

    static final String USER = "user";
    static final String PASSWORD = "$ecret";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    PhoneBookService phoneBookServiceMock;

    @Test
    @DisplayName("verifying HTTP request Matching")
    void getContacts_thenReturns200() throws Exception {
        mvc
                .perform(get("/api/v1/contacts/").contentType(MediaType.APPLICATION_JSON).with(httpBasic(USER, PASSWORD)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("verifying input serialization")
    void whenCreateContactWithValidInput_thenReturns201() throws Exception {
        final Contact contact = Contact.builder().id(1).firstName("JOHN").lastName("WAYNE").build();

        mvc.
                perform(post("/api/v1/contacts/").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(contact)).
                        with(httpBasic(USER, PASSWORD))).andExpect(status().isCreated());

    }

    @Test
    @DisplayName("verifying input validation")
    void whenPhoneNumberNullValue_thenReturns400() throws Exception {
        final PhoneNumber phoneNumber = PhoneNumber.builder().code("812").number(null).build();

        mvc.perform(post("/api/v1/contacts/{contactId}/phoneNumbers/", 1).contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(phoneNumber)).
                with(httpBasic(USER, PASSWORD))).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("verifying business logic calls")
    void whenCreateContactWithValidInput_thenCallsServiceLayer() throws Exception {
        final Contact contact = Contact.builder().firstName("Tom").lastName("Yam").build();

        mvc.perform(post("/api/v1/contacts/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(contact)).with(httpBasic(USER, PASSWORD)));

        ArgumentCaptor<Contact> contactArgumentCaptor = ArgumentCaptor.forClass(Contact.class);
        verify(phoneBookServiceMock).saveContact(contactArgumentCaptor.capture());

        assertThat(contactArgumentCaptor.getValue().getFirstName()).isEqualTo(contact.getFirstName());
        assertThat(contactArgumentCaptor.getValue().getLastName()).isEqualTo(contact.getLastName());
    }

    @Test
    @DisplayName("verifying output serialization")
    void whenCreateContactWIthValidInput_thenReturnsContact() throws Exception {

        final Contact contact = Contact.builder().id(0).firstName("Tom").lastName("Yam").build();

        when(phoneBookServiceMock.saveContact(any(Contact.class))).thenReturn(contact);

        MvcResult mvcResult = mvc.perform(post("/api/v1/contacts/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(contact)).with(httpBasic(USER, PASSWORD))).andReturn();
        assertThat(objectMapper.writeValueAsString(contact))
                .isEqualToIgnoringWhitespace(mvcResult.getResponse().getContentAsString());
    }
}
