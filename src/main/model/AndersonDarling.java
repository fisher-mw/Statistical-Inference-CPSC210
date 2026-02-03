package model;

import org.apache.commons.math3.distribution.NormalDistribution;

// preforms an anderson darling test for normality

public class AndersonDarling {

    private Dataset dataset;
    private double standardDeviation;
    private double mean;
    private double significance;

    public AndersonDarling(Dataset dataset) {
        this.dataset = dataset;
        mean = 0.0;
        standardDeviation = 0.0;
        significance = 0.787; // 5% significance (critical value for ADT)
    }

    // REQUIRES: dataset cannot be empty
    // EFFECTS: Returns A^2, the result of the Anderson-Darling test on the data
    public double andersonDarlingTestStat() {
        if (dataset.getData().isEmpty()) {
            throw new IllegalArgumentException("Data set cannot be empty.");
        } 

        sortAscending();
        standardize();
        int n = dataset.getData().size();
        double sum = 0.0;
        NormalDistribution normal = new NormalDistribution(0, 1);

        // Compute Anderson-Darling statistic
        for (int i = 0; i < n; i++) {
            double index = normal.cumulativeProbability(dataset.getData().get(i)); // F(X(i))
            double indexMinusOne = normal.cumulativeProbability(dataset.getData().get(n - 1 - i)); // F(X(n+1-i))

            // fixes division by zero
            if (index == 0) {
                index = 1e-10;
            }
            if (indexMinusOne == 1) {
                indexMinusOne = 1 - 1e-10;
            }

            sum += (2.0 * (i + 1) - 1) * (Math.log(index) + Math.log(1 - indexMinusOne));
        }

        double result = -n - sum / n;
        // adjusts statistic for normality
        result = result * (1 + (0.75 / n) + (2.25 / (n * n)));
        return result;
    }

    // REQUIRES: dataset is not empty
    // MODIFIES: this
    // EFFECTS: Sorts the dataset in ascending order
    public void sortAscending() {
        int n = dataset.getData().size();
        for (int i = 1; i < n; ++i) {
            Double key = dataset.getData().get(i);
            int j = i - 1;
            while (j >= 0 && dataset.getData().get(j) > key) {
                dataset.getData().set(j + 1, dataset.getData().get(j));
                j = j - 1;
            }
            dataset.getData().set(j + 1, key);
        }
    }

    // REQUIRES: dataset is not empty
    // MODIFIES: dataset
    // EFFECTS: transforms that data to z-scores
    public void standardize() {
        SummaryStatistics stats = new SummaryStatistics(dataset);
        mean = stats.calculateMean();
        standardDeviation = stats.calculateStandardDeviation();
        if (standardDeviation == 0) {
            throw new ArithmeticException("Standard deviation is zero; cannot standardize.");
        }
        for (int i = 0; i < dataset.getData().size(); i++) {
            dataset.getData().set(i, (dataset.getData().get(i) - mean) / standardDeviation);
        }
    }

    // EFFECTS: returns true if data follows a normal model
    public boolean isNormal() {
        double adjusted = andersonDarlingTestStat();
        return (adjusted < significance);
    }

    // Getter for dataset data
    public Dataset getDataset() {
        return dataset;
    }
}
