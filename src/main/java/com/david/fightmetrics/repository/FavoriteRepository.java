package com.david.fightmetrics.repository;

import com.david.fightmetrics.entity.Favorite;
import com.david.fightmetrics.entity.Fighter;
import com.david.fightmetrics.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository
        extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserOrderByCreatedAtDesc(
            User user
    );

    Optional<Favorite> findByUserAndFighter(
            User user,
            Fighter fighter
    );

    boolean existsByUserAndFighter(
            User user,
            Fighter fighter
    );

    void deleteByUserAndFighter(
            User user,
            Fighter fighter
    );

    long countByUser(User user);
}
