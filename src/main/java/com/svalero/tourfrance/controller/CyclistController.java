package com.svalero.tourfrance.controller;

import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.dto.CyclistInDto;
import com.svalero.tourfrance.domain.dto.CyclistOutDto;
import com.svalero.tourfrance.domain.dto.CyclistRegistrationDto;
import com.svalero.tourfrance.domain.dto.ErrorResponse;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.service.CyclistService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CyclistController {

    @Autowired
    private CyclistService cyclistService;
    private final Logger logger = LoggerFactory.getLogger(CyclistController.class);

    @GetMapping("/cyclists")
    public ResponseEntity<List<CyclistOutDto>> getAll(@RequestParam(value = "name", defaultValue = "") String name,
                                                          @RequestParam(value = "specialty", defaultValue = "") String specialty,
                                                            @RequestParam(value = "birthplace", defaultValue = "") String birthplace) {

        logger.info("BEGIN getAll");
        List<CyclistOutDto> cyclists = cyclistService.getAll(name, specialty, birthplace);
        logger.info("END getAll");
        return new ResponseEntity<>(cyclists, HttpStatus.OK);
    }

    @GetMapping("/cyclists/:cyclistId")
    public ResponseEntity<Cyclist> getCyclist(long cyclistId) throws CyclistNotFoundException {
        logger.info("BEGIN getCyclist");
        Cyclist cyclist = cyclistService.get(cyclistId);
        logger.info("END.getCyclist");
        return new ResponseEntity<>(cyclist, HttpStatus.OK);
    }

    @PostMapping("/teams/:teamId/cyclists")
    public ResponseEntity<CyclistOutDto> addCyclist(long teamId, @Valid @RequestBody CyclistRegistrationDto cyclist) throws TeamNotFoundException {
        logger.info("BEGIN addCyclist");
        CyclistOutDto newCyclist = cyclistService.add(teamId, cyclist);
        logger.info("END addCyclist");
        return new ResponseEntity<>(newCyclist, HttpStatus.CREATED);
    }

    @PutMapping("/cyclists/:cyclistId")
    public ResponseEntity<CyclistOutDto> modifyCyclist(long cyclistId, @Valid @RequestBody CyclistInDto cyclist) throws CyclistNotFoundException, TeamNotFoundException {
        logger.info("BEGIN modifyCyclist");
        CyclistOutDto modifiedCyclist = cyclistService.modify(cyclistId, cyclist);
        logger.info("END modifyCyclist");
        return new ResponseEntity<>(modifiedCyclist, HttpStatus.OK);
    }

    @DeleteMapping("/cyclists/:cyclistId")
    public ResponseEntity<Void> removeCyclist(long cyclistId) throws CyclistNotFoundException{
        logger.info("BEGIN removeCyclist");
        cyclistService.remove(cyclistId);
        logger.info("END removeCyclist");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CyclistNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCyclistNotFoundException(CyclistNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTeamNotFoundException(TeamNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
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
