package model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// represents a collection of datasets 
public class DataCollection implements Writable {
    private List<Dataset> datasets;
    private String name;


    
    public DataCollection(String name) {
        this.datasets = new ArrayList<>();
        this.name = name;
    }

    // REQUIRES: there is at least one dataset in the collection
    // EFFECTS: returns the largest dataset in the collection
    public Dataset getLargestDataset() {
        int size = 0;
        Dataset largestDataset = new Dataset(null, null);
        for (Dataset dataset : datasets) {
            if (dataset.size() > size) {
                size = dataset.size();
                largestDataset = dataset;
            }
        }
        return largestDataset;
    }

    public List<Dataset> getDatasets() {
        return datasets;
    }

    public void addDataset(Dataset dataset) {
        EventLog.getInstance().logEvent(new Event("Added dataset to: " + name));
        datasets.add(dataset);
    }



    // REQUIRES: datasets is not empty
    // EFFECTS: returns the names of the datasets
    public List<String> getNames() {
        List<String> names = new ArrayList<String>();
        for (Dataset data : datasets) {
            names.add(data.getName());
        }
        return names;
    }

    // EFFECTS: gets a specific dataset in a collection of datasets
    public Dataset getDataset(String name) {
        for (Dataset dataset : datasets) {
            if (dataset.getName().equals(name)) {
                EventLog.getInstance().logEvent(new Event("Retrieved dataset: " + name));
                return dataset;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    // EFFECTS: Converts dataset to JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("datasets", datasetsToJson());
        return json;
    }

    // EFFECTS: returns datasets in this DataCollection as a JSON array
    private JSONArray datasetsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Dataset dataset : datasets) {
            jsonArray.put(dataset.toJson()); // Ensure Dataset has a toJson() method
        }

        return jsonArray;
    }

    public void setName(String newName) {
        name = newName;
    }

}