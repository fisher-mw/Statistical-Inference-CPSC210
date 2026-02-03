package model;

// Computes summary statistics for the given data set, including but not limited to mean, median, and IQR

public class SummaryStatistics {
    private Dataset dataset;

    public SummaryStatistics(Dataset dataset) {
        this.dataset = dataset;
        AndersonDarling sample = new AndersonDarling(dataset);
        sample.sortAscending();
        this.dataset = sample.getDataset();
    }

    // REQUIRES: Data is not empty
    // MODIFIES: dataset
    // EFFECTS: calculates the average of data
    public double calculateMean() {
        int sum = 0;
        if (dataset.getData().size() == 0) {
            return sum;
        }
        for (double i : dataset.getData()) {
            sum += i;
        }
        return sum / dataset.getData().size();
    }

    // REQUIRES: Data is not empty
    // MODIFIES: dataset
    // EFFECTS: calculates the standard deviation of the data
    public double calculateStandardDeviation() {
        double sum = 0;
        double sampleMean = calculateMean();

        if (dataset.getData().size() == 0) {
            return sum;
        }
        for (double i : dataset.getData()) {
            sum += (i - sampleMean) * (i - sampleMean);
        }

        return Math.round(Math.sqrt(sum * 1 / (dataset.getData().size() - 1)));
    }

    // REQUIRES: data.size() != 0
    // EFFECTS: returns the first quartile of the dataset
    public double firstQuartile() {
        int firstQ = dataset.getData().size() / 4;
        return round(dataset.getData().get(firstQ));
    }

    // REQUIRES: data.size() != 0
    // EFFECTS: returns the third quartile of the dataset
    public double thirdQuartile() {
        int firstQ = dataset.getData().size() / 4;
        int lastQ = dataset.getData().size() - firstQ;
        return round(dataset.getData().get(lastQ));
    }

    // REQUIRES: data.size() != 0
    // EFFECTS: returns the interquartile range of the dataset
    public double iqr() {
        return round(thirdQuartile() - firstQuartile());
    }

    // REQUIRES: data.size() != 0
    // EFFECTS: returns the middle of the data
    public double median() {
        AndersonDarling sample = new AndersonDarling(dataset);
        int middle = dataset.getData().size() / 2;
        sample.getDataset(); // Get the sorted dataset
        return round(dataset.getData().get(middle));
    }

    // REQUIRES: data.size() != 0
    // EFFECTS: returns the smallest data point in the dataset
    public double min() {
        double min = dataset.getData().get(0);
        for (double i : dataset.getData()) {
            if (i <= min) {
                min = i;
            }
        }
        return round(min);
    }

    // REQUIRES: data.size() != 0
    // EFFECTS: returns the largest data point in the dataset
    public double max() {
        double max = dataset.getData().get(0);
        for (double i : dataset.getData()) {
            if (i >= max) {
                max = i;
            }
        }

        return round(max);
    }

    // EFFECTS: rounds to the nearest hundredth
    public double round(double d) {
        return Math.round(d * 100.0) / 100.0;
    }
}
