package model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestDataCollection {

    private DataCollection testDC;
    private Dataset test1;
    private Dataset test2;
    private Dataset test3;
    private Dataset test4;

    @BeforeEach
    public void setUp() {
        testDC = new DataCollection("test1");

        ReadFile large1 = new ReadFile("data/large_normal.csv", "price");
        ReadFile expo = new ReadFile("data/exponential_non_normal.csv", "price");
        ReadFile uni = new ReadFile("data/uniform_non_normal.csv", "price");
        ReadFile bimod = new ReadFile("data/bimodal_non_normal.csv", "price");

        test1 = new Dataset("normal data", large1.getCurrentData());
        test2 = new Dataset("uniform", uni.getCurrentData());
        test3 = new Dataset("exponential", expo.getCurrentData());
        test4 = new Dataset("bimodal", bimod.getCurrentData());

        testDC.addDataset(test1);
        testDC.addDataset(test2);
        testDC.addDataset(test3);
        testDC.addDataset(test4);
    }

    @Test
    public void testAddAndFindDataSet() {
        assertEquals(test2, testDC.getDataset("uniform"));
        assertEquals(test4, testDC.getDataset("bimodal"));

    }

    @Test
    public void testLargestDataSet() {
        assertEquals(testDC.getLargestDataset(), test1);
    }

    @Test
    public void testGetNames() {
        List<String> names = Arrays.asList("normal data", "uniform", "exponential", "bimodal");
        assertEquals(testDC.getNames(), names);
    }

    @Test
    public void testNameNotFound() {
        assertEquals(testDC.getDataset("DNE"), null);
        test1.setName("DNE");
        assertEquals(testDC.getDataset("DNE"), test1);

    }
}
