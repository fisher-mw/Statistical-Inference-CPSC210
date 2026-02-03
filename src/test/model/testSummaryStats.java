package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestSummaryStats {
    private SummaryStatistics largeNormal;
    private SummaryStatistics exponential;
    private SummaryStatistics uniform;
    private SummaryStatistics bimodal;
    private SummaryStatistics failData;
    private SummaryStatistics emptyData;

    @BeforeEach
    public void setUp() {
        ReadFile large1 = new ReadFile("data/large_normal.csv", "price");
        ReadFile expo = new ReadFile("data/exponential_non_normal.csv", "price");
        ReadFile uni = new ReadFile("data/uniform_non_normal.csv", "price");
        ReadFile bimod = new ReadFile("data/bimodal_non_normal.csv", "price");
        ReadFile fail = new ReadFile("data/test1.csv", "price");
        ReadFile empty = new ReadFile("data/testEmpty.csv", "price");

        Dataset dataset1 = new Dataset("large_normal1", large1.getCurrentData());
        Dataset dataset2 = new Dataset("exponential", expo.getCurrentData());
        Dataset dataset3 = new Dataset("uniform", uni.getCurrentData());
        Dataset dataset4 = new Dataset("bimodal", bimod.getCurrentData());
        Dataset datasetFail = new Dataset("fail", fail.getCurrentData());
        Dataset datasetEmpty = new Dataset("empty", empty.getCurrentData());

        largeNormal = new SummaryStatistics(dataset1);
        exponential = new SummaryStatistics(dataset2);
        uniform = new SummaryStatistics(dataset3);
        bimodal = new SummaryStatistics(dataset4);
        failData = new SummaryStatistics(datasetFail);
        emptyData = new SummaryStatistics(datasetEmpty);
    }

    @Test
    public void testMean() {
        assertEquals(largeNormal.calculateMean(), 0.0);
        assertEquals(bimodal.calculateMean(), 0);
    }

    @Test
    public void testStandardDeviation() {
        assertEquals(largeNormal.calculateStandardDeviation(), 1.0);
        assertEquals(bimodal.calculateStandardDeviation(), 2.0);
    }

    @Test
    public void testQuartile() {
        assertEquals(largeNormal.firstQuartile(), -0.68);
        assertEquals(largeNormal.thirdQuartile(), 0.68);
        assertEquals(largeNormal.iqr(), 1.36);
    }

    @Test
    public void testMinMax() {
        assertEquals(largeNormal.min(), -2.62);
        assertEquals(largeNormal.max(), 3.85);
        assertEquals(exponential.min(), 0.02);
        assertEquals(exponential.max(), 4.6);
        assertEquals(uniform.min(), 0.01);
        assertEquals(uniform.max(), 1.0);
    }

    @Test
    public void testMedian() {
        assertEquals(largeNormal.median(), 0.09);
        assertEquals(exponential.median(), 0.7);
        assertEquals(uniform.median(), 0.52);
    }

    @Test
    public void testRound() {
        assertEquals(largeNormal.round(0.01), 0.01);        
        assertEquals(largeNormal.round(0.666666), 0.67);
        assertEquals(largeNormal.round(-12.4892), -12.49);
        assertEquals(largeNormal.round(0.0), 0.0);
    }

    @Test
    public void testZeroSD() {
        assertEquals(emptyData.calculateStandardDeviation(), 0);
    }
}
