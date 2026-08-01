package com.david.fightmetrics.service;

import com.david.fightmetrics.entity.Fighter;
import com.david.fightmetrics.entity.WeightClass;
import com.david.fightmetrics.repository.FighterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FighterService {

    private final FighterRepository fighterRepository;

    public FighterService(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    public List<Fighter> findAll() {
        return fighterRepository.findAll();
    }

    public Fighter findById(Long id) {
        return fighterRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe ningún luchador con el id " + id
                        )
                );
    }

    public Fighter save(Fighter fighter) {
        return fighterRepository.save(fighter);
    }

    public void deleteById(Long id) {
        if (!fighterRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "No existe ningún luchador con el id " + id
            );
        }

        fighterRepository.deleteById(id);
    }

    public List<Fighter> search(String query) {
        return fighterRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        query,
                        query
                );
    }

    public List<Fighter> findByWeightClass(WeightClass weightClass) {
        return fighterRepository.findByWeightClass(weightClass);
    }

    public List<Fighter> findActiveFighters() {
        return fighterRepository.findByActiveTrue();
    }
}