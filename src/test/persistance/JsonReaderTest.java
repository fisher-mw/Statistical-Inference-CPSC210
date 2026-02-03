package persistance;

import model.DataCollection;
import model.Dataset;
import persistence.JsonReader;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            DataCollection dc = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    void testReaderEmptyDataCollection() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyDataCollection.json");
        try {
            DataCollection dc = reader.read();
            // Assuming the JSON file contains a "name" field "My Data Collection" and no
            // datasets
            assertEquals("My Data Collection", dc.getName());
            assertEquals(0, dc.getDatasets().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralDataCollection() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralDataCollection.json");
        try {
            DataCollection dc = reader.read();
            // Assuming the JSON file contains a "name" field "My Data Collection"
            // and exactly two datasets
            assertEquals("My Data Collection", dc.getName());

            List<Dataset> datasets = dc.getDatasets();
            assertEquals(2, datasets.size());

            checkDataset("Dataset1", datasets.get(0));
            checkDataset("Dataset2", datasets.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
