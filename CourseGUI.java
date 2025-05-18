package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CourseGUI extends JFrame implements ActionListener {
    private final JTextField courseIdField, courseNameField, creditsField, teacherIdField;
    private final JButton addButton, displayButton, modifyButton, deleteButton;

    private Statement stmt;

    public CourseGUI() {
        setTitle("Course Management");

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Fields
        panel.add(new JLabel("Course ID:"));
        courseIdField = new JTextField(20);
        panel.add(courseIdField);

        panel.add(new JLabel("Course Name:"));
        courseNameField = new JTextField(20);
        panel.add(courseNameField);

        panel.add(new JLabel("Credits:"));
        creditsField = new JTextField(20);
        panel.add(creditsField);

        panel.add(new JLabel("Teacher ID:"));
        teacherIdField = new JTextField(20);
        panel.add(teacherIdField);

        // Buttons
        addButton = new JButton("Add Course");
        displayButton = new JButton("Display Courses");
        modifyButton = new JButton("Modify Course");
        deleteButton = new JButton("Delete Course");

        addButton.addActionListener(this);
        displayButton.addActionListener(this);
        modifyButton.addActionListener(this);
        deleteButton.addActionListener(this);

        panel.add(addButton); panel.add(displayButton);
        panel.add(modifyButton); panel.add(deleteButton);

        add(panel);
        setSize(400, 300);
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
            JOptionPane.showMessageDialog(this, "DB Connection Error: " + ex.getMessage());
            return;
        }

        Table tb = new Table();

        if (e.getSource() == addButton) {
            String sql = "INSERT INTO courses VALUES('" + courseIdField.getText() + "', '"
                    + courseNameField.getText() + "', " + creditsField.getText() + ", '"
                    + teacherIdField.getText() + "')";
            try {
                stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog(this, "Course added successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == displayButton) {
            try {
                ResultSet rs = stmt.executeQuery("SELECT * FROM courses");
                JTable table = new JTable(tb.buildTableModel(rs));
                JOptionPane.showMessageDialog(this, new JScrollPane(table));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == modifyButton) {
            String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to modify:");
            String sql = "SELECT * FROM courses WHERE courseID = '" + courseId + "'";
            try {
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    String[] fields = {"Course Name", "Credits", "Teacher ID"};
                    int choice = JOptionPane.showOptionDialog(this, "Select field to modify:",
                            "Modify Course", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, fields, fields[0]);
                    String column = switch (choice) {
                        case 0 -> "courseName";
                        case 1 -> "credits";
                        case 2 -> "teacherID";
                        default -> "";
                    };
                    String newValue = JOptionPane.showInputDialog(this, "Enter new value:");
                    if (newValue != null && !newValue.isEmpty()) {
                        sql = "UPDATE courses SET " + column + " = '" + newValue + "' WHERE courseID = '" + courseId + "'";
                        stmt.executeUpdate(sql);
                        JOptionPane.showMessageDialog(this, "Course updated successfully.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Course not found.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == deleteButton) {
            String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to delete:");
            if (courseId != null && !courseId.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this, "Confirm deletion?", "Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String sql = "DELETE FROM courses WHERE courseID = '" + courseId + "'";
                    try {
                        int affected = stmt.executeUpdate(sql);
                        if (affected > 0) {
                            JOptionPane.showMessageDialog(this, "Course deleted.");
                        } else {
                            JOptionPane.showMessageDialog(this, "No such course found.");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    }
                }
            }
        }
    }
}