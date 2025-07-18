package com.svalero.tourfrance.repository;

import com.svalero.tourfrance.domain.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    List<Team> findAll();
    List<Team> findByName(String name);
    List<Team> findByCountry(String country);
    List<Team> findByEmail(String email);
    List<Team> findByNameAndCountry(String name, String country);
    List<Team> findByNameAndEmail(String name, String email);
    List<Team> findByCountryAndEmail(String country, String email);
    List<Team> findByNameAndCountryAndEmail(String name, String country, String email);

}
