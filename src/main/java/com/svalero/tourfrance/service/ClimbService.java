package com.svalero.tourfrance.service;

import com.svalero.tourfrance.domain.Climb;
import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.Stage;
import com.svalero.tourfrance.domain.dto.*;
import com.svalero.tourfrance.exception.ClimbNotFoundException;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.exception.StageNotFoundException;
import com.svalero.tourfrance.repository.ClimbRepository;
import com.svalero.tourfrance.repository.StageRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClimbService {

    @Autowired
    private ClimbRepository climbRepository;
    @Autowired
    private StageRepository stageRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<ClimbOutDto> getAll(String name, String category, String region) {
        List<Climb> climbList;

        if (name.isEmpty() && category.isEmpty() && region.isEmpty()) {
            climbList = climbRepository.findAll();
        } else if (name.isEmpty() && !category.isEmpty() && region.isEmpty()) {
            climbList = climbRepository.findByCategory(category);
        } else if (!name.isEmpty() && category.isEmpty() && region.isEmpty()) {
            climbList = climbRepository.findByName(name);
        } else if (name.isEmpty() && category.isEmpty() && !region.isEmpty()) {
            climbList = climbRepository.findByRegion(region);
        } else if (!name.isEmpty() && !category.isEmpty() && region.isEmpty()) {
            climbList = climbRepository.findByNameAndCategory(name, category);
        } else if (!name.isEmpty() && category.isEmpty() && !region.isEmpty()) {
            climbList = climbRepository.findByNameAndRegion(name, region);
        } else if (name.isEmpty() && !category.isEmpty() && !region.isEmpty()) {
            climbList = climbRepository.findByCategoryAndRegion(category, region);
        } else {
            climbList = climbRepository.findByNameAndCategoryAndRegion(name, category, region);
        }


        List<ClimbOutDto> climbOutDtos = modelMapper.map(climbList, new TypeToken<List<ClimbOutDto>>() {}.getType());
        return climbOutDtos;
    }

    public Climb get(long id) throws ClimbNotFoundException {
        return climbRepository.findById(id)
                .orElseThrow(ClimbNotFoundException::new);
    }

    public ClimbOutDto add(long stageId, ClimbRegistrationDto climbInDto) throws StageNotFoundException {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(StageNotFoundException::new);

        Climb climb = modelMapper.map(climbInDto, Climb.class);
        climb.setStage(stage);
        Climb newClimb = climbRepository.save(climb);

        return modelMapper.map(newClimb, ClimbOutDto.class);
    }

    public ClimbOutDto modify(long climbId, ClimbInDto climbInDto) throws ClimbNotFoundException, StageNotFoundException {
        Climb climb = climbRepository.findById(climbId)
                .orElseThrow(ClimbNotFoundException::new);

        Stage stage = stageRepository.findById(climbInDto.getStageId())
                        .orElseThrow(StageNotFoundException::new);

        climb.setStage(stage);
        modelMapper.typeMap(ClimbInDto.class, Climb.class)
                .addMappings(mapper -> mapper.skip(Climb::setId));
        modelMapper.map(climbInDto, climb);

        climbRepository.save(climb);

        return modelMapper.map(climb, ClimbOutDto.class);
    }

    public void remove(long id) throws ClimbNotFoundException {
        climbRepository.findById(id)
                .orElseThrow(ClimbNotFoundException::new);
        climbRepository.deleteById(id);
    }
}
