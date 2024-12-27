import models.Genre;
import models.MovieInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EditMovieForm extends JFrame implements ActionListener {
    final int MOVIE_ID = MovieInfo.getMovieID();
    DB db = DB.getInstance();
    final String MOVIE_TITLE = db.getMovieName(MOVIE_ID);
    List<Genre> genreList = db.getAllGenres();
    JTextField txtTitle = new JTextField(20);
    JButton btnUpdateMovie = new JButton("Update models.Movie");
    JButton btnReset = new JButton("Undo title");

    List<Checkbox> checkboxes;


    public EditMovieForm() {
        txtTitle.setText(MOVIE_TITLE);
        setTitle("Edit Movie");
        JPanel panel = new JPanel();
        JPanel top = new JPanel();
        JPanel middle = new JPanel();
        panel.setLayout(new BorderLayout());

        top.add(new JLabel("Movie"));
        top.add(txtTitle);
        top.add(btnReset);

        middle.setLayout(new GridLayout(genreList.size(), 2));
        addGenres(middle);

        panel.add(top, BorderLayout.NORTH);
        panel.add(middle, BorderLayout.CENTER);
        panel.add(btnUpdateMovie, BorderLayout.SOUTH);
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 400);
        btnUpdateMovie.addActionListener(this);
        btnReset.addActionListener(this);

        setVisible(true);
        ShowSelectedGenres();
    }

    private void ShowSelectedGenres() {
        checkboxes.stream()
                .filter(checkbox -> db.getSelectedMovieGenres(MOVIE_ID).stream().anyMatch(label -> label.equals(checkbox.getLabel())))
                .forEach(checkbox -> checkbox.setState(true));
    }

    private void addGenres(JPanel middle) {
        checkboxes = genreList.stream().map(genre -> new Checkbox(genre.type())).toList();
        checkboxes.forEach(middle::add);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnUpdateMovie){
            boolean hasSelectedGenre = checkboxes.stream().anyMatch(Checkbox::getState);
            if (txtTitle.getText().trim().isBlank()) {
                JOptionPane.showMessageDialog(null, "models.Movie title is required");
                return;
            }
            if (!hasSelectedGenre) {
                JOptionPane.showMessageDialog(null, "Please choose a genre");
                return;
            }
            updateGenres();

        }

        if (e.getSource() == btnReset){
            txtTitle.setText("");
            txtTitle.setText(MOVIE_TITLE);
        }


    }

    private void updateGenres() {
        db.deleteMovieGenre(MOVIE_ID);
        List<Integer> selectedGenreIDs = Genre.getSelectedGenres(checkboxes, genreList).stream().map(Genre::id).toList();
        db.addMovieGenres(MOVIE_ID, selectedGenreIDs);
        new EditMovieForm();

    }


    public static void main() {
        new EditMovieForm();
    }

}

