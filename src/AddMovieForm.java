import models.Genre;
import models.MyWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddMovieForm extends JFrame implements ActionListener {
    private static MainMenu mainMenu;
    DB db = DB.getInstance();
    List<Genre> genreList = db.getAllGenres();
    JTextField txtTitle = new JTextField(20);
    JButton btnAddMovie = new JButton("Add Movie");
    List<Checkbox> checkboxes;

    public AddMovieForm(MainMenu mainMenuForm) {
        mainMenu = mainMenuForm;
        setTitle("Add Movie");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel middle = new JPanel();

        JPanel top = new JPanel();
        top.add(new JLabel("Movie"));
        top.add(txtTitle);
        middle.setLayout(new GridLayout(genreList.size(), 2));

        addGenres(middle);

        panel.add(top, BorderLayout.NORTH);
        panel.add(middle, BorderLayout.CENTER);
        panel.add(btnAddMovie, BorderLayout.SOUTH);

        setContentPane(panel);
        setDefaultCloseOperation(MyWindow.getCloseOperation());
        setSize(400, 400);
        btnAddMovie.addActionListener(this);
        autofocus();
        setVisible(true);

    }

    private void addGenres(JPanel middle) {
        checkboxes = genreList.stream().map(genre -> new Checkbox(genre.type())).toList();
        checkboxes.forEach(middle::add);
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
        if (e.getSource() == btnAddMovie) {
            boolean hasSelectedGenre = checkboxes.stream().anyMatch(Checkbox::getState);
            if (txtTitle.getText().trim().isBlank()){
                JOptionPane.showMessageDialog(null, "Movie title is required");
                return;
            }
            if (!hasSelectedGenre) {
                JOptionPane.showMessageDialog(null, "Please choose a genre");
                return;
            }

            List<String> selectedGenres = checkboxes.stream().filter(Checkbox::getState).map(Checkbox::getLabel).toList();
            List<Integer> selectedGenreIDs = Genre.getSelectedGenres(checkboxes, genreList).stream().map(Genre::id).toList();
            boolean hasAddedMovie = db.addMovieAndGenres(txtTitle.getText().trim(), new HashSet<>(selectedGenreIDs));
            if (!hasAddedMovie){
                JOptionPane.showMessageDialog(null,"There was an error adding the movie");
                return;
            }
            clearForm();
            JOptionPane.showMessageDialog(null,"Movie Added");

            mainMenu.dispose();
            this.dispose();
            new MainMenu();


        }
    }

    private void clearForm() {
        txtTitle.setText("");
        checkboxes.forEach(checkbox -> checkbox.setState(false));
    }




    public static void main(String[] args) {
        new AddMovieForm(mainMenu);
    }

}

