package com.svalero.tourfrance.controller;
import com.svalero.tourfrance.domain.Sponsor;
import com.svalero.tourfrance.domain.dto.SponsorInDto;
import com.svalero.tourfrance.domain.dto.SponsorOutDto;
import com.svalero.tourfrance.domain.dto.SponsorRegistrationDto;
import com.svalero.tourfrance.domain.dto.ErrorResponse;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.exception.SponsorNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.service.SponsorService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
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
public class SponsorController {

    @Autowired
    private SponsorService sponsorService;
    private final Logger logger = LoggerFactory.getLogger(SponsorController.class);

    @GetMapping("/sponsors")
    public ResponseEntity<List>SponsorOutDto>> getAll(@RequestParam(value = "name", defaultValue = "") String name,
                                                      @RequestParam(value = "country", defaultValue = "") String country,
                                                      @RequestParam(value = "email", defaultValue = "") String email) {

        logger.info("BEGIN getAll");
        List<SponsorOutDto> sponsors = sponsorService.getAll(name, country, email);
        logger.info("END getAll");
        return new ResponseEntity<>(sponsors, HttpStatus.OK);
    }

    @GetMapping("/sponsors/:sponsorId")
    public ResponseEntity<Sponsor> getSponsor(long sponsorId) throws SponsorNotFoundException {
        logger.info("BEGIN getSponsor");
        Sponsor sponsor = sponsorService.get(sponsorId);
        logger.info("END getSponsor");
        return new ResponseEntity<>(sponsor, HttpStatus.OK);
    }

    @PostMapping("/teams/:teamId/sponsors")
    public ResponseEntity<SponsorOutDto> addSponsor(long teamId, @Valid @RequestBody SponsorRegistrationDto sponsor) throws TeamNotFoundException {
        logger.info("BEGIN addSponsor");
        SponsorOutDto newSponsor = sponsorService.add(teamId, sponsor);
        logger.info("END addSponsor");
        return new ResponseEntity<>(newSponsor, HttpStatus.CREATED);
    }

    @PutMapping("/sponsors/:sponsorId")
    public ResponseEntity<SponsorOutDto> modifySponsor(long sponsorId, @Valid @RequestBody SponsorInDto sponsor) throws SponsorNotFoundException, TeamNotFoundException {
        logger.info("BEGIN modifySponsor");
        SponsorOutDto modifiedSponsor = sponsorService.modify(sponsorId, sponsor);
        logger.info("END modifySponsor");
        return new ResponseEntity<>(modifiedSponsor, HttpStatus.OK);
    }

    @DeleteMapping("/sponsors/sponsorId")
    public ResponseEntity<Void> removeSponsor(long sponsorId) throws SponsorNotFoundException {
        logger.info("BEGIN removeSponsor");
        sponsorService.remove(sponsorId);
        logger.info("END removeSponsor");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SponsorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSponsorNotFoundException(SponsorNotFoundException exception) {
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
