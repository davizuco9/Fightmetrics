package com.david.fightmetrics.repository;

import com.david.fightmetrics.entity.Event;
import com.david.fightmetrics.entity.Fight;
import com.david.fightmetrics.entity.Fighter;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
