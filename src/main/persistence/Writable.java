package persistence;

import org.json.JSONObject;

// CREDIT: CPSC210 Json Serialization Demo

public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
