package ui.tabs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.List;
import javax.swing.*;
import model.AndersonDarling;
import model.DataCollection;
import model.Dataset;
import model.ReadFile;
import model.SummaryStatistics;
import persistence.JsonWriter;
import ui.ButtonNames;
import ui.GUI;

public class DataTab extends Tab implements ActionListener {

    private static final String JSON_STORE = "./data/datacollection.json";
    private JsonWriter jsonWriter;
    private JLabel dataName;
    private Dataset dataSet;
    private JTextField textField;
    private JTextField variableField;
    private JButton submitButton;
    private JLabel displayLabel;
    private DataCollection dataCollection;
    private String dataCollectionName = "Untitled Collection";
    private Boolean saved = true; // Initialize as true since nothing has changed yet
    private JComboBox<String> datasetDropdown;
    private JPanel dropdownPanel;

    public DataTab(GUI controller) {
        super(controller);
        jsonWriter = new JsonWriter(JSON_STORE);

        setLayout(new BorderLayout());
        placeDataCollectionName();
        createDatasetDropdown();
        setTextBox();
        createButtonPanel();
    }

    // EFFECTS: instatiates buttons required to preform compuatiotions on the
    // dataset
    private void createButtonPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JButton saveButton = new JButton(ButtonNames.SAVE.getValue());
        saveButton.addActionListener(e -> {
            saveDataCollection();
            JOptionPane.showMessageDialog(this, "Data Collection Saved!");
        });

        JButton clearButton = new JButton("Clear Data");
        clearButton.addActionListener(e -> clearDataCollection());

        JButton andersonDarlingButton = new JButton("Anderson-Darling Test");
        JButton summaryStatsButton = new JButton("Summary Statistics");

        andersonDarlingButton.addActionListener(e -> computeAndersonDarling());
        summaryStatsButton.addActionListener(e -> computeSummaryStatistics());

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        savePanel.add(saveButton);
        savePanel.add(clearButton);

        JPanel testButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        testButtonPanel.add(andersonDarlingButton);
        testButtonPanel.add(summaryStatsButton);

