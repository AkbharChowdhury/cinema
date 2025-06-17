package forms;

import models.Genre;
import models.Messages;
import models.MovieInfo;
import models.MyWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import dbs.Database;

public class EditMovieForm extends JFrame implements ActionListener {
    private static MainMenuWithTable mainMenu;
    final int MOVIE_ID = MovieInfo.getMovieID();

    Database db = Database.getInstance();
    final String MOVIE_TITLE = db.getMovieName(MOVIE_ID);
    List<Genre> genreList = db.getAllGenres();
    JTextField txtTitle = new JTextField(20);
    JButton btnUpdateMovie = new JButton("Update Movie");
    JButton btnUndoTitle = new JButton("Undo title");

    List<Checkbox> checkboxes;


    public EditMovieForm(MainMenuWithTable mainMenuForm) {
        mainMenu = mainMenuForm;
        txtTitle.setText(MOVIE_TITLE);
        setTitle("Edit Movie");
        JPanel panel = new JPanel();
        JPanel top = new JPanel();
        JPanel middle = new JPanel();
        panel.setLayout(new BorderLayout());

        top.add(new JLabel("Movie"));
        top.add(txtTitle);
        top.add(btnUndoTitle);

        middle.setLayout(new GridLayout(genreList.size(), 2));
        addGenres(middle);

        panel.add(top, BorderLayout.NORTH);
        panel.add(middle, BorderLayout.CENTER);
        panel.add(btnUpdateMovie, BorderLayout.SOUTH);
        setContentPane(panel);
        setDefaultCloseOperation(MyWindow.getCloseOperation());
        setSize(450, 400);
        btnUpdateMovie.addActionListener(this);
        btnUndoTitle.addActionListener(this);

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
        if (e.getSource() == btnUpdateMovie) {
            boolean hasSelectedGenre = Genre.hasSelectedGenre.apply(checkboxes);
            if (txtTitle.getText().trim().isBlank()) {
                Messages.showErrorMessage("Title required!", "Movie title is required");
                return;
            }
            if (!hasSelectedGenre) {
                Messages.showErrorMessage("Genre required!", "Please choose a genre");
                return;
            }

            updateGenres();

        }

        if (e.getSource() == btnUndoTitle) {
            txtTitle.setText("");
            txtTitle.setText(MOVIE_TITLE);
        }
    }

    private void updateGenres() {
        db.updateMovieTitle(txtTitle.getText().trim(), MOVIE_ID);
        db.delete("movie_genres", "movie_id", MOVIE_ID);
        List<Integer> selectedGenreIDs = Genre.getSelectedGenres(checkboxes, genreList).stream().map(Genre::id).toList();

        db.addMovieGenres(MOVIE_ID, selectedGenreIDs);
        Messages.message("Movie updated");
        redirectToMainMenu();
    }

    private void redirectToMainMenu() {
        if (mainMenu != null) mainMenu.dispose();
        dispose();
        new MainMenuWithTable();
    }


    public static void main(String[] args) {
        new EditMovieForm(mainMenu);

    }

}

