package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TeacherGUI extends JFrame implements ActionListener {
    private final JTextField teacherIdField, firstNameField, lastNameField, subjectField, emailField, phoneField;
    private final JButton addButton, displayButton, deleteButton;
    private Statement stmt;

    public TeacherGUI() {
        setTitle("Teacher Management");

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        // Fields
        panel.add(new JLabel("Teacher ID:"));
        teacherIdField = new JTextField(20);
        panel.add(teacherIdField);

        panel.add(new JLabel("First Name:"));
        firstNameField = new JTextField(20);
        panel.add(firstNameField);

        panel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField(20);
        panel.add(lastNameField);

        panel.add(new JLabel("Subject:"));
        subjectField = new JTextField(20);
        panel.add(subjectField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField(20);
        panel.add(emailField);

        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField(20);
        panel.add(phoneField);

        addButton = new JButton("Add Teacher");
        displayButton = new JButton("Display All");
        deleteButton = new JButton("Delete by ID");

        addButton.addActionListener(this);
        displayButton.addActionListener(this);
        deleteButton.addActionListener(this);

        panel.add(addButton);
        panel.add(displayButton);
        panel.add(deleteButton);

        add(panel);
        setSize(400, 400);
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
            String sql = "INSERT INTO teachers VALUES('" + teacherIdField.getText() + "', '"
                    + firstNameField.getText() + "', '" + lastNameField.getText() + "', '" + subjectField.getText()
                    + "', '" + emailField.getText() + "', '" + phoneField.getText() + "')";
            try {
                stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog(this, "Teacher added successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == displayButton) {
            try {
                ResultSet rs = stmt.executeQuery("SELECT * FROM teachers");
                JTable table = new JTable(tb.buildTableModel(rs));
                JOptionPane.showMessageDialog(this, new JScrollPane(table));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == deleteButton) {
            String teacherId = JOptionPane.showInputDialog(this, "Enter Teacher ID to delete:");
            if (teacherId != null && !teacherId.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this teacher?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        int rowsAffected = stmt.executeUpdate("DELETE FROM teachers WHERE teacherID = '" + teacherId + "'");
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Teacher deleted successfully.");
                        } else {
                            JOptionPane.showMessageDialog(this, "No teacher found with the given ID.");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    }
                }
            }
        }
    }
}