package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenuGUI extends JFrame {

    public MainMenuGUI() {
        setTitle("Student Management System - Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        JButton btnStudents = new JButton("Manage Students");
        JButton btnTeachers = new JButton("Manage Teachers");
        JButton btnCourses = new JButton("Manage Courses");
        JButton btnEnrollments = new JButton("Manage Enrollments");
        JButton btnExit = new JButton("Exit");

        btnStudents.addActionListener((ActionEvent e) -> new StudentGUI().setVisible(true));
        btnTeachers.addActionListener((ActionEvent e) -> new TeacherGUI().setVisible(true));
        btnCourses.addActionListener((ActionEvent e) -> new CourseGUI().setVisible(true));
        btnEnrollments.addActionListener((ActionEvent e) -> new EnrollmentGUI().setVisible(true));
        btnExit.addActionListener((ActionEvent e) -> System.exit(0));

        add(btnStudents);
        add(btnTeachers);
        add(btnCourses);
        add(btnEnrollments);
        add(btnExit);
    }
}