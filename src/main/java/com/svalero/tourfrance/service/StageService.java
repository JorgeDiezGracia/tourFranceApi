package com.svalero.tourfrance.service;

import com.svalero.tourfrance.domain.Stage;
import com.svalero.tourfrance.domain.dto.StageInDto;
import com.svalero.tourfrance.domain.dto.StageOutDto;
import com.svalero.tourfrance.domain.dto.StageRegistrationDto;
import com.svalero.tourfrance.domain.dto.TeamOutDto;
import com.svalero.tourfrance.exception.StageNotFoundException;
import com.svalero.tourfrance.repository.StageRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StageService {

    @Autowired
    private StageRepository stageRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<StageOutDto> getAll(String departure, String arrival, String type) {
        List<Stage> stageList;

        if (departure.isEmpty() && arrival.isEmpty() && type.isEmpty()) {
            stageList = stageRepository.findAll();
        } else if (departure.isEmpty() && !arrival.isEmpty() && type.isEmpty()) {
            stageList = stageRepository.findByArrival(arrival);
        } else if (!departure.isEmpty() && arrival.isEmpty() && type.isEmpty()) {
            stageList = stageRepository.findByDeparture(departure);
        } else if (departure.isEmpty() && arrival.isEmpty() && !type.isEmpty()) {
            stageList = stageRepository.findByType(type);
        } else if (!departure.isEmpty() && !arrival.isEmpty() && type.isEmpty()) {
            stageList = stageRepository.findByDepartureAndArrival(departure, arrival);
        } else if (!departure.isEmpty() && arrival.isEmpty() && !type.isEmpty()) {
            stageList = stageRepository.findByDepartureAndType(departure, type);
        } else if (departure.isEmpty() && !arrival.isEmpty() && !type.isEmpty()) {
            stageList = stageRepository.findByArrivalAndType(arrival, type);
        } else {
            stageList = stageRepository.findByDepartureAndArrivalAndType(departure, arrival, type);
        }

        List<StageOutDto> stageOutDtos = modelMapper.map(stageList, new TypeToken<List<StageOutDto>>() {}.getType());
        return stageOutDtos;

    }

    public Stage get(long id) throws StageNotFoundException {
        return stageRepository.findById(id)
                .orElseThrow(StageNotFoundException::new);
    }

    public StageOutDto add(StageRegistrationDto stageInDto) {
        Stage stage = modelMapper.map(stageInDto, Stage.class);
        Stage newStage = stageRepository.save(stage);
        return modelMapper.map(newStage, StageOutDto.class);
    }

    public StageOutDto modify(long stageId, StageInDto stageInDto) throws StageNotFoundException {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(StageNotFoundException::new);

        modelMapper.map(stageInDto, stage);
        modelMapper.typeMap(StageInDto.class, Stage.class)
                .addMappings(mapper -> mapper.skip(Stage::setId));
        modelMapper.map(stageInDto, stage);
        stageRepository.save(stage);
        return modelMapper.map(stage, StageOutDto.class);
    }

    public void remove(long id) throws StageNotFoundException{
        stageRepository.findById(id)
                .orElseThrow(StageNotFoundException::new);
        stageRepository.deleteById(id);
    }
}
