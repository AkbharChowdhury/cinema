
import models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class EditMovieForm extends JFrame implements ActionListener {
    private static MainMenu mainMenu;
    private final int MOVIE_ID = MovieInfo.getMovieID();
    private final Database db = Database.getInstance();
    final String MOVIE_TITLE = db.getMovieName(MOVIE_ID);
    private final List<Genre> genreList = db.getAllGenres();
    private final JTextField txtTitle = new JTextField(40);
    private final JButton btnUpdateMovie = new JButton("Update Movie");
    private final JButton btnUndoTitle = new JButton("Undo title");
    private final JButton btnUndoGenre = new JButton("Undo Genre");

    private final JButton[] buttons = {btnUpdateMovie, btnUndoTitle, btnUndoGenre};


    private final List<Checkbox> checkboxes;


    public EditMovieForm(MainMenu mainMenuForm) {
        System.out.println(MOVIE_ID);
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
        top.add(btnUndoGenre);

        middle.setLayout(new GridLayout(genreList.size(), 2));

        checkboxes = genreList.stream().map(genre -> new Checkbox(genre.name())).toList();
        checkboxes.forEach(middle::add);

        panel.add(top, BorderLayout.NORTH);
        panel.add(middle, BorderLayout.CENTER);
        panel.add(btnUpdateMovie, BorderLayout.SOUTH);
        setContentPane(panel);
        setDefaultCloseOperation(MyWindow.getCloseOperation());
        setSize(750, 400);

        Arrays.stream(buttons).forEach(button -> button.addActionListener(this));
        MyButton.handCursor.accept(buttons);
        showSelectedGenres();

        setVisible(true);
    }

    private void showSelectedGenres() {
        checkboxes.stream()
                .filter(checkbox -> db.getSelectedMovieGenres(MOVIE_ID).stream().anyMatch(label -> label.equals(checkbox.getLabel())))
                .forEach(checkbox -> checkbox.setState(true));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnUndoGenre) {
            checkboxes.forEach(checkbox -> checkbox.setState(false));
            showSelectedGenres();
        }
        if (e.getSource() == btnUpdateMovie) {
            if (!isFormValid()) return;
            updateGenres();
        }

        if (e.getSource() == btnUndoTitle) {
            txtTitle.setText("");
            txtTitle.setText(MOVIE_TITLE);
        }
    }

    private boolean isFormValid() {

        boolean hasSelectedGenre = Genre.hasSelectedGenre.apply(checkboxes);
        if (txtTitle.getText().trim().isBlank()) {
            Messages.showErrorMessage("Title required!", "Movie title is required");
            return false;
        }
        if (!hasSelectedGenre) {
            Messages.showErrorMessage("Genre required!", "Please choose a genre");
            return false;
        }
        return true;
    }

    private void updateGenres() {
        db.updateMovieTitle(txtTitle.getText().trim(), MOVIE_ID);
        db.deleteRecord("movie_genres", "movie_id", MOVIE_ID);
        List<Integer> selectedGenres = Genre.getSelectedGenres(checkboxes, genreList).stream().map(Genre::id).toList();

        db.addMovieGenres(MOVIE_ID, selectedGenres);
        Messages.message("Movie updated");
        redirectToMainMenu();
    }

    private void redirectToMainMenu() {
        if (mainMenu != null) mainMenu.dispose();
        dispose();
        new MainMenu();
    }


    public static void main(String[] args) {
        new EditMovieForm(mainMenu);

    }

}

