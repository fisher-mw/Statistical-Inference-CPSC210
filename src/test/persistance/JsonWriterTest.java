package persistance;

import model.DataCollection;
import model.Dataset;
import persistence.JsonReader;
import persistence.JsonWriter;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// CREDIT: JsonSerializationDemo supplied by CPSC210 team

class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            DataCollection dc = new DataCollection("My Data Collection");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("FileNotFoundException expected");
        } catch (FileNotFoundException e) {
            // Pass: exception was expected due to an illegal file name.
        }
    }

    @Test
    void testWriterEmptyDataCollection() {
        try {
            DataCollection dc = new DataCollection("test");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyDataCollection.json");
            writer.open();
            writer.write(dc);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyDataCollection.json");
            dc = reader.read();
            assertEquals("test", dc.getName());
            assertEquals(0, dc.getDatasets().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralDataCollection() {
        try {
            DataCollection dc = new DataCollection("test");
            dc.addDataset(new Dataset("Dataset1", List.of(1.1, 2.2, 3.3)));
            dc.addDataset(new Dataset("Dataset2", List.of(4.4, 5.5, 6.6)));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralDataCollection.json");
            writer.open();
            writer.write(dc);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralDataCollection.json");
            dc = reader.read();
            assertEquals("test", dc.getName());
            List<Dataset> datasets = dc.getDatasets();
            assertEquals(2, datasets.size());
            checkDataset("Dataset1", datasets.get(0));
            checkDataset("Dataset2", datasets.get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
