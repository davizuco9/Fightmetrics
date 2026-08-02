package com.david.fightmetrics.dto;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {

    private int totalRows;
    private int importedRows;
    private int failedRows;
    private final List<String> errors = new ArrayList<>();

    public ImportResult() {
    }

    public void incrementTotalRows() {
        totalRows++;
    }

    public void incrementImportedRows() {
        importedRows++;
    }

    public void incrementFailedRows() {
        failedRows++;
    }

    public void addError(String error) {
        errors.add(error);
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getImportedRows() {
        return importedRows;
    }

    public int getFailedRows() {
        return failedRows;
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
