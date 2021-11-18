package com.kohls.cpe.darkside.repository;

import com.kohls.cpe.darkside.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactsRepository extends JpaRepository<Contact, Integer> {

    // native query
    @Query(nativeQuery = true, value = "select * from contact as c where c.first_name = :firstName order by c.first_name asc, c.last_name asc")
    List<Contact> findAll(@Param("firstName") String firstName);

    // Inferred Query with a long method name
    List<Contact> findAllByLastNameOrderByFirstNameAscLastNameAsc(String lastName);

    // custom JPQL query
    @Query("select c from Contact c where c.firstName = :firstName and c.lastName = :lastName order by c.firstName asc, c.lastName asc")
    List<Contact> findAll(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
