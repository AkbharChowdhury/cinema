import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class EditMovieForm extends JFrame implements ActionListener {
    final int MOVIE_ID = 108;
    DB db = DB.getInstance();
    List<Genre> genreList = db.getAllGenres();
    JTextField txtTitle = new JTextField(20);
    JButton btnAdd = new JButton("Add Movie");
    List<Checkbox> checkboxes;
    List<Integer> initialGenreID = db.getMovieGenres(MOVIE_ID);

    public EditMovieForm() {
        txtTitle.setText(db.getMovieName(MOVIE_ID));
        setTitle("Edit Movie");
        JPanel panel = new JPanel();
        JPanel top = new JPanel();
        JPanel middle = new JPanel();
        panel.setLayout(new BorderLayout());

        top.add(new JLabel("Movie"));
        top.add(txtTitle);
        middle.setLayout(new GridLayout(genreList.size(), 2));
        addGenres(middle);

        panel.add(top, BorderLayout.NORTH);
        panel.add(middle, BorderLayout.CENTER);
        panel.add(btnAdd, BorderLayout.SOUTH);

        setContentPane(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        btnAdd.addActionListener(this);
        autofocus();
        setVisible(true);
        ShowSelectedGenres();
    }

    private void ShowSelectedGenres() {
        List<Integer> selectedGenreIDs = db.getMovieGenres(MOVIE_ID);
        var selected = db.getMovieGenres2(MOVIE_ID);

        checkboxes.stream()
                .filter(checkbox -> selected.stream().anyMatch(label -> label.equals(checkbox.getLabel())))
                .forEach(checkbox -> checkbox.setState(true));

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
        boolean hasSelectedGenre = checkboxes.stream().anyMatch(Checkbox::getState);
        if (txtTitle.getText().trim().isBlank()) {
            JOptionPane.showMessageDialog(null, "Movie title is required");
            return;
        }
        if (!hasSelectedGenre) {
            JOptionPane.showMessageDialog(null, "Please choose a genre");
            return;
        }
        updateGenres();

    }

    private void updateGenres() {
        db.deleteMovieGenre(MOVIE_ID);
        List<Integer> selectedGenreIDs = Genre.getSelectedGenres(checkboxes, genreList).stream().map(Genre::id).toList();
        db.addMovieGenres(MOVIE_ID, selectedGenreIDs);
        new EditMovieForm();

    }

    private void clearForm() {
        txtTitle.setText("");
        checkboxes.forEach(checkbox -> checkbox.setState(false));
    }


    public static void main() {
        new EditMovieForm();
    }

}

