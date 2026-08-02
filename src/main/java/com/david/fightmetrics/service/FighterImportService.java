package com.david.fightmetrics.service;

import com.david.fightmetrics.dto.ImportResult;
import com.david.fightmetrics.entity.Fighter;
import com.david.fightmetrics.entity.WeightClass;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class FighterImportService {

    private static final int EXPECTED_COLUMNS = 11;

    private final FighterService fighterService;

    public FighterImportService(
            FighterService fighterService
    ) {
        this.fighterService = fighterService;
    }

    public ImportResult importCsv(
            MultipartFile file
    ) {
        validateFile(file);

        ImportResult result = new ImportResult();

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                file.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                )
        ) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (lineNumber == 1 && isHeader(line)) {
                    continue;
                }

                if (line.isBlank()) {
                    continue;
                }

                result.incrementTotalRows();

                try {
                    Fighter fighter = parseFighter(line);

                    fighterService.save(fighter);
                    result.incrementImportedRows();

                } catch (Exception exception) {
                    result.incrementFailedRows();

                    result.addError(
                            "Línea "
                                    + lineNumber
                                    + ": "
                                    + exception.getMessage()
                    );
                }
            }

        } catch (IOException exception) {
            throw new IllegalArgumentException(
                    "No se ha podido leer el archivo CSV",
                    exception
            );
        }

        return result;
    }

    private Fighter parseFighter(String line) {
        String[] columns = line.split(";", -1);

        if (columns.length != EXPECTED_COLUMNS) {
            throw new IllegalArgumentException(
                    "se esperaban "
                            + EXPECTED_COLUMNS
                            + " columnas, pero se encontraron "
                            + columns.length
            );
        }

        Fighter fighter = new Fighter();

        fighter.setFirstName(
                requireText(
                        columns[0],
                        "el nombre"
                )
        );

        fighter.setLastName(
                requireText(
                        columns[1],
                        "los apellidos"
                )
        );

        fighter.setNickname(
                optionalText(columns[2])
        );

        fighter.setNationality(
                requireText(
                        columns[3],
                        "la nacionalidad"
                )
        );

        fighter.setWeightClass(
                parseWeightClass(columns[4])
        );

        fighter.setHeightCm(
                parseOptionalDouble(
                        columns[5],
                        "la altura"
                )
        );

        fighter.setReachCm(
                parseOptionalDouble(
                        columns[6],
                        "el alcance"
                )
        );

        fighter.setWins(
                parseRequiredNonNegativeInteger(
                        columns[7],
                        "las victorias"
                )
        );

        fighter.setLosses(
                parseRequiredNonNegativeInteger(
                        columns[8],
                        "las derrotas"
                )
        );

        fighter.setDraws(
                parseRequiredNonNegativeInteger(
                        columns[9],
                        "los empates"
                )
        );

        fighter.setActive(
                parseBoolean(columns[10])
        );

        return fighter;
    }

    private boolean isHeader(String line) {
        return line
                .trim()
                .toLowerCase()
                .startsWith("firstname;");
    }

    private String requireText(
            String value,
            String fieldName
    ) {
        String normalized = optionalText(value);

        if (normalized == null) {
            throw new IllegalArgumentException(
                    "falta " + fieldName
            );
        }

        return normalized;
    }

    private String optionalText(String value) {
        if (value == null) {
            return null;
        }

        String normalized = value.trim();

        return normalized.isEmpty()
                ? null
                : normalized;
    }

    private WeightClass parseWeightClass(
            String value
    ) {
        String normalized = requireText(
                value,
                "la categoría de peso"
        )
                .toUpperCase()
                .replace(" ", "_")
                .replace("-", "_");

        try {
            return WeightClass.valueOf(normalized);

        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "categoría de peso no válida: "
                            + value
            );
        }
    }

    private Double parseOptionalDouble(
            String value,
            String fieldName
    ) {
        String normalized = optionalText(value);

        if (normalized == null) {
            return null;
        }

        try {
            double number = Double.parseDouble(
                    normalized.replace(",", ".")
            );

            if (number <= 0) {
                throw new IllegalArgumentException(
                        fieldName + " debe ser mayor que 0"
                );
            }

            return number;

        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    fieldName + " debe ser un número"
            );
        }
    }

    private Integer parseRequiredNonNegativeInteger(
            String value,
            String fieldName
    ) {
        String normalized = requireText(
                value,
                fieldName
        );

        try {
            int number = Integer.parseInt(normalized);

            if (number < 0) {
                throw new IllegalArgumentException(
                        fieldName + " no puede ser negativo"
                );
            }

            return number;

        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    fieldName + " debe ser un número entero"
            );
        }
    }

    private Boolean parseBoolean(
            String value
    ) {
        String normalized = requireText(
                value,
                "el estado activo"
        ).toLowerCase();

        return switch (normalized) {
            case "true", "1", "sí", "si", "yes" -> true;
            case "false", "0", "no" -> false;

            default -> throw new IllegalArgumentException(
                    "el campo active debe ser true o false"
            );
        };
    }

    private void validateFile(
            MultipartFile file
    ) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Debes seleccionar un archivo CSV"
            );
        }

        String originalFilename =
                file.getOriginalFilename();

        if (originalFilename == null ||
                !originalFilename
                        .toLowerCase()
                        .endsWith(".csv")) {
            throw new IllegalArgumentException(
                    "El archivo debe tener extensión .csv"
            );
        }
    }
}
