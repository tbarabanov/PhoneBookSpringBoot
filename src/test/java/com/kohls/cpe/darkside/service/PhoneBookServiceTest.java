package com.kohls.cpe.darkside.service;

import com.kohls.cpe.darkside.entity.Contact;
import com.kohls.cpe.darkside.entity.PhoneNumber;
import com.kohls.cpe.darkside.exception.ResourceNotFoundException;
import com.kohls.cpe.darkside.repository.ContactsRepository;
import com.kohls.cpe.darkside.repository.PhoneNumbersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@EnabledIf(expression = "#{systemProperties['service.tests.enabled']?:true}")
@ExtendWith(MockitoExtension.class)
public class PhoneBookServiceTest {

    @Mock
    private ContactsRepository contactsRepository;

    @Mock
    private PhoneNumbersRepository phoneNumbersRepository;

    @InjectMocks
    private PhoneBookServiceImpl phoneBookService;

    @Test
    void whenDeleteContact_thenThrowsResourceNotFoundException() {
        when(contactsRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> phoneBookService.deleteContact(0)).
                isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void whenSavePhoneNumber_thenPhoneNumberAddedToContacts() {
        final PhoneNumber phoneNumber = PhoneNumber.builder().code("+64").number("987-12-55").build();
        final Contact contact = Contact.builder().firstName("Jim").lastName("Beam").build();

        when(contactsRepository.findById(anyInt())).thenReturn(Optional.of(contact));
        when(contactsRepository.save(any(Contact.class))).then(returnsFirstArg());

        phoneBookService.savePhoneNumber(0, phoneNumber);

        assertThat(contact.getPhoneNumbers()).containsOnly(phoneNumber);
    }

    @Test
    void whenDeletePhoneNumber_thenPhoneNumberRemovedFromContacts() {
        final PhoneNumber phoneNumber = PhoneNumber.builder().code("+64").number("987-12-55").build();
        final Contact contact = Contact.builder().
                firstName("Jim").
                lastName("Beam").
                phoneNumbers(
                        new HashSet<PhoneNumber>() {{
                            add(phoneNumber);
                        }}).
                build();

        when(contactsRepository.findById(anyInt())).thenReturn(Optional.of(contact));
        when(phoneNumbersRepository.findById(anyInt())).thenReturn(Optional.of(phoneNumber));

        phoneBookService.deletePhoneNumber(0, 0);

        assertThat(contact.getPhoneNumbers()).doesNotContain(phoneNumber);
    }
}
