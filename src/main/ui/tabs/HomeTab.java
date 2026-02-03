package ui.tabs;

import javax.swing.*;
import ui.ButtonNames;
import ui.GUI;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import model.DataCollection;
import model.Dataset;
import persistence.JsonReader;

public class HomeTab extends Tab {

    private JLabel greeting;
    private static final String INIT_GREETING = "Welcome to Normality Tester";
    private static final String JSON_STORE = "./data/datacollection.json";
    private DataCollection dataCollection;
    private JsonReader jsonReader;
    private JList<String> datasetList;
    private DefaultListModel<String> listModel;
    private JPanel dataPanel;
    private JTextField collectionNameField;

    // Path to the image file
    private static final String IMAGE_PATH = "lib\\bayesian-2889576_1280.png";

    // EFFECTS: constructs a home tab with an image and buttons
    public HomeTab(GUI controller) {
        super(controller);
        jsonReader = new JsonReader(JSON_STORE);
        setLayout(new BorderLayout());
        File file = new File(IMAGE_PATH);
        if (!file.exists()) {
            System.out.println("File not found: " + IMAGE_PATH);
        }

        placeImage();
        placeGreeting();
        createCollectionNameInput();
        createDatasetPanel();
        placeLoadData();
    }

    // EFFECTS: adds an image to the top of the panel
    private void placeImage() {
        ImageIcon icon = new ImageIcon(IMAGE_PATH);
        Image temp = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon img = new ImageIcon(temp);
        JLabel imageLabel = new JLabel(img, JLabel.CENTER);
        this.add(imageLabel, BorderLayout.NORTH);
    }

