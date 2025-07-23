package com.svalero.tourfrance.repository;

import com.svalero.tourfrance.domain.Stage;
import com.svalero.tourfrance.domain.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StageRepository extends CrudRepository<Stage, Long> {

    List<Stage> findAll();
    List<Stage> findByDeparture(String departure);
    List<Stage> findByArrival(String arrival);
    List<Stage> findByType(String type);
    List<Stage> findByDepartureAndArrival(String departure, String arrival);
    List<Stage> findByDepartureAndType(String departure, String type);
    List<Stage> findByArrivalAndType(String arrival, String type);
    List<Stage> findByDepartureAndArrivalAndType(String departure, String arrival, String type);
}
