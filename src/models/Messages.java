package models;

import javax.swing.*;

public class Messages {
    private Messages() {

    }

    public static void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void message(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

}