    // EFFECTS: creates greeting text below the image
    private void placeGreeting() {
        greeting = new JLabel(INIT_GREETING, JLabel.CENTER);
        greeting.setFont(new Font("Serif", Font.BOLD, 20));

        // We'll create a panel to hold both the greeting and collection name input
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(greeting, BorderLayout.NORTH);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    // EFFECTS: creates input field for collection name
    private void createCollectionNameInput() {
        // Create panel for the collection name input
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Add label and text field
        JLabel nameLabel = new JLabel("Data Collection Name:");
        nameLabel.setFont(new Font("Monospace", Font.PLAIN, 16));

        collectionNameField = new JTextField("Untitled Collection", 20);
        collectionNameField.setFont(new Font("Serif", Font.PLAIN, 16));

        // Create button to create new collection
        JButton createButton = new JButton("Create New Collection");
        createButton.addActionListener(e -> {
            String name = collectionNameField.getText().trim();
            if (!name.isEmpty()) {
                dataCollection = new DataCollection(name);
                greeting.setText("Created new collection: " + name);
                updateDatasetList();
                dataPanel.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a name for the collection");
            }
        });

        // Add components to the panel
        namePanel.add(nameLabel);
        namePanel.add(collectionNameField);
        namePanel.add(createButton);

        // Add to the center panel (below greeting)
        ((JPanel) this.getComponent(this.getComponentCount() - 1)).add(namePanel, BorderLayout.CENTER);
    }

    // EFFECTS: creates a panel to display loaded datasets
    private void createDatasetPanel() {
        // Create panel with border and title
        dataPanel = new JPanel(new BorderLayout());
        dataPanel.setBorder(BorderFactory.createTitledBorder("Available Datasets"));

        // Create list model and JList
        listModel = new DefaultListModel<>();
        datasetList = new JList<>(listModel);
        datasetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        datasetList.setVisibleRowCount(5);

        // Add JList to a scroll pane
        JScrollPane scrollPane = new JScrollPane(datasetList);
        dataPanel.add(scrollPane, BorderLayout.CENTER);

        // Add info panel
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JLabel collectionLabel = new JLabel("No collection loaded");
        JLabel countLabel = new JLabel("Datasets: 0");

        infoPanel.add(collectionLabel);
        infoPanel.add(countLabel);
        dataPanel.add(infoPanel, BorderLayout.NORTH);

        // Initially hide the panel until data is loaded
        dataPanel.setVisible(false);

        // Add to main panel
        this.add(dataPanel, BorderLayout.EAST);
    }

    // EFFECTS: adds buttons to the bottom of the panel
    private void placeLoadData() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton loadButton = getLoadButton();
        JButton clearButton = getClearButton();
        JButton renameButton = getRenameButton();
        JButton viewButton = getViewButton();
        JButton switchButton = getSwitchButton();

        buttonPanel.add(loadButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(renameButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(switchButton);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    // EFFECTS: Clear data button
    private JButton getClearButton() {
        JButton clearButton = new JButton("Clear Data");
        clearButton.addActionListener(e -> {
            if (dataCollection != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to clear all data? This cannot be undone.",
                        "Clear Data Collection", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Create a new empty data collection with the same name
                    String currentName = dataCollection.getName();
                    dataCollection = new DataCollection(currentName);

                    // Update UI
                    updateDatasetList();
                    greeting.setText("Collection cleared: " + currentName);

                    // Pass the empty collection to the DataTab
                    passDataCollectionToDataTab();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No collection is currently active");
            }
        });
        return clearButton;
    }

    // EFFECTS: Switch to Data Tab button
    private JButton getSwitchButton() {
        JButton switchButton = new JButton("Switch to Data Tab");
        switchButton.addActionListener(e -> {
            if (dataCollection != null) {
                passDataCollectionToDataTab();
                controller.getTabbedPane().setSelectedIndex(1);
            } else {
                JOptionPane.showMessageDialog(this, "Please create or load a data collection first");
            }
        });
        return switchButton;
    }

    // EFFECTS: View data dietails button
    private JButton getViewButton() {
        JButton viewButton = new JButton("View Dataset Details");
        viewButton.addActionListener(e -> {
            String selected = datasetList.getSelectedValue();
            if (selected != null && dataCollection != null) {
                Dataset dataset = dataCollection.getDataset(selected);
                if (dataset != null) {
                    showDatasetDetails(dataset);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a dataset first");
            }
        });
        return viewButton;
    }

    // EFFECTS: instantiates rename button
    private JButton getRenameButton() {
        JButton renameButton = new JButton("Rename Collection");
        renameButton.addActionListener(e -> {
            if (dataCollection != null) {
                String newName = collectionNameField.getText().trim();
                if (!newName.isEmpty()) {
                    dataCollection.setName(newName);
                    updateDatasetList();
                    greeting.setText("Collection renamed to: " + newName);

                    // Pass the renamed collection to the DataTab to update the name there
                    passDataCollectionToDataTab();
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a name for the collection");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No collection is currently active");
            }
        });
        return renameButton;
    }

    // EFFECTS: instantaites load button
    private JButton getLoadButton() {
        JButton loadButton = new JButton(ButtonNames.LOAD.getValue());
        loadButton.addActionListener(e -> {
            loadDataCollection();
            if (dataCollection != null) {
                updateDatasetList();
                greeting.setText("Data Loaded Successfully: " + dataCollection.getName());
                // Update the collection name field with the loaded name
                collectionNameField.setText(dataCollection.getName());
                dataPanel.setVisible(true);

                // Pass the data collection to the DataTab immediately after loading
                passDataCollectionToDataTab();
            } else {
                greeting.setText("Failed to Load Data");
            }
        });
        return loadButton;
    }

    // MODIFIES: dataCollection
    // EFFECTS: loads json object and handles errors
    private void loadDataCollection() {
        try {
            dataCollection = jsonReader.read();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to read from file. Please check the data.");
        }
    }

    // EFFECTS: updates the dataset list with available datasets
    private void updateDatasetList() {
        if (dataCollection != null) {
            JPanel infoPanel = (JPanel) dataPanel.getComponent(1);
            ((JLabel) infoPanel.getComponent(0)).setText("Collection: " + dataCollection.getName());

            List<String> datasetNames = dataCollection.getNames();
            ((JLabel) infoPanel.getComponent(1)).setText("Datasets: " + datasetNames.size());

            listModel.clear();
            for (String name : datasetNames) {
                listModel.addElement(name);
            }

            // Select first dataset if available
            if (!datasetNames.isEmpty()) {
                datasetList.setSelectedIndex(0);
            }
        }
    }

    // EFFECTS: show details for the selected dataset
    private void showDatasetDetails(Dataset dataset) {
        if (dataset != null) {
            // Create a formatted message with dataset details
            StringBuilder details = new StringBuilder();
            details.append("Dataset: ").append(dataset.getName()).append("\n\n");
            details.append("Number of data points: ").append(dataset.getData().size()).append("\n");
            int sampleSize = Math.min(5, dataset.getData().size());
            if (sampleSize > 0) {
                details.append("\nSample values:\n");
                for (int i = 0; i < sampleSize; i++) {
                    details.append(dataset.getData().get(i)).append("\n");
                }
            }

            JOptionPane.showMessageDialog(this, details.toString(),
                    "Dataset Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // REQUIRES: DataTab is at index 1
    // EFFECTS: passes the loaded data collection to the DataTab
    private void passDataCollectionToDataTab() {
        DataTab dataTab = (DataTab) controller.getTabbedPane().getComponentAt(1);
        dataTab.setDataCollection(dataCollection);
    }

    // EFFECTS: getter method for the data collection
    public DataCollection getDataCollection() {
        return dataCollection;
    }
}