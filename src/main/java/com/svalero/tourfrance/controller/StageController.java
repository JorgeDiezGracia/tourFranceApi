package com.svalero.tourfrance.controller;


import com.svalero.tourfrance.domain.Stage;
import com.svalero.tourfrance.domain.dto.StageOutDto;
import com.svalero.tourfrance.domain.dto.StageInDto;
import com.svalero.tourfrance.domain.dto.StageRegistrationDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.svalero.tourfrance.service.StageService;
import com.svalero.tourfrance.exception.StageNotFoundException;
import com.svalero.tourfrance.domain.dto.ErrorResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StageController {

    @Autowired
    private StageService stageService;
    private final Logger logger = LoggerFactory.getLogger(StageController.class);

    @GetMapping("stages")
    public ResponseEntity<List<StageOutDto>> getAll(@RequestParam(value = "departure", defaultValue = "") String departure,
                                                    @RequestParam(value = "arrival", defaultValue = "") String arrival,
                                                    @RequestParam(value = "type", defaultValue = "") String type) {
        logger.info("BEGIN getAll");
        List<StageOutDto> stages = stageService.getAll(departure, arrival, type);
        logger.info("END getAll");
        return new ResponseEntity<>(stages, HttpStatus.OK);
    }

    @GetMapping("/stages/{stageId}")
    public ResponseEntity<Stage> getStage(@PathVariable long stageId) throws StageNotFoundException {
        logger.info("BEGIN getStage");
        Stage stage = stageService.get(stageId);
        logger.info("END getSTage");
        return new ResponseEntity<>(stage, HttpStatus.OK);
    }

    @PostMapping("/stages")
    public ResponseEntity<StageOutDto> addStage(@Valid @RequestBody StageRegistrationDto stage) {
        logger.info("BEGIN addStage");
        StageOutDto newStage = stageService.add(stage);
        logger.info("END addStage");
        return new ResponseEntity<>(newStage, HttpStatus.CREATED);
    }

    @PutMapping("/stages/{stageId}")
    public ResponseEntity<StageOutDto> modifyStage(@PathVariable long stageId, @Valid @RequestBody StageInDto stage) throws StageNotFoundException {
        logger.info("BEGIN modifyStage");
        StageOutDto modifiedStage = stageService.modify(stageId, stage);
        logger.info("END modifyStage");
        return new ResponseEntity<>(modifiedStage, HttpStatus.OK);

    }

    @DeleteMapping("/stages/{stageId}")
    public ResponseEntity<Void> removeStage(@PathVariable long stageId) throws StageNotFoundException {
        logger.info("BEGIN removeSTage");
        stageService.remove(stageId);
        logger.info("END removeStage");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(StageNotFoundException.class)
    public ResponseEntity<ErrorResponse>
    handleStageNotFoundException(StageNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse>MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(ErrorResponse.validationError(errors), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse error = ErrorResponse.generalError(500, "Internal Server Error");
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