        bottomPanel.add(savePanel, BorderLayout.WEST);
        bottomPanel.add(testButtonPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: data collection
    // EFFECTS: clears the loaded data collection
    private void clearDataCollection() {
        if (dataCollection != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to clear all data?",
                    "Clear Data Collection", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Create a new empty data collection with the same name
                String currentName = dataCollection.getName();
                dataCollection = new DataCollection(currentName);
                dataSet = null;

                // Update UI components
                updateDatasetDropdown();
                dropdownPanel.setVisible(false);

                // Set saved to false because we've changed the data collection
                saved = false;

                JOptionPane.showMessageDialog(this, "Data collection cleared.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No data collection to clear.");
        }
    }

    // EFFECTS: instantiates data collection name
    private void placeDataCollectionName() {
        dataName = new JLabel("Current Data Collection: " + dataCollectionName);
        dataName.setFont(new Font("Serif", Font.BOLD, 18));
        dataName.setHorizontalAlignment(JLabel.LEFT);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(dataName);
        this.add(topPanel, BorderLayout.NORTH);
    }

    // EFFECTS: instantiates dropdown pannel
    private void createDatasetDropdown() {
        dropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel dropdownLabel = new JLabel("Select Dataset: ");
        dropdownLabel.setFont(new Font("Serif", Font.BOLD, 14));

        datasetDropdown = new JComboBox<>();
        datasetDropdown.setPreferredSize(new Dimension(200, 25));
        datasetDropdown.addActionListener(e -> {
            String selected = (String) datasetDropdown.getSelectedItem();
            if (selected != null && dataCollection != null) {
                dataSet = dataCollection.getDataset(selected);
                if (e.getSource() == datasetDropdown) {
                    JOptionPane.showMessageDialog(this, "Dataset " + selected + " selected.");
                }
            }
        });

        dropdownPanel.add(dropdownLabel);
        dropdownPanel.add(datasetDropdown);
        dropdownPanel.setVisible(false);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(dataName, BorderLayout.NORTH);
        topContainer.add(dropdownPanel, BorderLayout.CENTER);

        this.add(topContainer, BorderLayout.NORTH);
    }

    // EFFECTS: instantiates text box
    private void setTextBox() {
        Font font = new Font("Monospace", Font.BOLD, 20);
        displayLabel = new JLabel("Enter dataset file name:");
        displayLabel.setFont(font);
        textField = new JTextField(16);
        textField.setFont(font);

        JLabel variableLabel = new JLabel("Enter variable of interest:");
        variableLabel.setFont(font);
        variableField = new JTextField(10);
        variableField.setFont(font);

        submitButton = new JButton("Add Dataset");
        submitButton.addActionListener(this);

        JPanel panel = new JPanel();
        panel.add(displayLabel);
        panel.add(textField);
        panel.add(variableLabel);
        panel.add(variableField);
        panel.add(submitButton);

        this.add(panel, BorderLayout.CENTER);
    }

    // MODIFES: texField, variableField, dataCollection
    // EFFECTS: resets text box and adds new data collection
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String datasetName = textField.getText();
            String variableOfInterest = variableField.getText();

            if (dataCollection == null) {
                dataCollection = new DataCollection(dataCollectionName);
                dataName.setText("Current Data Collection: " + dataCollectionName);
            }

            readCSV(datasetName, variableOfInterest);
            updateDatasetDropdown();

            saved = false;

            textField.setText("");
            variableField.setText("");
        }
    }

    // EFFECTS: updates dataset dropdown menu when a new dataset is added
    private void updateDatasetDropdown() {
        if (dataCollection != null) {
            List<String> datasetNames = dataCollection.getNames();

            datasetDropdown.removeAllItems();
            for (String name : datasetNames) {
                datasetDropdown.addItem(name);
            }

            if (!datasetNames.isEmpty()) {
                dropdownPanel.setVisible(true);
                if (dataSet != null) {
                    datasetDropdown.setSelectedItem(dataSet.getName());
                }
            } else {
                dropdownPanel.setVisible(false);
            }
        }
    }

    // EFFECTS: saves data collection to json file
    private void saveDataCollection() {
        try {
            jsonWriter.open();
            jsonWriter.write(dataCollection);
            jsonWriter.close();
            saved = true;
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
            saved = false;
        }
    }

    // EFFECTS: Reads passed in CSV
    private void readCSV(String name, String variableOfInterest) {
        try {
            ReadFile read = new ReadFile(name, variableOfInterest);
            dataSet = new Dataset(name, read.getCurrentData());
            dataCollection.addDataset(dataSet);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage());
        }
    }

    // EFFECTS: ensures dataset is selected for AD computation
    private void computeAndersonDarling() {
        if (dataSet != null) {
            String result = computeAndersonDarlingTest(dataSet);
            JOptionPane.showMessageDialog(this, result);
        } else {
            JOptionPane.showMessageDialog(this, "No dataset selected.");
        }
    }

    // EFFECTS: displays result of anderson darling test
    public static String computeAndersonDarlingTest(Dataset data) {
        AndersonDarling dataSetTest = new AndersonDarling(data);
        if (dataSetTest.isNormal()) {
            return "Data follows a normal distribution.";
        } else {
            return "Data does not follow a normal distribution.";
        }
    }

    // EFFECTS: displays summary statistics
    private void computeSummaryStatistics() {
        if (dataSet != null) {
            SummaryStatistics stats = new SummaryStatistics(dataSet);
            String statsResult = "Mean: " + stats.calculateMean()
                    +
                    "\nMedian: " + stats.median()
                    +
                    "\nStandard Deviation: "
                    + stats.calculateStandardDeviation()
                    +
                    "\nIQR: " + stats.iqr()
                    +
                    "\nRange: " + (stats.max() - stats.min());

            JOptionPane.showMessageDialog(this, statsResult);
        } else {
            JOptionPane.showMessageDialog(this, "No dataset selected.");
        }
    }

    // EFFECTS: Loads data collection to data tab
    public void setDataCollection(DataCollection newDataCollection) {
        if (newDataCollection != null) {
            this.dataCollection = newDataCollection;
            this.dataCollectionName = dataCollection.getName();
            dataName.setText("Current Data Collection: " + dataCollectionName);
            updateDatasetDropdown();
            saved = true;
        }
    }

    // EFFECTS: Asked user if they want to save data
    public boolean checkSaveBeforeExit() {
        if (!saved && dataCollection != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "You have unsaved changes. Save before exiting?",
                    "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                saveDataCollection();
                return true;
            } else if (confirm == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        return true;
    }
}