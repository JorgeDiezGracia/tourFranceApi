package com.svalero.tourfrance.repository;

import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CyclistRepository extends CrudRepository<Cyclist, Long> {
    List<Cyclist> findAll();
    List<Cyclist> findByName(String name);
    List<Cyclist> findByNameAndTitles(String name, int titles);
}

