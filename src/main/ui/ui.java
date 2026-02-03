/* 

package ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import model.Dataset;
import model.AndersonDarling;
import model.DataCollection;
import model.SummaryStatistics;
import persistence.JsonReader;
import persistence.JsonWriter;
import model.ReadFile;

// Handles user interaction
public class UI {

    private static final String JSON_STORE = "./data/datacollection.json";
    private static String dataString;
    private static Dataset dataset;
    private static BufferedReader reader;
    private static DataCollection dataCollection;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Boolean saved = false;

    public UI() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        try {
            runUI();
        } catch (IOException e) {
            System.out.println("Error with UI");
        }
    }

    // MODIFIES: reader, dataCollection
    // EFFECTS: Stages UI for functionality
    private void runUI() throws IOException {

        reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to my project!");
        System.out.println("The purpose of this program is to test for normality");
        System.out.println("and perform statistical analysis on a dataset.");
        System.out.println("Press 1 to make a new data collection");
        System.out.println("Press 2 to load a data collection");
        String option = reader.readLine();
        if (option.equals("1")) {
            System.out.println("Name the data collection");
            String name = reader.readLine();
            dataCollection = new DataCollection(name);
            addData();
            showMenu();
        } else {
            loadDataCollection();
        }

    }

    // EFFECTS: menu for selection
    private void showMenu() throws IOException {
        String input = "";
        while (!input.equals("q")) {
            System.out.println("\nMenu:");
            System.out.println("1: Anderson-Darling normality test");
            System.out.println("2: Compute summary statistics");
            System.out.println("3: Add dataset");
            System.out.println("4: Select dataset");
            System.out.println("5: Save data collection");
            System.out.println("6: Load data collection");
            System.out.println("q: Quit");
            System.out.print("Select an option: ");
            input = reader.readLine();

            handleInput(input);
        }
    }

    // EFFECTS: handles input from user menu
    private void handleInput(String input) throws IOException {
        switch (input) {
            case "1" -> System.out.println(computeAndersonDarling(dataset));
            case "2" -> computeSummaryStatistics(dataset);
            case "3" -> addData();
            case "4" -> dataDirectory();
            case "5" -> saveDataCollection();
            case "6" -> loadDataCollection();
            case "q" -> extracted();
            default -> System.out.println("Invalid input. Please try again.");
        }
    }

    // EFFECTS: ensures user has had the option to save data
    private void extracted() throws IOException {
        if (saved) {
            System.out.println("Exiting...");
        } else {
            saveData();
            System.out.println("Exiting...");
        }
    }

    // EFFECTS: gives user the option to save data
    private void saveData() throws IOException {
        System.out.println("Data is not saved");
        System.out.println("Would you like to save your data? Y/N:");
        String input = reader.readLine();
        if (input.equals("Y")) {
            saveDataCollection();
        } else {
            System.out.println("Your data has not been saved");
        }
    }

    // MODIFIES: dataset, dataCollection
    // EFFECTS: adds data to data collection
    private void addData() throws IOException {
        System.out.print("Enter dataset: ");
        dataString = reader.readLine();
        System.out.print("Enter dataset name: ");
        String name = reader.readLine();
        System.out.print("Enter variable of interest: ");
        String variable = reader.readLine();
        ReadFile dataSet = new ReadFile(dataString, variable);
        dataset = new Dataset(name, dataSet.getCurrentData());
        dataCollection.addDataset(dataset);
    }

    // EFFECTS: runs anderson darling test on current dataset
    public static String computeAndersonDarling(Dataset data) {
        AndersonDarling dataSet = new AndersonDarling(data);
        if (dataSet.isNormal()) {
            return "Data follows a normal model.";
        } else {
            return "Data does not follow a normal model.";
        }
    }

    // EFFECTS: computs summary statistics for current dataset
    private void computeSummaryStatistics(Dataset data) {
        SummaryStatistics stats = new SummaryStatistics(data);
        System.out.println("Mean: " + stats.calculateMean());
        System.out.println("Median: " + stats.median());
        System.out.println("Standard Deviation: " + stats.calculateStandardDeviation());
        System.out.println("IQR: " + stats.iqr());
        System.out.println("Range: " + (stats.min() - stats.max()));
    }

    // MODIFIES: json object, saved
    // EFFECTS: saves json object
    private void saveDataCollection() {
        try {
            jsonWriter.open();
            jsonWriter.write(dataCollection);
            jsonWriter.close();
            System.out.println("Saved data to " + JSON_STORE);
            saved = true;
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
            saved = false;
        }
    }

    // MODIFIES: dataCollection
    // EFFECTS: loads json object
    private void loadDataCollection() {
        try {
            dataCollection = jsonReader.read();
            System.out.println("Loaded data from " + JSON_STORE);
            dataDirectory();
            showMenu();
        } catch (IOException e) {
            System.out.println("Unable to read from file.");
        }
    }

    // MODIFIES: dataset
    // EFFECTS: loads data collection directory
    private void dataDirectory() throws IOException {
        List<String> names = dataCollection.getNames();
        for (int i = 0; i < names.size(); i++) {
            System.out.println((i + 1) + ": " + names.get(i));
        }
        System.out.println("Current data collection: " + dataCollection.getName());
        System.out.println("Enter the name from the above datasets");
        String name = reader.readLine();
        dataset = dataCollection.getDataset(name);

    }
}

*/