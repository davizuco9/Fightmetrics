package com.david.fightmetrics.repository;

import com.david.fightmetrics.entity.RankingEntry;
import com.david.fightmetrics.entity.RankingType;
import com.david.fightmetrics.entity.WeightClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RankingEntryRepository
        extends JpaRepository<RankingEntry, Long> {

    List<RankingEntry>
    findByOrganizationIgnoreCaseAndRankingTypeOrderByPositionAsc(
            String organization,
            RankingType rankingType
    );

    List<RankingEntry>
    findByOrganizationIgnoreCaseAndRankingTypeAndWeightClassOrderByPositionAsc(
            String organization,
            RankingType rankingType,
            WeightClass weightClass
    );

    Optional<RankingEntry>
    findByOrganizationIgnoreCaseAndRankingTypeAndWeightClassAndPosition(
            String organization,
            RankingType rankingType,
            WeightClass weightClass,
            Integer position
    );

    boolean existsByOrganizationIgnoreCaseAndRankingTypeAndWeightClassAndFighterId(
            String organization,
            RankingType rankingType,
            WeightClass weightClass,
            Long fighterId
    );
}