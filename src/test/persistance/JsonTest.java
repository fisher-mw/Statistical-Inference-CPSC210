package persistance;

import model.Dataset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// CREDIT: JsonSerializationDemo supplied by CPSC210 team

public class JsonTest {
    protected void checkDataset(String expectedName, Dataset dataset) {
        assertEquals(expectedName, dataset.getName());
        assertNotNull(dataset.getData());
    }

}