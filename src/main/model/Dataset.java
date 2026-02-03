package model;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

import java.util.List;

// Represents a dataset, containing a list of quantitative data
public class Dataset implements Writable {
    private String name;
    private List<Double> data;

    public Dataset(String name, List<Double> data) {
        this.name = name;
        this.data = data;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public List<Double> getData() {
        return data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int size() {
        return data.size();
    }

    // EFFECTS: Converts Dataset object to JSON representation
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("data", dataToJson());
        return json;
    }

    // EFFECTS: Converts data list to a JSON array
    private JSONArray dataToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Double value : data) {
            jsonArray.put(value);
        }
        return jsonArray;
    }
}
