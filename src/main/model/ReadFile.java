package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import exceptions.NonExistentColumnException;

// Scans CSV file for quantitative data given by the user
// Assigns values to a list of doubles, if there is no quantitative value the list is empty

public class ReadFile {

    private String fileName;
    private String variableOfInterest;
    private List<Double> currentData;
    private String headerLine;

    public ReadFile(String fileName, String variableOfInterest) {
        // parsing a CSV file into Scanner class constructor
        this.fileName = fileName;
        this.variableOfInterest = variableOfInterest;
        currentData = loadData(fileName, variableOfInterest);

    }

    // REQUIRES: csv is not empty, variableOfInterest exists
    // MODIFIES: this
    // EFFECTS: loads variableOfInterest into a list
    private List<Double> loadData(String fileName, String variableOfInterest) {
        List<Double> quantitativeValues = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            headerLine = br.readLine(); // Read the first line (header)
            if (headerLine.isEmpty()) {
                return quantitativeValues; // Empty file check
            }

            String[] headers = headerLine.split(",");
            int columnIndex = -1;

            // Find the column index for the variable of interest
            columnIndex = findVariableOfInterest(variableOfInterest, headers, columnIndex);

            readDataColumns(quantitativeValues, br, columnIndex);
        } catch (IOException | NonExistentColumnException e) {
            e.printStackTrace();
        }

        return quantitativeValues;
    }

    // REQUIRES: inputted csv is not empty
    // EFFECTS: finds the column in a csv that corresponds to the variable of
    // interest
    private int findVariableOfInterest(String variableOfInterest, String[] headers, int columnIndex)
            throws NonExistentColumnException {

        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(variableOfInterest)) {
                columnIndex = i;
                break;
            }
        }
        if (columnIndex == -1) {

            throw new NonExistentColumnException();
        }

        return columnIndex;
    }

    // REQUIRES: inputted csv is not empty
    // EFFECTS: reads column values in csv
    private void readDataColumns(List<Double> quantitativeValues, BufferedReader br, int columnIndex)
            throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            if (columnIndex < values.length) {
                String value = values[columnIndex].trim();
                try {
                    double num = Double.parseDouble(value);
                    quantitativeValues.add(num);
                } catch (NumberFormatException e) {
                    // skip non-numaric
                }
            }
        }
    }

    // getters & setters

    public String getFileName() {
        return fileName;
    }

    public String getHeaderLine() {
        return headerLine;
    }

    public String getVariableOfInterest() {
        return variableOfInterest;
    }

    public List<Double> getCurrentData() {
        return currentData;
    }

    public void setVariableOfInterest(String variableOfInterest) {
        this.variableOfInterest = variableOfInterest;
    }

}
