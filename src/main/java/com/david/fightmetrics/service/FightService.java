package com.david.fightmetrics.service;

import com.david.fightmetrics.dto.FighterStats;
import com.david.fightmetrics.entity.Event;
import com.david.fightmetrics.entity.Fight;
import com.david.fightmetrics.entity.Fighter;
import com.david.fightmetrics.entity.FightStatus;
import com.david.fightmetrics.entity.VictoryMethod;
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
        return fightRepository
                .findByEventOrderByMainEventDescIdAsc(event);
    }

    public List<Fight> findByFighter(Fighter fighter) {
        return fightRepository
                .findByRedCornerFighterOrBlueCornerFighterOrderByEventEventDateDesc(
                        fighter,
                        fighter
                );
    }

    public long countWins(Fighter fighter) {
        return fightRepository.countByWinnerAndStatus(
                fighter,
                FightStatus.COMPLETED
        );
    }

    public long countLosses(Fighter fighter) {
        return fightRepository.countLosses(
                fighter,
                FightStatus.COMPLETED
        );
    }

    public long countDraws(Fighter fighter) {
        return fightRepository.countDraws(
                fighter,
                FightStatus.COMPLETED,
                VictoryMethod.DRAW
        );
    }

    public FighterStats calculateStats(Fighter fighter) {
        List<Fight> fights = findByFighter(fighter);

        long totalFights = fightRepository.countTotalFights(
                fighter,
                FightStatus.COMPLETED
        );

        long wins = countWins(fighter);
        long losses = countLosses(fighter);
        long draws = countDraws(fighter);

        long koTkoWins =
                fightRepository.countByWinnerAndStatusAndVictoryMethod(
                        fighter,
                        FightStatus.COMPLETED,
                        VictoryMethod.KO_TKO
                );

        long submissionWins =
                fightRepository.countByWinnerAndStatusAndVictoryMethod(
                        fighter,
                        FightStatus.COMPLETED,
                        VictoryMethod.SUBMISSION
                );

        long unanimousDecisionWins =
                fightRepository.countByWinnerAndStatusAndVictoryMethod(
                        fighter,
                        FightStatus.COMPLETED,
                        VictoryMethod.UNANIMOUS_DECISION
                );

        long splitDecisionWins =
                fightRepository.countByWinnerAndStatusAndVictoryMethod(
                        fighter,
                        FightStatus.COMPLETED,
                        VictoryMethod.SPLIT_DECISION
                );

        long majorityDecisionWins =
                fightRepository.countByWinnerAndStatusAndVictoryMethod(
                        fighter,
                        FightStatus.COMPLETED,
                        VictoryMethod.MAJORITY_DECISION
                );

        long decisionWins =
                unanimousDecisionWins
                        + splitDecisionWins
                        + majorityDecisionWins;

        long otherWins =
                Math.max(
                        0,
                        wins
                                - koTkoWins
                                - submissionWins
                                - decisionWins
                );

        long currentWinStreak =
                calculateCurrentWinStreak(
                        fighter,
                        fights
                );

        double winPercentage =
                totalFights > 0
                        ? roundPercentage(
                        wins * 100.0 / totalFights
                )
                        : 0.0;

        long finishWins =
                koTkoWins + submissionWins;

        double finishPercentage =
                wins > 0
                        ? roundPercentage(
                        finishWins * 100.0 / wins
                )
                        : 0.0;

        return new FighterStats(
                totalFights,
                wins,
                losses,
                draws,
                koTkoWins,
                submissionWins,
                decisionWins,
                otherWins,
                currentWinStreak,
                winPercentage,
                finishPercentage
        );
    }

    private long calculateCurrentWinStreak(
            Fighter fighter,
            List<Fight> fights
    ) {
        long streak = 0;

        for (Fight fight : fights) {
            if (fight.getStatus() != FightStatus.COMPLETED) {
                continue;
            }

            if (fight.getVictoryMethod() == VictoryMethod.NO_CONTEST) {
                continue;
            }

            if (fight.getVictoryMethod() == VictoryMethod.DRAW) {
                break;
            }

            if (fight.getWinner() == null) {
                break;
            }

            if (fight.getWinner().getId().equals(fighter.getId())) {
                streak++;
            } else {
                break;
            }
        }

        return streak;
    }

    private double roundPercentage(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private void validateFight(Fight fight) {
        if (fight.getEvent() == null) {
            throw new IllegalArgumentException(
                    "El combate debe pertenecer a un evento"
            );
        }

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

        if (fight.getVictoryMethod() == VictoryMethod.DRAW &&
                fight.getWinner() != null) {
            throw new IllegalArgumentException(
                    "Un combate declarado empate no puede tener ganador"
            );
        }

        if (fight.getVictoryMethod() == VictoryMethod.NO_CONTEST &&
                fight.getWinner() != null) {
            throw new IllegalArgumentException(
                    "Un combate sin resultado no puede tener ganador"
            );
        }
    }
}
