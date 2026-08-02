package com.david.fightmetrics.service;

import com.david.fightmetrics.entity.Fighter;
import com.david.fightmetrics.entity.RankingEntry;
import com.david.fightmetrics.entity.RankingType;
import com.david.fightmetrics.entity.WeightClass;
import com.david.fightmetrics.repository.RankingEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {

    private final RankingEntryRepository rankingEntryRepository;

    public RankingService(
            RankingEntryRepository rankingEntryRepository
    ) {
        this.rankingEntryRepository = rankingEntryRepository;
    }

    public List<RankingEntry> findAll() {
        return rankingEntryRepository.findAll();
    }

    public RankingEntry findById(Long id) {
        return rankingEntryRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe ninguna entrada de ranking con el id " + id
                        )
                );
    }

    public List<RankingEntry> findPoundForPound(
            String organization
    ) {
        return rankingEntryRepository
                .findByOrganizationIgnoreCaseAndRankingTypeOrderByPositionAsc(
                        organization,
                        RankingType.POUND_FOR_POUND
                );
    }

    public List<RankingEntry> findByWeightClass(
            String organization,
            WeightClass weightClass
    ) {
        return rankingEntryRepository
                .findByOrganizationIgnoreCaseAndRankingTypeAndWeightClassOrderByPositionAsc(
                        organization,
                        RankingType.WEIGHT_CLASS,
                        weightClass
                );
    }

    public RankingEntry save(RankingEntry rankingEntry) {
        normalizeAndValidate(rankingEntry);

        return rankingEntryRepository.save(rankingEntry);
    }

    public void deleteById(Long id) {
        if (!rankingEntryRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "No existe ninguna entrada de ranking con el id " + id
            );
        }

        rankingEntryRepository.deleteById(id);
    }

    private void normalizeAndValidate(
            RankingEntry rankingEntry
    ) {
        if (rankingEntry.getOrganization() == null ||
                rankingEntry.getOrganization().isBlank()) {
            throw new IllegalArgumentException(
                    "La organización es obligatoria"
            );
        }

        rankingEntry.setOrganization(
                rankingEntry.getOrganization().trim().toUpperCase()
        );

        if (rankingEntry.getRankingType() == null) {
            throw new IllegalArgumentException(
                    "El tipo de ranking es obligatorio"
            );
        }

        if (rankingEntry.getFighter() == null ||
                rankingEntry.getFighter().getId() == null) {
            throw new IllegalArgumentException(
                    "El luchador es obligatorio"
            );
        }

        if (rankingEntry.getPosition() == null ||
                rankingEntry.getPosition() < 1) {
            throw new IllegalArgumentException(
                    "La posición debe ser mayor o igual que 1"
            );
        }

        if (rankingEntry.getRankingType() == RankingType.WEIGHT_CLASS &&
                rankingEntry.getWeightClass() == null) {
            throw new IllegalArgumentException(
                    "El ranking por categoría necesita una categoría de peso"
            );
        }

        if (rankingEntry.getRankingType() == RankingType.POUND_FOR_POUND) {
            rankingEntry.setWeightClass(null);
        }

        validatePositionAvailability(rankingEntry);
        validateFighterAvailability(rankingEntry);
    }

    private void validatePositionAvailability(
            RankingEntry rankingEntry
    ) {
        rankingEntryRepository
                .findByOrganizationIgnoreCaseAndRankingTypeAndWeightClassAndPosition(
                        rankingEntry.getOrganization(),
                        rankingEntry.getRankingType(),
                        rankingEntry.getWeightClass(),
                        rankingEntry.getPosition()
                )
                .filter(existingEntry ->
                        rankingEntry.getId() == null ||
                                !existingEntry.getId().equals(rankingEntry.getId())
                )
                .ifPresent(existingEntry -> {
                    throw new IllegalArgumentException(
                            "La posición "
                                    + rankingEntry.getPosition()
                                    + " ya está ocupada en ese ranking"
                    );
                });
    }

    private void validateFighterAvailability(
            RankingEntry rankingEntry
    ) {
        List<RankingEntry> entries;

        if (rankingEntry.getRankingType() == RankingType.POUND_FOR_POUND) {
            entries = findPoundForPound(
                    rankingEntry.getOrganization()
            );
        } else {
            entries = findByWeightClass(
                    rankingEntry.getOrganization(),
                    rankingEntry.getWeightClass()
            );
        }

        boolean fighterAlreadyRanked = entries.stream()
                .anyMatch(existingEntry ->
                        existingEntry.getFighter()
                                .getId()
                                .equals(
                                        rankingEntry.getFighter().getId()
                                )
                                &&
                                (
                                        rankingEntry.getId() == null ||
                                                !existingEntry.getId()
                                                        .equals(
                                                                rankingEntry.getId()
                                                        )
                                )
                );

        if (fighterAlreadyRanked) {
            Fighter fighter = rankingEntry.getFighter();

            throw new IllegalArgumentException(
                    fighter.getFirstName()
                            + " "
                            + fighter.getLastName()
                            + " ya aparece en ese ranking"
            );
        }
    }
}
