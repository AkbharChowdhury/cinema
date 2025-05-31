import models.Messages;
import models.Movie;
import models.MovieInfo;
import models.MyWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainMenu extends JFrame implements ActionListener {
    Database db = Database.getInstance();
    List<Movie> movieList = db.getMovieList();
    SearchMovies search = new SearchMovies(movieList);

    JButton btnEdit = new JButton("Edit");
    JButton btnAdd = new JButton("Add");
    JButton btnRemove = new JButton("Remove");

    DefaultListModel<String> model = new DefaultListModel<>();
    JList<String> list = new JList<>(model);
    JTextField txtTitle = new JTextField(40);
    JComboBox<String> comboBoxGenres = new JComboBox<>();
    List<String> genreList = new ArrayList<>(db.getMovieGenres());



    public MainMenu() {
        List<String> genreList = new ArrayList<>(db.getMovieGenres());
        genreList.addFirst("Any");
        comboBoxGenres.setModel(new DefaultComboBoxModel<>(new Vector<>(genreList)));
        list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        setResizable(true);
        setLayout(new BorderLayout());
        setSize(800, 300);
        setTitle("Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();

        top.add(new JLabel("Movie: "));
        top.add(txtTitle);
        top.add(new JLabel("Genre"));
        top.add(comboBoxGenres);



        JPanel middle = new JPanel();
        middle.add(new JScrollPane(list));

        JPanel south = new JPanel();

        south.add(btnRemove);
        south.add(btnEdit);
        south.add(btnAdd);
        add(BorderLayout.NORTH, top);
        add(BorderLayout.CENTER, middle);
        add(BorderLayout.SOUTH, south);

        btnEdit.addActionListener(this);
        btnAdd.addActionListener(this);
        btnRemove.addActionListener(this);
        comboBoxGenres.addActionListener(this);

        populateList();
        list.setPreferredSize(new Dimension(550, 600));

        setVisible(true);

        txtTitle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                search.setTitle(txtTitle.getText());
                populateList();
            }
        });
    }

    public static void main(String[] args) {
        try {
            new MainMenu();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }


    public int getMovieID() {
        movieList = search.filterResults();
        return movieList.get(list.getSelectedIndex()).id();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        MyWindow.setHasOpenMainMenu(true);
        if (e.getSource() == btnEdit) editMovie();
        if (e.getSource() == btnAdd) new AddMovieForm(this);
        if (e.getSource() == btnRemove) removeMovie();
        if (e.getSource() == comboBoxGenres) {
            search.setGenre(comboBoxGenres.getSelectedItem().toString());
            populateList();
        }

    }

    private void editMovie() {
        if (list.isSelectionEmpty()) {
            showMovieRequiredMessage();
            return;
        }

        MovieInfo.setMovieID(getMovieID());
        new EditMovieForm(MainMenu.this);
    }
    private void showMovieRequiredMessage(){
        Messages.showErrorMessage("Movie required!", "Please select a movie!");
    }

    private void removeMovie() {

        if (list.isSelectionEmpty()) {
            showMovieRequiredMessage();
            return;
        }

        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this movie?") == JOptionPane.YES_OPTION) {

            db.delete("movies", "movie_id", getMovieID());
            model.remove(list.getSelectedIndex());
        }
    }


    public void populateList() {
        model.clear();
        for (var movie : search.filterResults()) {
            String content = MessageFormat.format("{0}    {1}", movie.title(), movie.genres());
            model.addElement(content);

        }


    }


}