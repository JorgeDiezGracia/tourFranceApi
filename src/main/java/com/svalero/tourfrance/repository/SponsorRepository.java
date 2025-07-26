package com.svalero.tourfrance.repository;

import com.svalero.tourfrance.domain.Sponsor;
import com.svalero.tourfrance.domain.Sponsor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SponsorRepository extends CrudRepository<Sponsor, Long> {
    List<Sponsor> findAll();
    List<Sponsor> findByName(String name);
    List<Sponsor> findByCountry(String country);
    List<Sponsor> findByEmail(String email);
    List<Sponsor> findByNameAndEmail(String name, String email);
    List<Sponsor> findByNameAndCountry(String name, String country);
    List<Sponsor> findByCountryAndEmail(String country, String email);
    List<Sponsor> findByNameAndCountryAndEmail(String name, String country, String email);
}

