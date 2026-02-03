package model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestReadFile {
    String test1;
    String test2;
    String testEmpty;
    String testBadData;
    ReadFile emptyTest;
    ReadFile nonEmptyTest;
    ReadFile notFound; 
    ReadFile badData;


    @BeforeEach
    void runBefore() {
        test1 = "data/test1.csv";
        test2 = "data/test2.csv";
        testEmpty = "data/testEmpty.csv";
        testBadData = "data/bad_data.csv";
        emptyTest = new ReadFile(testEmpty, "price");
        nonEmptyTest = new ReadFile(test1, "price");
        notFound = new ReadFile(test2, "DNE");
        badData = new ReadFile("data/bad_data.csv", "name");


    }

    @Test
    void testEmptyCSV() {
        assertTrue(emptyTest.getCurrentData().isEmpty());
        assertTrue(emptyTest.getHeaderLine().isEmpty());
    }



    @Test
    void testNonEmptyCSV() {
        assertFalse(nonEmptyTest.getCurrentData().isEmpty());

    }

    @Test
    void testVariableNotFound() {
        assertTrue(notFound.getCurrentData().isEmpty());

    }

    // csv has no quantitative variable 
    @Test
    void testNonNumericData() {
        assertTrue(badData.getCurrentData().isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        ArrayList<Double> test1 = new ArrayList<Double>();
        test1.add(500.0);
        test1.add(1000.0);
        test1.add(50.0);
        assertEquals(nonEmptyTest.getFileName(), "data/test1.csv");
        assertEquals(nonEmptyTest.getVariableOfInterest(), "price");
        assertEquals(nonEmptyTest.getCurrentData(), test1);
        nonEmptyTest.setVariableOfInterest("test");
        assertEquals(nonEmptyTest.getVariableOfInterest(), "test");
    }


}
