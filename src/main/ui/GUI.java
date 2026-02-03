package ui;

import javax.swing.*;

import model.Event;
import model.EventLog;

import java.awt.event.*;

import ui.tabs.DataTab;
import ui.tabs.HomeTab;

public class GUI extends JFrame {
    public static final int HOME_TAB_INDEX = 0;
    public static final int DATA_TAB_INDEX = 1;

    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;
    private JTabbedPane sidebar;

    public static void main(String[] args) {
        new GUI();
    }

    // MODIFIES: this
    // EFFECTS: creates GUI
    GUI() {
        super("Normality Test");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        sidebar = new JTabbedPane();
        sidebar.setTabPlacement(JTabbedPane.LEFT);

        loadTabs();
        add(sidebar);
        setVisible(true);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
                System.out.println("Application closed");
            }

        });

    }

    private void onClose() {
        EventLog eventLog = EventLog.getInstance(); 
        for (Event event : eventLog) {
            System.out.println(event);
        }

    }

    // MODIFIES: this
    // EFFECTS: adds home tab and data tab to UI
    private void loadTabs() {
        JPanel homeTab = new HomeTab(this);
        JPanel dataTab = new DataTab(this);

        sidebar.add(homeTab, HOME_TAB_INDEX);
        sidebar.setTitleAt(HOME_TAB_INDEX, "Home");
        sidebar.add(dataTab, DATA_TAB_INDEX);
        sidebar.setTitleAt(DATA_TAB_INDEX, "Data");
    }

    // EFFECTS: returns sidebar of this UI
    public JTabbedPane getTabbedPane() {
        return sidebar;
    }

}
