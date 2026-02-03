package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestAndersonDarling {
    private AndersonDarling largeNormal1;
    private AndersonDarling largeNormal2;
    private AndersonDarling exponential;
    private AndersonDarling uniform;
    private AndersonDarling bimodal;
    private AndersonDarling sortData;
    private AndersonDarling emptyData;

    @BeforeEach
    void runBefore() {
        ReadFile large1 = new ReadFile("data/large_normal.csv", "price");
        ReadFile large2 = new ReadFile("data/large_normal1.csv", "price");
        ReadFile expo = new ReadFile("data/exponential_non_normal.csv", "price");
        ReadFile uni = new ReadFile("data/uniform_non_normal.csv", "price");
        ReadFile bimod = new ReadFile("data/bimodal_non_normal.csv", "price");
        ReadFile fail = new ReadFile("data/test1.csv", "price");
        ReadFile empty = new ReadFile("data/testEmpty.csv", "price");

        Dataset dataset1 = new Dataset("large_normal1", large1.getCurrentData());
        Dataset dataset2 = new Dataset("large_normal2", large2.getCurrentData());
        Dataset dataset3 = new Dataset("exponential", expo.getCurrentData());
        Dataset dataset4 = new Dataset("uniform", uni.getCurrentData());
        Dataset dataset5 = new Dataset("bimodal", bimod.getCurrentData());
        Dataset datasetFail = new Dataset("fail", fail.getCurrentData());
        Dataset datasetEmpty = new Dataset("empty", empty.getCurrentData());

        largeNormal1 = new AndersonDarling(dataset1);
        largeNormal2 = new AndersonDarling(dataset2);
        exponential = new AndersonDarling(dataset3);
        uniform = new AndersonDarling(dataset4);
        bimodal = new AndersonDarling(dataset5);
        sortData = new AndersonDarling(datasetFail);
        emptyData = new AndersonDarling(datasetEmpty);
    }

    @Test
    public void testIsNormalLargeDataSet() {
        assertTrue(largeNormal1.isNormal());
        assertTrue(largeNormal2.isNormal());
        assertFalse(exponential.isNormal());
        assertFalse(uniform.isNormal());
        assertFalse(bimodal.isNormal());
        try {
            assertFalse(emptyData.isNormal());
            fail();
        } catch (Exception e) {
            // caught expected exception for empty dataset
        }
    }

    @Test
    public void testZeroSD() {
        List<Double> zeroSD = Arrays.asList(0.5, -0.1, 0.3, -0.2, 0.0, 0.1, -0.3, 0.4, -0.5, 0.2);
        Dataset dataset = new Dataset("zeroSD", zeroSD);
        AndersonDarling adTest = new AndersonDarling(dataset);
        try {
            adTest.isNormal();
            fail();
        } catch (ArithmeticException e) {
            // passed
        }
    }

    @Test
    public void testSmallDataSet() {
        List<Double> smallData = Arrays.asList(1.2, 2.3, 3.1, 2.9, 1.7, 2.1, 2.8, 3.0, 1.5, 2.5);
        Dataset dataset = new Dataset("smallData", smallData);
        AndersonDarling adTest = new AndersonDarling(dataset);
        assertFalse(adTest.isNormal());
    }

    @Test
    void testSmallBimodalDataset() {
        List<Double> bimodalData = Arrays.asList(-3.0, -2.8, -2.5, -2.3, 2.3, 2.5, 2.8, 3.0);
        Dataset dataset = new Dataset("bimodalData", bimodalData);
        AndersonDarling adTest = new AndersonDarling(dataset);
        assertFalse(adTest.isNormal());
    }

    @Test
    public void testSortAscending() {
        List<Double> sorted = Arrays.asList(50.0, 500.0, 1000.0);
        Dataset dataset = new Dataset("sortedData", sorted);
        AndersonDarling adTest = new AndersonDarling(dataset);
        adTest.sortAscending();
        assertEquals(sorted, adTest.getDataset().getData());
    }

    @Test
    public void testStandardization() {
        List<Double> standardized = Arrays.asList(-0.9810526315789474, -0.03368421052631579, 1.0189473684210526);
        Dataset dataset = new Dataset("standardizedData", standardized);
        AndersonDarling adTest = new AndersonDarling(dataset);
        adTest.standardize();
        assertEquals(standardized, adTest.getDataset().getData());
    }
}
