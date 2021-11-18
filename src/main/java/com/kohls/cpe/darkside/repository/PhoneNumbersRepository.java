package com.kohls.cpe.darkside.repository;

import com.kohls.cpe.darkside.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneNumbersRepository extends JpaRepository<PhoneNumber, Integer> {
}
