package ui;

public enum ButtonNames {
    LOAD("Load Data"),
    SAVE("Save Data");
 

    private final String name;

    ButtonNames(String name) {
        this.name = name;
    }

    //EFFECTS: returns name value of this button
    public String getValue() {
        return name;
    }
}
