package persistence;

import model.DataCollection;
import model.Dataset;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// CREDIT: CPSC210 Json Serialization Demo
// Represents a reader that reads JSON representation of DataCollection from a file

public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads DataCollection from file and returns it;
    // throws IOException if an error occurs reading data from file
    public DataCollection read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseDataCollection(jsonObject);
    }

    // EFFECTS: reads source file as a string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses DataCollection from JSON object and returns it
    private DataCollection parseDataCollection(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        DataCollection dc = new DataCollection(name);
        addDatasets(dc, jsonObject);
        return dc;
    }

    // MODIFIES: dc
    // EFFECTS: parses datasets from JSON object and adds them to DataCollection
    private void addDatasets(DataCollection dc, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("datasets");
        for (Object json : jsonArray) {
            JSONObject nextDataset = (JSONObject) json;
            addDataset(dc, nextDataset);
        }
    }

    // MODIFIES: dc
    // EFFECTS: parses a dataset from JSON object and adds it to DataCollection
    private void addDataset(DataCollection dc, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        JSONArray dataArray = jsonObject.getJSONArray("data");
        List<Double> dataList = new ArrayList<>();
        for (Object num : dataArray) {
            dataList.add(((Number) num).doubleValue());
        }
        dc.addDataset(new Dataset(name, dataList));
    }
}
