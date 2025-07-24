package com.svalero.tourfrance.repository;

import com.svalero.tourfrance.domain.Climb;
import com.svalero.tourfrance.domain.Cyclist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClimbRepository extends CrudRepository<Climb, Long> {

    List<Climb> findAll();
    List<Climb> findByName(String name);
    List<Climb> findByCategory(String category);
    List<Climb> findByRegion(String region);
    List<Climb> findByNameAndRegion(String name, String region);
    List<Climb> findByNameAndCategory(String name, String category);
    List<Climb> findByCategoryAndRegion(String category, String region);
    List<Climb> findByNameAndCategoryAndRegion(String name, String category, String region);

}
