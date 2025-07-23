package com.svalero.tourfrance.controller;

import com.svalero.tourfrance.domain.Team;
import com.svalero.tourfrance.domain.dto.TeamInDto;
import com.svalero.tourfrance.domain.dto.TeamOutDto;
import com.svalero.tourfrance.domain.dto.TeamRegistrationDto;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.domain.dto.ErrorResponse;
import com.svalero.tourfrance.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TeamController {

    @Autowired
    private TeamService teamService;
    private final Logger logger = LoggerFactory.getLogger(TeamController.class);

    @GetMapping("/teams")
    public ResponseEntity<List<TeamOutDto>> getAll(@RequestParam(value = "name", defaultValue = "") String name,
                                                   @RequestParam(value = "country", defaultValue = "") String country,
                                                   @RequestParam(value = "email", defaultValue = "") String email) {

        logger.info("BEGIN getAll");
        List<TeamOutDto> teams = teamService.getAll(name, country, email);
        logger.info("END getAll");
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }


    @GetMapping("/teams/:teamId")
    public ResponseEntity<Team> getTeam(long teamId) throws TeamNotFoundException {
        logger.info("BEGIN getTeam");
        Team team = teamService.get(teamId);
        logger.info("END getTeam");
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    @PostMapping("/teams")
    public ResponseEntity<TeamOutDto> addTeam(@Valid @RequestBody TeamRegistrationDto team) {

        logger.info("BEGIN addTeam");
    TeamOutDto newTeam = teamService.add(team);
        logger.info("END addTeam");
        return new ResponseEntity<>(newTeam, HttpStatus.CREATED);

}

    @PutMapping("/teams/:teamId")
    public ResponseEntity<TeamOutDto> modifyTeam(long teamId, @Valid @RequestBody TeamInDto team) throws TeamNotFoundException{
    logger.info("BEGIN modifyTeam");
    TeamOutDto modifiedTeam = teamService.modify(teamId, team);
    logger.info("END modifyTeam");
    return new ResponseEntity<>(modifiedTeam, HttpStatus.OK);
        }

    @DeleteMapping("/teams/:teamId")
    public ResponseEntity<Void> removeTeam(long teamId) throws TeamNotFoundException{
    logger.info("BEGIN removeTeam");
    teamService.remove(teamId);
    logger.info("END removeTeam");
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
