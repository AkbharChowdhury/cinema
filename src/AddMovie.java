

import javax.swing.*;
import java.awt.*;
import java.awt.Checkbox;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


public final class AddMovie extends JFrame implements ActionListener {


    private final JTextField txtTitle = new JTextField(20);
    String[] genres = {"Action", "Adventure", "Horror", "Animation", "Drama", "Thriller", "Comedy"};

    JButton btnAdd = new JButton("Add Movie");

    List<Checkbox> checkboxes;

    public AddMovie() {

        setResizable(false);
        setLayout(new BorderLayout());
        setSize(400, 400);
        setTitle("Add Movie");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        top.add(new JLabel("Movie Title:"));
        top.add(txtTitle);
        JPanel middle = new JPanel();
        var labels = Arrays.stream(genres).sorted().toList();
        checkboxes = labels.stream().map(Checkbox::new).toList();
        checkboxes.forEach(middle::add);

        JPanel south = new JPanel();

        south.add(btnAdd);

        add(BorderLayout.NORTH, top);
        add(BorderLayout.CENTER, middle);
        add(BorderLayout.SOUTH, south);


        btnAdd.addActionListener(this);
        autofocus();
        setVisible(true);


    }


    public static void main() {
        new AddMovie();
    }

    private void autofocus() {

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                txtTitle.requestFocus();
            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            System.out.println("Selected genres");
            List<String> selectedGenres = checkboxes.stream().filter(Checkbox::getState).map(Checkbox::getLabel).toList();
            String genreFormatted = String.join("|", selectedGenres);
            System.out.println(genreFormatted);
        }
    }


}



