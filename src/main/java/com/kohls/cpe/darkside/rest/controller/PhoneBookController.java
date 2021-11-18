package com.kohls.cpe.darkside.rest.controller;

import com.kohls.cpe.darkside.entity.Contact;
import com.kohls.cpe.darkside.entity.Contacts;
import com.kohls.cpe.darkside.entity.PhoneNumber;
import com.kohls.cpe.darkside.service.PhoneBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
public class PhoneBookController {

    private final PhoneBookService phoneBookService;


    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public Contact createContact(@RequestBody @Valid Contact contact) {
        return phoneBookService.saveContact(contact);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(value = "/{contactId}/phoneNumbers",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public Contact createPhoneNumber(@PathVariable Integer contactId, @RequestBody @Valid PhoneNumber phoneNumber) {
        return phoneBookService.savePhoneNumber(contactId, phoneNumber);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{contactId}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public void deleteContact(@PathVariable Integer contactId) {
        phoneBookService.deleteContact(contactId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{contactId}/phoneNumbers/{phoneNumberId}",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public void deletePhoneNumber(@PathVariable Integer contactId, @PathVariable Integer phoneNumberId) {
        phoneBookService.deletePhoneNumber(contactId, phoneNumberId);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public Contacts getContacts(@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName) {
        if (isNull(firstName) && isNull(lastName)) {
            return new Contacts(phoneBookService.findAll());
        } else if (isNull(firstName)) {
            return new Contacts(phoneBookService.findAllByLastName(lastName));
        } else if (isNull(lastName)) {
            return new Contacts(phoneBookService.findAllByFirstName(firstName));
        } else {
            return new Contacts(phoneBookService.findAllByFirstNameAndLastName(firstName, lastName));
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping(value = "/{contactId}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public Contact getContact(@PathVariable Integer contactId) {
        return phoneBookService.findById(contactId);
    }
}
