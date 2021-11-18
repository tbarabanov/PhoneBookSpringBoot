package com.kohls.cpe.darkside.service;

import com.kohls.cpe.darkside.entity.Contact;
import com.kohls.cpe.darkside.entity.PhoneNumber;

import java.util.List;

public interface PhoneBookService {

    Contact saveContact(Contact contact);

    void deleteContact(int contactId);

    Contact savePhoneNumber(int contactId, PhoneNumber phoneNumber);

    List<Contact> findAll();

    Contact findById(Integer contactId);

    List<Contact> findAllByFirstNameAndLastName(String firstName, String lastName);

    List<Contact> findAllByFirstName(String firstName);

    List<Contact> findAllByLastName(String lastName);

    void deletePhoneNumber(int contactId, int phoneNumberId);
}
