package persistence;

import model.DataCollection;
import org.json.JSONObject;

import java.io.*;

// CREDIT: CPSC210 Json Serialization Demo
// Represents a writer that writes JSON representation of DataCollection to a file
public class JsonWriter {
    private static final int INDENTATION = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file
    // cannot be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of DataCollection to file
    public void write(DataCollection dc) {
        JSONObject json = dc.toJson();
        saveToFile(json.toString(INDENTATION));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
