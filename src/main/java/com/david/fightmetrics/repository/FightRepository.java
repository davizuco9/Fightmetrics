package com.david.fightmetrics.repository;

import com.david.fightmetrics.entity.Event;
import com.david.fightmetrics.entity.Fight;
import com.david.fightmetrics.entity.Fighter;
import com.david.fightmetrics.entity.FightStatus;
import com.david.fightmetrics.entity.VictoryMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FightRepository extends JpaRepository<Fight, Long> {

    List<Fight> findByEventOrderByMainEventDescIdAsc(Event event);

    List<Fight> findByRedCornerFighterOrBlueCornerFighterOrderByEventEventDateDesc(
            Fighter redCornerFighter,
            Fighter blueCornerFighter
    );

    boolean existsByEventAndRedCornerFighterAndBlueCornerFighter(
            Event event,
            Fighter redCornerFighter,
            Fighter blueCornerFighter
    );

    long countByWinnerAndStatus(
            Fighter winner,
            FightStatus status
    );

    long countByWinnerAndStatusAndVictoryMethod(
            Fighter winner,
            FightStatus status,
            VictoryMethod victoryMethod
    );

    @Query("""
            SELECT COUNT(f)
            FROM Fight f
            WHERE f.status = :status
              AND (
                    f.redCornerFighter = :fighter
                    OR f.blueCornerFighter = :fighter
              )
            """)
    long countTotalFights(
            @Param("fighter") Fighter fighter,
            @Param("status") FightStatus status
    );

    @Query("""
            SELECT COUNT(f)
            FROM Fight f
            WHERE f.status = :status
              AND f.winner IS NOT NULL
              AND f.winner <> :fighter
              AND (
                    f.redCornerFighter = :fighter
                    OR f.blueCornerFighter = :fighter
              )
            """)
    long countLosses(
            @Param("fighter") Fighter fighter,
            @Param("status") FightStatus status
    );

    @Query("""
            SELECT COUNT(f)
            FROM Fight f
            WHERE f.status = :status
              AND f.victoryMethod = :victoryMethod
              AND (
                    f.redCornerFighter = :fighter
                    OR f.blueCornerFighter = :fighter
              )
            """)
    long countDraws(
            @Param("fighter") Fighter fighter,
            @Param("status") FightStatus status,
            @Param("victoryMethod") VictoryMethod victoryMethod
    );
}