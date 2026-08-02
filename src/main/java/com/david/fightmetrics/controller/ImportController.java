package com.david.fightmetrics.controller;

import com.david.fightmetrics.dto.ImportResult;
import com.david.fightmetrics.service.FighterImportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/import")
public class ImportController {

    private final FighterImportService fighterImportService;

    public ImportController(
            FighterImportService fighterImportService
    ) {
        this.fighterImportService = fighterImportService;
    }

    @GetMapping("/fighters")
    public String showFighterImportForm() {
        return "import/fighters";
    }

    @PostMapping("/fighters")
    public String importFighters(
            @RequestParam("file") MultipartFile file,
            Model model
    ) {
        try {
            ImportResult result =
                    fighterImportService.importCsv(file);

            model.addAttribute(
                    "importResult",
                    result
            );

        } catch (IllegalArgumentException exception) {
            model.addAttribute(
                    "importError",
                    exception.getMessage()
            );
        }

        return "import/fighters";
    }
}