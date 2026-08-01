package com.david.fightmetrics.repository;

import com.david.fightmetrics.entity.Fighter;
import com.david.fightmetrics.entity.WeightClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FighterRepository extends JpaRepository<Fighter, Long> {

    List<Fighter> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName
    );

    List<Fighter> findByWeightClass(WeightClass weightClass);

    List<Fighter> findByActiveTrue();
}