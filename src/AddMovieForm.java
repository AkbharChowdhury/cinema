import models.Genre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class AddMovieForm extends JFrame implements ActionListener {
    DB db = DB.getInstance();
    List<Genre> genreList = db.getAllGenres();
    JTextField txtTitle = new JTextField(20);
    JButton btnAddMovie = new JButton("Add Movie");
    List<Checkbox> checkboxes;

    public AddMovieForm() {
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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

//            String genreFormatted = String.join("|", selectedGenres);
            // db.addMovie(txtTitle.getText().trim(), genreFormatted);
            db.addMovie(txtTitle.getText().trim());

            int lastInsertedMovieID = db.getLastID("movie_id", "movies");
            db.addMovieGenres(lastInsertedMovieID, selectedGenreIDs);
            clearForm();
            JOptionPane.showMessageDialog(null,"Movie Added");
        }
    }

    private void clearForm() {
        txtTitle.setText("");
        checkboxes.forEach(checkbox -> checkbox.setState(false));
    }


    public static void main() {
        new AddMovieForm();
    }

}

