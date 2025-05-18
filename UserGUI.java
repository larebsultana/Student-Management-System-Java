package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UserGUI extends JFrame implements ActionListener {
    private final JLabel usernameLabel, passwordLabel, roleLabel;
    private final JTextField usernameField, passwordField;
    private final JComboBox<String> roleComboBox;
    private final JButton addButton, displayButton, modifyButton, deleteButton;

    private Statement stmt;

    public UserGUI() {
        setTitle("User Management");

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        roleLabel = new JLabel("Role:");

        usernameField = new JTextField(20);
        passwordField = new JTextField(20);
        roleComboBox = new JComboBox<>(new String[]{"admin", "teacher", "student"});

        addButton = new JButton("Add");
        displayButton = new JButton("Display");
        modifyButton = new JButton("Modify");
        deleteButton = new JButton("Delete");

        addButton.addActionListener(this);
        displayButton.addActionListener(this);
        modifyButton.addActionListener(this);
        deleteButton.addActionListener(this);

        panel.add(usernameLabel); panel.add(usernameField);
        panel.add(passwordLabel); panel.add(passwordField);
        panel.add(roleLabel); panel.add(roleComboBox);
        panel.add(addButton); panel.add(displayButton);
        panel.add(modifyButton); panel.add(deleteButton);

        add(panel);
        setSize(400, 250);
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
            String sql = "INSERT INTO users (username, password, role) VALUES ('" + usernameField.getText() + "', '"
                    + passwordField.getText() + "', '" + roleComboBox.getSelectedItem() + "')";
            try {
                stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog(this, "User added successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == displayButton) {
            String sql = "SELECT * FROM users";
            try {
                ResultSet rs = stmt.executeQuery(sql);
                JTable table = new JTable(tb.buildTableModel(rs));
                JOptionPane.showMessageDialog(this, new JScrollPane(table));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == modifyButton) {
            String username = JOptionPane.showInputDialog(this, "Enter username to modify:");
            if (username == null || username.isEmpty()) return;

            String sql = "SELECT * FROM users WHERE username = '" + username + "'";
            try {
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    String[] options = {"Password", "Role"};
                    int choice = JOptionPane.showOptionDialog(this, "Select field to modify:", "Modify",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                    String column = (choice == 0) ? "password" : "role";
                    String newValue;
                    if (column.equals("role")) {
                        Object selectedRole = JOptionPane.showInputDialog(this, "Select new role:",
                                "Modify Role", JOptionPane.PLAIN_MESSAGE, null,
                                new String[]{"admin", "teacher", "student"}, rs.getString("role"));
                        if (selectedRole == null) return;
                        newValue = selectedRole.toString();
                    } else {
                        newValue = JOptionPane.showInputDialog(this, "Enter new value:");
                        if (newValue == null || newValue.isEmpty()) return;
                    }

                    sql = "UPDATE users SET " + column + " = '" + newValue + "' WHERE username = '" + username + "'";
                    stmt.executeUpdate(sql);
                    JOptionPane.showMessageDialog(this, "Data updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "User not found.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == deleteButton) {
            String username = JOptionPane.showInputDialog(this, "Enter username to delete:");
            if (username == null || username.isEmpty()) return;

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM users WHERE username = '" + username + "'";
                try {
                    int rowsAffected = stmt.executeUpdate(sql);
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "User deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "No user found with the given username.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        }
    }
}