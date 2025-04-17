/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author RC_Student_lab
 */
 import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Random;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JLabel statusLabel;

    private static final String DATA_FILE = "user_data.txt";

    public Login() {
        // Welcome Message
        JOptionPane.showMessageDialog(this, "Welcome to QuickChat!");
        setTitle("User Login & Registration");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2)); // Updated GridLayout to accommodate new button

        // UI Components
        add(new JLabel("Username (1-7 chars, must have _):"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password (min 8 chars, 1 uppercase, 1 number, 1 symbol):"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Cell Number (+27xxxxxxxxx):"));
        phoneField = new JTextField();
        add(phoneField);

        JButton registerButton = new JButton("Register");
        add(registerButton);

        JButton loginButton = new JButton("Login");
        add(loginButton);

        JButton viewUsersButton = new JButton("View Registered Users"); // New Button
        add(viewUsersButton);

        statusLabel = new JLabel("");
        add(statusLabel);

        // Button Actions
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        viewUsersButton.addActionListener(new ActionListener() { // New ActionListener
            @Override
            public void actionPerformed(ActionEvent e) {
                displayRegisteredUsers();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String phone = phoneField.getText();

        if (!checkUserName(username)) {
            statusLabel.setText("Invalid username format.");
            return;
        }
        if (!checkPasswordComplexity(password)) {
            statusLabel.setText("Invalid password format.");
            return;
        }
        if (!checkCellPhoneNumber(phone)) {
            statusLabel.setText("Invalid phone format.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            writer.write(username + "," + password + "," + phone);
            writer.newLine();
        } catch (IOException e) {
            statusLabel.setText("Error saving user details.");
            return;
        }

        statusLabel.setText("Registration successful!");
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(username) && userDetails[1].equals(password)) {
                    JOptionPane.showMessageDialog(this, " Hi " + username.split("_")[0] + "!");
                    new QuickChat(username).setVisible(true);
                    // Open QuickChat on successful login
                    dispose(); // Close Login window
                    return;
                }
            }
        } catch (IOException e) {
            statusLabel.setText("Error reading user details.");
            return;
        }

        statusLabel.setText("Login failed. Try again.");
    }

    private boolean checkUserName(String username) {
        return username.matches("^[a-zA-Z0-9_]{1,7}$");
    }

    private boolean checkPasswordComplexity(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return password.matches(regex);
    }

    private boolean checkCellPhoneNumber(String phone) {
        return phone.matches("^\\+27\\d{9}$");
    }

    private void displayRegisteredUsers() { // New Method
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            StringBuilder users = new StringBuilder("Registered Users:\n");
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                users.append("Username: ").append(userDetails[0])
                     .append(", Cell Number: ").append(userDetails[2])
                     .append("\n");
            }
            JOptionPane.showMessageDialog(this, users.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading user data.");
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}

// QuickChat Class remains unchanged

