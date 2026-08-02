package com.david.fightmetrics.controller;

import com.david.fightmetrics.service.FavoriteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public String listFavorites(
            Authentication authentication,
            Model model
    ) {
        String username = authentication.getName();

        model.addAttribute(
                "favorites",
                favoriteService.findByUsername(username)
        );

        model.addAttribute(
                "favoriteCount",
                favoriteService.countByUsername(username)
        );

        return "favorites/list";
    }

    @PostMapping("/fighters/{fighterId}")
    public String addFavorite(
            @PathVariable Long fighterId,
            Authentication authentication,
            @RequestParam(
                    defaultValue = "/fighters"
            ) String redirectUrl
    ) {
        favoriteService.addFavorite(
                authentication.getName(),
                fighterId
        );

        return "redirect:" + sanitizeRedirectUrl(redirectUrl);
    }

    @PostMapping("/fighters/{fighterId}/delete")
    public String removeFavorite(
            @PathVariable Long fighterId,
            Authentication authentication,
            @RequestParam(
                    defaultValue = "/favorites"
            ) String redirectUrl
    ) {
        favoriteService.removeFavorite(
                authentication.getName(),
                fighterId
        );

        return "redirect:" + sanitizeRedirectUrl(redirectUrl);
    }

    private String sanitizeRedirectUrl(String redirectUrl) {
        if (redirectUrl == null ||
                !redirectUrl.startsWith("/") ||
                redirectUrl.startsWith("//")) {
            return "/favorites";
        }

        return redirectUrl;
    }
}