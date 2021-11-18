package com.kohls.cpe.darkside.service;

import com.kohls.cpe.darkside.entity.Contact;
import com.kohls.cpe.darkside.entity.PhoneNumber;
import com.kohls.cpe.darkside.exception.ResourceNotFoundException;
import com.kohls.cpe.darkside.repository.ContactsRepository;
import com.kohls.cpe.darkside.repository.PhoneNumbersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhoneBookServiceImpl implements PhoneBookService {

    private final ContactsRepository contactsRepository;
    private final PhoneNumbersRepository phoneNumbersRepository;

    @Override
    public Contact saveContact(Contact contact) {
        return contactsRepository.save(contact);
    }

    @Transactional
    @Override
    public void deleteContact(int contactId) {
        Contact contact = contactsRepository.findById(contactId).orElseThrow(() -> new ResourceNotFoundException("contactId-" + contactId));
        contactsRepository.delete(contact);
    }

    @Transactional
    @Override
    public Contact savePhoneNumber(int contactId, PhoneNumber phoneNumber) {
        return contactsRepository.findById(contactId).map(contact -> {
                    contact.addPhoneNumber(phoneNumber);
                    return contactsRepository.save(contact);
                }
        ).orElseThrow(() -> new ResourceNotFoundException("contactId-" + contactId));
    }

    @Override
    public List<Contact> findAll() {
        return contactsRepository.findAll();
    }

    @Override
    public Contact findById(Integer contactId) {
        return contactsRepository.findById(contactId).orElseThrow(() -> new ResourceNotFoundException("contactId-" + contactId));
    }

    @Override
    public List<Contact> findAllByFirstNameAndLastName(String firstName, String lastName) {
        return contactsRepository.findAll(firstName, lastName);
    }

    @Override
    public List<Contact> findAllByFirstName(String firstName) {
        return contactsRepository.findAll(firstName);
    }

    @Override
    public List<Contact> findAllByLastName(String lastName) {
        return contactsRepository.findAllByLastNameOrderByFirstNameAscLastNameAsc(lastName);
    }

    @Transactional
    @Override
    public void deletePhoneNumber(int contactId, int phoneNumberId) {
        Contact contact = contactsRepository.findById(contactId).orElseThrow(
                () -> new ResourceNotFoundException("contactId-" + contactId));

        PhoneNumber phoneNumber = phoneNumbersRepository.findById(phoneNumberId).orElseThrow(
                () -> new ResourceNotFoundException("phoneNumberId-" + phoneNumberId));

        contact.removePhoneNumber(phoneNumber);
        contactsRepository.save(contact);
    }

}
