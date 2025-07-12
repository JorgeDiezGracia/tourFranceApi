package com.svalero.tourfrance.service;

import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.repository.CyclistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CyclistService {

    @Autowired
    private CyclistRepository cyclistRepository;

    public List<Cyclist> getAll() {
        List<Cyclist> allCyclists = cyclistRepository.findAll();
        return allCyclists;
    }

    public Cyclist get(long id) throws CyclistNotFoundException {
        return cyclistRepository.findById(id)
                .orElseThrow(CyclistNotFoundException::new);
    }

    public Cyclist add(Cyclist cyclist) {
        return cyclistRepository.save(cyclist);
    }

    public void remove(long id) throws CyclistNotFoundException {
        cyclistRepository.findById(id)
                .orElseThrow(CyclistNotFoundException::new);
        cyclistRepository.deleteById(id);
    }
}





