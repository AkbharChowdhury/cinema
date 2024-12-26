import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        JTextField txtTitle = new JTextField(30);

        String[] genres = {"Action", "Adventure", "Horror", "Animation", "Drama", "Thriller", "Comedy", "Si-fi"};
        List<Checkbox> checkboxes;
        JFrame f = new JFrame("Add Movie");

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        JPanel middle = new JPanel();

        JPanel top = new JPanel();
        top.add(new JLabel("Movie"));
        top.add(new JTextField(20));
        middle.setLayout(new GridLayout(genres.length, 2));


        var labels = Arrays.stream(genres).sorted().toList();
        checkboxes = labels.stream().map(Checkbox::new).toList();
        checkboxes.forEach(middle::add);
        // random filling to demonstrate the result of the filled grid


        JButton btn = new JButton("Add");
        content.add(top, BorderLayout.NORTH);

        content.add(middle, BorderLayout.CENTER);
        content.add(btn, BorderLayout.SOUTH);

        f.setContentPane(content);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 400);
        f.setVisible(true);

        btn.addActionListener(_ -> {
                long count = checkboxes.stream().filter(Checkbox::getState).count();
                if (count == 0) {
                    JOptionPane.showMessageDialog(null, "Please choose a genre");
                    return;
                }

                System.out.println("Selected genres");
                List<String> selectedGenres = checkboxes.stream().filter(Checkbox::getState).map(Checkbox::getLabel).toList();
                String genreFormatted = String.join("|", selectedGenres);
                System.out.println(txtTitle.getText() + "  " + genreFormatted);
                System.out.println(genreFormatted);

        });
    }
}