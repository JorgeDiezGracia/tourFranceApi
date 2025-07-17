package com.svalero.tourfrance.repository;

import com.svalero.tourfrance.domain.Cyclist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CyclistRepository extends CrudRepository<Cyclist, Long> {
    List<Cyclist> findAll();
    List<Cyclist> findByName(String name);
    List<Cyclist> findBySpecialty(String specialty);
    List<Cyclist> findByBirthplace(String birthplace);
    List<Cyclist> findByNameAndBirthplace(String name, String birthplace);
    List<Cyclist> findByNameAndSpecialty(String name, String specialty);
    List<Cyclist> findBySpecialtyAndBirthplace(String specialty, String birthplace);
    List<Cyclist> findByNameAndSpecialtyAndBirthplace(String name, String specialty, String birthplace);
}

