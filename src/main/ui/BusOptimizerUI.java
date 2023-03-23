package ui;

import model.Event;
import model.EventLog;
import model.Route;
import model.Routes;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class BusOptimizerUI extends JPanel implements ListSelectionListener {
    private JList list;
    private DefaultListModel listModel;

    private static final String JSON_STORE = "./data/busData.json";
    private static final String addRouteString = "Add Route";
    private static final String removeRouteString = "Remove Route";
    private static final String saveDataString = "Save";
    private static final String loadDataString = "Load";
    ImageIcon icon = createImageIcon("thumb.png","a thumb up");


    private static JFrame frame;
    private JPanel buttonPane;
    private JButton removeRouteButton;
    private JButton addRouteButton;
    private JButton saveDataButton;
    private JButton loadDataButton;
    private JTextField enterTextField;

    private Routes routes;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: initializes the UI things
    public BusOptimizerUI() {
        super(new BorderLayout());

        routes = new Routes();
        listModel = new DefaultListModel();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        listModelInit(); // initializes the list display thing
        addRemoveInit(); // adds the add button / remove button / text input
        saveLoadInit();  // initializes the save and load buttons
    }

    // EFFECTS: initialize the save and load
    public void saveLoadInit() {
        loadDataButton = new JButton(loadDataString);
        saveDataButton = new JButton(saveDataString);
        LoadDataListener loadDataListener = new LoadDataListener();
        SaveDataListener saveDataListener = new SaveDataListener();
        loadDataButton.setActionCommand(loadDataString);
        saveDataButton.setActionCommand(saveDataString);
        loadDataButton.addActionListener(loadDataListener);
        saveDataButton.addActionListener(saveDataListener);

        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(loadDataButton);
        buttonPane.add(saveDataButton);

        add(buttonPane, BorderLayout.PAGE_END);
    }

    // EFFECTS: listener that saves the routes to file on click
    class SaveDataListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(routes);
                jsonWriter.close();
                JOptionPane.showMessageDialog(frame,"Routes successfully saved",
                        "Saved Routes", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(frame,"Unable to save file",
                        "Did Not Save Routes", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: listener that loads routes from file on click
    class LoadDataListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                routes = jsonReader.read();
                for (Route r : routes.getRouteList()) {
                    listModel.addElement(r.getRouteName());
                }
                loadDataButton.setEnabled(false);
                JOptionPane.showMessageDialog(frame,"Routes successfully loaded",
                        "Loaded Routes", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(frame,"Unable to load file",
                        "Did Not Load Routes", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // EFFECTS: initialize list model (from List Demo)
    private void listModelInit() {
        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
    }

    // EFFECTS: initialize add and remove buttons and text field
    public void addRemoveInit() { // Form follows from List Demo example
        JScrollPane listScrollPane = new JScrollPane(list);

        // adds buttons for adding / removing
        addRouteButton = new JButton(addRouteString);
        AddRouteListener addRouteListener = new AddRouteListener(addRouteButton);
        addRouteButton.setActionCommand(addRouteString);
        addRouteButton.addActionListener(addRouteListener);
        addRouteButton.setEnabled(false);

        removeRouteButton = new JButton(removeRouteString);
        removeRouteButton.setActionCommand(removeRouteString);
        removeRouteButton.addActionListener(new RemoveRouteListener());

        // input text field
        enterTextField = new JTextField(10);
        enterTextField.addActionListener(addRouteListener);
        enterTextField.getDocument().addDocumentListener(addRouteListener);

        //Create a panel that uses BoxLayout.
        buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(removeRouteButton);
        buttonPane.add(enterTextField);
        buttonPane.add(addRouteButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    // EFFECTS: listener that adds routes on click button
    class AddRouteListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddRouteListener(JButton button) {
            this.button = button;
        }

        // Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String name = enterTextField.getText();

            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                enterTextField.requestFocusInWindow();
                enterTextField.selectAll();
                return;
            }

            int index = list.getSelectedIndex();
            if (index == -1) {
                index = 0;
            } else {
                index++;
            }

            listModel.insertElementAt(enterTextField.getText(), index);
            routes.addRoute(new Route(enterTextField.getText(), new ArrayList<>())); // adds a new route to routes !!!
            enterTextField.requestFocusInWindow();
            enterTextField.setText("");

            // Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);

            // my visual component
            JOptionPane.showMessageDialog(frame,"Route successfully added",
                    "Route Added", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        // EFFECTS: checks if list already has item
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        // EFFECTS: updates button (Required by DocumentListener)
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        // EFFECTS: updates for if a text field has a remove (Required by DocumentListener)
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        // EFFECTS: updates for text field not empty (Required by DocumentListener)
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        // EFFECTS: enables button
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        // EFFECTS: disables addition if text field is empty
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    // EFFECTS: listener that removes routes on click button
    class RemoveRouteListener implements ActionListener {
        // EFFECTS: action listener for removing route
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            int size = listModel.getSize();

            // removes from list at index, removes from routes
            listModel.remove(index);
            routes.removeRoute(routes.getRouteList().get(index).getRouteName());

            if (size == 0) {
                removeRouteButton.setEnabled(false);
            } else {
                if (index == listModel.getSize()) {
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    // EFFECTS: handler for changed values in list
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
                removeRouteButton.setEnabled(false);

            } else {
                removeRouteButton.setEnabled(true);
            }
        }
    }

    // EFFECTS: creates GUI and shows it
    public static void runBusOptimizerUI() {
        frame = new JFrame("CPSC 210: Bus Optimizer");

        // prints log to console
        // from: https://stackoverflow.com/questions/60516720/java-how-to-print-message-when-a-jframe-is-closed
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.out.println("Printing Log: \n");

                for (Event next : EventLog.getInstance()) {
                    System.out.println(next.toString() + "\n");
                }
                System.exit(0); // exit
            }
        });

        // Create and set up the content pane
        JComponent newContentPane = new BusOptimizerUI();
        frame.setContentPane(newContentPane);

        // Displays the window
        frame.pack();
        frame.setVisible(true);
    }

    // EFFECTS: creates an image icon
    protected ImageIcon createImageIcon(String path,
                                        String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}