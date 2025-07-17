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

    @GetMapping("/cyclists")
    public ResponseEntity<List<CyclistOutDto>> getAll(@RequestParam(value = "name", defaultValue = "") String name,
                                                          @RequestParam(value = "specialty", defaultValue = "") String specialty,
                                                            @RequestParam(value = "birthplace", defaultValue = "") String birthplace) {

        return new ResponseEntity<>(cyclistService.getAll(name,specialty, birthplace), HttpStatus.OK);
    }

    @GetMapping("/cyclists/:cyclistId")
    public ResponseEntity<Cyclist> getCyclist(long cyclistId) throws CyclistNotFoundException {
        Cyclist cyclist = cyclistService.get(cyclistId);
        return new ResponseEntity<>(cyclist, HttpStatus.OK);
    }

    @PostMapping("/teams/:teamId/cyclists")
    public ResponseEntity<CyclistOutDto> addCyclist(long teamId, @Valid @RequestBody CyclistRegistrationDto cyclist) throws TeamNotFoundException {
        CyclistOutDto newCyclist = cyclistService.add(teamId, cyclist);
        return new ResponseEntity<>(newCyclist, HttpStatus.CREATED);
    }

    @PutMapping("/cyclists/:cyclistId")
    public ResponseEntity<CyclistOutDto> modifyCyclist(long cyclistId, @Valid @RequestBody CyclistInDto cyclist) throws CyclistNotFoundException, TeamNotFoundException {
        CyclistOutDto modifiedCyclist = cyclistService.modify(cyclistId, cyclist);
        return new ResponseEntity<>(modifiedCyclist, HttpStatus.OK);
    }

    @DeleteMapping("/cyclists/:cyclistId")
    public ResponseEntity<Void> removeCyclist(long cyclistId) throws CyclistNotFoundException{
        cyclistService.remove(cyclistId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CyclistNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCyclistNotFoundException(CyclistNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTeamNotFoundException(TeamNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
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
        return new ResponseEntity<>(ErrorResponse.validationError(errors), HttpStatus.BAD_REQUEST);
    }
}
