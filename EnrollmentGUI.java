package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EnrollmentGUI extends JFrame implements ActionListener {
    private final JTextField enrollmentIdField, studentIdField, courseIdField, gradeField;
    private final JButton addButton, displayButton, modifyButton, deleteButton;
    private Statement stmt;

    public EnrollmentGUI() {
        setTitle("Enrollment Management");

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        panel.add(new JLabel("Enrollment ID (for modify/delete):"));
        enrollmentIdField = new JTextField(20);
        panel.add(enrollmentIdField);

        panel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField(20);
        panel.add(studentIdField);

        panel.add(new JLabel("Course ID:"));
        courseIdField = new JTextField(20);
        panel.add(courseIdField);

        panel.add(new JLabel("Grade:"));
        gradeField = new JTextField(20);
        panel.add(gradeField);

        addButton = new JButton("Add Enrollment");
        displayButton = new JButton("Display Enrollments");
        modifyButton = new JButton("Modify Enrollment");
        deleteButton = new JButton("Delete Enrollment");

        addButton.addActionListener(this);
        displayButton.addActionListener(this);
        modifyButton.addActionListener(this);
        deleteButton.addActionListener(this);

        panel.add(addButton);
        panel.add(displayButton);
        panel.add(modifyButton);
        panel.add(deleteButton);

        add(panel);
        setSize(450, 300);
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
            JOptionPane.showMessageDialog(this, "Database Connection Error: " + ex.getMessage());
            return;
        }

        Table tb = new Table();

        if (e.getSource() == addButton) {
            String sql = "INSERT INTO enrollments (studentID, courseID, grade) VALUES ('"
                    + studentIdField.getText() + "', '" + courseIdField.getText() + "', '"
                    + gradeField.getText() + "')";
            try {
                stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog(this, "Enrollment added successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == displayButton) {
            try {
                ResultSet rs = stmt.executeQuery("SELECT * FROM enrollments");
                JTable table = new JTable(tb.buildTableModel(rs));
                JOptionPane.showMessageDialog(this, new JScrollPane(table));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == modifyButton) {
            String enrollmentId = enrollmentIdField.getText().trim();
            if (enrollmentId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Enrollment ID to modify.");
                return;
            }
            try {
                ResultSet rs = stmt.executeQuery("SELECT * FROM enrollments WHERE enrollmentID = " + enrollmentId);
                if (rs.next()) {
                    String[] fields = {"Student ID", "Course ID", "Grade"};
                    int choice = JOptionPane.showOptionDialog(this, "Select field to modify:",
                            "Modify Enrollment", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE, null, fields, fields[0]);
                    String column = switch (choice) {
                        case 0 -> "studentID";
                        case 1 -> "courseID";
                        case 2 -> "grade";
                        default -> "";
                    };
                    String newValue = JOptionPane.showInputDialog(this, "Enter new value:");
                    if (newValue != null && !newValue.isEmpty()) {
                        String sql = "UPDATE enrollments SET " + column + " = '" + newValue + "' WHERE enrollmentID = " + enrollmentId;
                        stmt.executeUpdate(sql);
                        JOptionPane.showMessageDialog(this, "Enrollment updated successfully.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Enrollment ID not found.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == deleteButton) {
            String enrollmentId = enrollmentIdField.getText().trim();
            if (enrollmentId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Enrollment ID to delete.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this enrollment?", "Delete Enrollment", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int deleted = stmt.executeUpdate("DELETE FROM enrollments WHERE enrollmentID = " + enrollmentId);
                    if (deleted > 0) {
                        JOptionPane.showMessageDialog(this, "Enrollment deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Enrollment ID not found.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        }
    }
}