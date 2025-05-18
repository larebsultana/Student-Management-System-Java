package Project;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {

    public SplashScreen(int duration) {
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.white);

        int width = 450;
        int height = 300;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        JLabel label = new JLabel(new ImageIcon("splash.png")); // Ensure this file is in the correct directory
        JLabel copyrt = new JLabel("Student Management System", JLabel.CENTER);
        copyrt.setFont(new Font("Segoe UI", Font.BOLD, 24));
        copyrt.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        content.add(label, BorderLayout.CENTER);
        content.add(copyrt, BorderLayout.SOUTH);
        Color borderColor = new Color(70, 130, 180);
        content.setBorder(BorderFactory.createLineBorder(borderColor, 5));

        setVisible(true);

        try {
            Thread.sleep(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(false);
    }

    public static void showSplashAndLaunch() {
        SplashScreen splash = new SplashScreen(3000);
        splash.dispose();

        SwingUtilities.invokeLater(() -> {
            MainMenuGUI menu = new MainMenuGUI();
            menu.setVisible(true);
        });
    }
}