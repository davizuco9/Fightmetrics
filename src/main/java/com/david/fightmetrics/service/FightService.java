package com.david.fightmetrics.service;

import com.david.fightmetrics.entity.Event;
import com.david.fightmetrics.entity.Fight;
import com.david.fightmetrics.entity.Fighter;
import com.david.fightmetrics.repository.FightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FightService {

    private final FightRepository fightRepository;

    public FightService(FightRepository fightRepository) {
        this.fightRepository = fightRepository;
    }

    public List<Fight> findAll() {
        return fightRepository.findAll();
    }

    public Fight findById(Long id) {
        return fightRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe ningún combate con el id " + id
                        )
                );
    }

    public Fight save(Fight fight) {
        validateFight(fight);
        return fightRepository.save(fight);
    }

    public void deleteById(Long id) {
        if (!fightRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "No existe ningún combate con el id " + id
            );
        }

        fightRepository.deleteById(id);
    }

    public List<Fight> findByEvent(Event event) {
        return fightRepository.findByEventOrderByMainEventDescIdAsc(event);
    }

    public List<Fight> findByFighter(Fighter fighter) {
        return fightRepository
                .findByRedCornerFighterOrBlueCornerFighterOrderByEventEventDateDesc(
                        fighter,
                        fighter
                );
    }

    private void validateFight(Fight fight) {
        if (fight.getRedCornerFighter() == null ||
                fight.getBlueCornerFighter() == null) {
            throw new IllegalArgumentException(
                    "El combate debe tener dos luchadores"
            );
        }

        if (fight.getRedCornerFighter().getId()
                .equals(fight.getBlueCornerFighter().getId())) {
            throw new IllegalArgumentException(
                    "Un luchador no puede competir contra sí mismo"
            );
        }

        if (fight.getWinner() != null &&
                !fight.getWinner().getId()
                        .equals(fight.getRedCornerFighter().getId()) &&
                !fight.getWinner().getId()
                        .equals(fight.getBlueCornerFighter().getId())) {
            throw new IllegalArgumentException(
                    "El ganador debe ser uno de los dos participantes"
            );
        }
    }
}
