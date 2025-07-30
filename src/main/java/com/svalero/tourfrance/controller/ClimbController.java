package com.svalero.tourfrance.controller;

import com.svalero.tourfrance.domain.Climb;
import com.svalero.tourfrance.domain.dto.ClimbInDto;
import com.svalero.tourfrance.domain.dto.ClimbOutDto;
import com.svalero.tourfrance.domain.dto.ClimbRegistrationDto;
import com.svalero.tourfrance.domain.dto.ErrorResponse;
import com.svalero.tourfrance.exception.ClimbNotFoundException;
import com.svalero.tourfrance.exception.StageNotFoundException;
import com.svalero.tourfrance.service.ClimbService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.security.cert.CertificateRevokedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClimbController {

    @Autowired
    private ClimbService climbService;
    private final Logger logger = LoggerFactory.getLogger(ClimbController.class);

    @GetMapping("climbs")
    public ResponseEntity<List<ClimbOutDto>> getAll(@RequestParam(value ="name", defaultValue = "") String name,
                                                    @RequestParam(value = "category", defaultValue = "") String category,
                                                    @RequestParam(value = "region", defaultValue = "") String region) {
        logger.info("BEGIN getall");
        List<ClimbOutDto> climbs = climbService.getAll(name, category, region);
        logger.info("END getAll");
        return new ResponseEntity<>(climbs, HttpStatus.OK);
    }

    @GetMapping("climbs/{climbId}")
    public ResponseEntity<Climb> getClimb(@PathVariable long climbId) throws ClimbNotFoundException {
        logger.info("BEGIN getClimb");
        Climb climb = climbService.get(climbId);
        logger.info("END getClimb");
        return new ResponseEntity<>(climb, HttpStatus.OK);
    }

    @PostMapping("/stages/{stageId}/climbs")
    public ResponseEntity<ClimbOutDto> addClimb(@PathVariable long stageId, @Valid @RequestBody ClimbRegistrationDto climb) throws StageNotFoundException {
        logger.info("BEGIN addClimb");
        ClimbOutDto newClimb = climbService.add(stageId, climb);
        logger.info("END addClimb");
        return new ResponseEntity<>(newClimb, HttpStatus.CREATED);
    }

    @PutMapping("/climbs/{climbId}")
    public ResponseEntity<ClimbOutDto> modifyClimb(@PathVariable long climbId, @Valid @RequestBody ClimbInDto climb) throws ClimbNotFoundException, StageNotFoundException {
        logger.info("BEGIN modifyClimb");
        ClimbOutDto modifiedClimb = climbService.modify(climbId, climb);
        logger.info("END modifyClimb");
        return new ResponseEntity<>(modifiedClimb, HttpStatus.OK);
    }

    @DeleteMapping("/climbs/{climbId}")
    public ResponseEntity<Void> removeClimb(@PathVariable long climbId) throws ClimbNotFoundException{
        logger.info("BEGIN removeClimb");
        climbService.remove(climbId);
        logger.info("END removeClimb");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ClimbNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClimbNotFoundException(ClimbNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStageNotFoundException(StageNotFoundException exception) {
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
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
