package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentGUI extends JFrame implements ActionListener {
    private final JLabel studentIdLabel, firstNameLabel, lastNameLabel, majorLabel, phoneLabel, gpaLabel, dobLabel;
    private final JTextField studentIdField, firstNameField, lastNameField, majorField, phoneField, gpaField, dobField;
    private final JButton addButton, displayButton, sortButton, searchButton, modifyButton, deleteButton;
    private final JComboBox<String> sortComboBox, searchComboBox;

    private Statement stmt;

    public StudentGUI() {
        setTitle("Student Management - sdata Table");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 5, 5));

        // Labels
        studentIdLabel = new JLabel("Student ID:");
        firstNameLabel = new JLabel("First Name:");
        lastNameLabel = new JLabel("Last Name:");
        majorLabel = new JLabel("Major:");
        phoneLabel = new JLabel("Phone:");
        gpaLabel = new JLabel("GPA:");
        dobLabel = new JLabel("Date of Birth (yyyy-mm-dd):");

        // Text Fields
        studentIdField = new JTextField(20);
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        majorField = new JTextField(20);
        phoneField = new JTextField(20);
        gpaField = new JTextField(20);
        dobField = new JTextField(20);

        // Buttons
        addButton = new JButton("Add");
        displayButton = new JButton("Display");
        sortButton = new JButton("Sort");
        searchButton = new JButton("Search");
        modifyButton = new JButton("Modify");
        deleteButton = new JButton("Delete");

        // ComboBoxes
        sortComboBox = new JComboBox<>(new String[]{"First Name", "Last Name", "Major"});
        searchComboBox = new JComboBox<>(new String[]{"Student ID", "Last Name", "Major"});

        // Action Listeners
        addButton.addActionListener(this);
        displayButton.addActionListener(this);
        sortButton.addActionListener(this);
        searchButton.addActionListener(this);
        modifyButton.addActionListener(this);
        deleteButton.addActionListener(this);

        // Add Components to Panel
        panel.add(studentIdLabel); panel.add(studentIdField);
        panel.add(firstNameLabel); panel.add(firstNameField);
        panel.add(lastNameLabel); panel.add(lastNameField);
        panel.add(majorLabel); panel.add(majorField);
        panel.add(phoneLabel); panel.add(phoneField);
        panel.add(gpaLabel); panel.add(gpaField);
        panel.add(dobLabel); panel.add(dobField);

        panel.add(new JLabel("Sort by:")); panel.add(sortComboBox);
        panel.add(sortButton); panel.add(displayButton);

        panel.add(new JLabel("Search by:")); panel.add(searchComboBox);
        panel.add(searchButton); panel.add(modifyButton);
        panel.add(deleteButton); panel.add(addButton);

        add(panel);
        setSize(400, 500);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dbConnect db = new dbConnect();
        Connection conn;
        try {
            conn = db.getConnection();
            stmt = conn.createStatement();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + ex.getMessage());
            return;
        }

        Table tb = new Table();

        if (e.getSource() == addButton) {
            String sql = "INSERT INTO sdata VALUES('" + studentIdField.getText() + "', '"
                    + firstNameField.getText() + "', '" + lastNameField.getText() + "', '" + majorField.getText()
                    + "', '" + phoneField.getText() + "', '" + gpaField.getText() + "', '" + dobField.getText() + "')";
            try {
                stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog(this, "Student added successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == displayButton) {
            String sql = "SELECT * FROM sdata";
            try {
                ResultSet rs = stmt.executeQuery(sql);
                JTable table = new JTable(tb.buildTableModel(rs));
                JOptionPane.showMessageDialog(this, new JScrollPane(table));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == sortButton) {
            String selected = (String) sortComboBox.getSelectedItem();
            String sql = switch (selected) {
                case "First Name" -> "SELECT * FROM sdata ORDER BY firstName";
                case "Last Name" -> "SELECT * FROM sdata ORDER BY lastName";
                case "Major" -> "SELECT * FROM sdata ORDER BY major";
                default -> "";
            };
            try {
                ResultSet rs = stmt.executeQuery(sql);
                JTable table = new JTable(tb.buildTableModel(rs));
                JOptionPane.showMessageDialog(this, new JScrollPane(table));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == searchButton) {
            String selected = (String) searchComboBox.getSelectedItem();
            String column = switch (selected) {
                case "Student ID" -> "studentID";
                case "Last Name" -> "lastName";
                case "Major" -> "major";
                default -> "";
            };
            String searchTerm = JOptionPane.showInputDialog(this, "Enter search term:");
            if (searchTerm != null && !searchTerm.isEmpty()) {
                String sql = "SELECT * FROM sdata WHERE " + column + " LIKE '%" + searchTerm + "%'";
                try {
                    ResultSet rs = stmt.executeQuery(sql);
                    JTable table = new JTable(tb.buildTableModel(rs));
                    JOptionPane.showMessageDialog(this, new JScrollPane(table));
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }

        } else if (e.getSource() == modifyButton) {
            String studentId = JOptionPane.showInputDialog(this, "Enter student ID to modify:");
            String sql = "SELECT * FROM sdata WHERE studentID = '" + studentId + "'";
            try {
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    String[] options = {"First Name", "Last Name", "Major", "Phone", "GPA", "Date of Birth"};
                    int choice = JOptionPane.showOptionDialog(this, "Select field to modify:", "Modify",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                    String column = switch (choice) {
                        case 0 -> "firstName";
                        case 1 -> "lastName";
                        case 2 -> "major";
                        case 3 -> "phone";
                        case 4 -> "gpa";
                        case 5 -> "dob";
                        default -> "";
                    };
                    String newValue = JOptionPane.showInputDialog(this, "Enter new value:");
                    if (newValue != null && !newValue.isEmpty()) {
                        sql = "UPDATE sdata SET " + column + " = '" + newValue + "' WHERE studentID = '" + studentId + "'";
                        stmt.executeUpdate(sql);
                        JOptionPane.showMessageDialog(this, "Data updated successfully.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == deleteButton) {
            String studentId = JOptionPane.showInputDialog(this, "Enter Student ID to delete:");
            if (studentId != null && !studentId.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String sql = "DELETE FROM sdata WHERE studentID = '" + studentId + "'";
                    try {
                        int rowsAffected = stmt.executeUpdate(sql);
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Student deleted successfully.");
                        } else {
                            JOptionPane.showMessageDialog(this, "No student found with the given ID.");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    }
                }
            }
        }
    }
}