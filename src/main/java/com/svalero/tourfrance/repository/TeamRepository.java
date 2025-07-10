package com.svalero.tourfrance.repository;

import com.svalero.tourfrance.domain.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    List<Team> findAll();
    List<Team> findByName(String name);
    List<Team> findByNameAndCountry(String name, String country);
}
