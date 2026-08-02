package com.david.fightmetrics.service;

import com.david.fightmetrics.entity.Favorite;
import com.david.fightmetrics.entity.Fighter;
import com.david.fightmetrics.entity.User;
import com.david.fightmetrics.repository.FavoriteRepository;
import com.david.fightmetrics.repository.FighterRepository;
import com.david.fightmetrics.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final FighterRepository fighterRepository;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            UserRepository userRepository,
            FighterRepository fighterRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.fighterRepository = fighterRepository;
    }

    public List<Favorite> findByUsername(String username) {
        User user = findUserByUsername(username);

        return favoriteRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public long countByUsername(String username) {
        User user = findUserByUsername(username);

        return favoriteRepository.countByUser(user);
    }

    public boolean isFavorite(
            String username,
            Long fighterId
    ) {
        User user = findUserByUsername(username);
        Fighter fighter = findFighterById(fighterId);

        return favoriteRepository.existsByUserAndFighter(
                user,
                fighter
        );
    }

    @Transactional
    public void addFavorite(
            String username,
            Long fighterId
    ) {
        User user = findUserByUsername(username);
        Fighter fighter = findFighterById(fighterId);

        if (favoriteRepository.existsByUserAndFighter(
                user,
                fighter
        )) {
            return;
        }

        Favorite favorite = new Favorite(
                user,
                fighter
        );

        favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(
            String username,
            Long fighterId
    ) {
        User user = findUserByUsername(username);
        Fighter fighter = findFighterById(fighterId);

        favoriteRepository.deleteByUserAndFighter(
                user,
                fighter
        );
    }

    private User findUserByUsername(String username) {
        return userRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe el usuario " + username
                        )
                );
    }

    private Fighter findFighterById(Long fighterId) {
        return fighterRepository
                .findById(fighterId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe ningún luchador con el id "
                                        + fighterId
                        )
                );
    }
}
