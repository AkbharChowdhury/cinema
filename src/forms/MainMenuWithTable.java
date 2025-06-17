package forms;

import dbs.Database;
import models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class MainMenuWithTable extends JFrame implements ActionListener {
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

    JTable table = new JTable();
    DefaultTableModel model2 = (DefaultTableModel) table.getModel();


    public MainMenuWithTable() {

        // table prop

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);

        List<String> columns = new ArrayList<>();
        columns.add("Title");
        columns.add("Genres");

        columns.forEach(model2::addColumn);

//        list.forEach(model2::addColumn);


        list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        List<String> genreList = new ArrayList<>(db.getMovieGenres());
        genreList.addFirst("Any");
        comboBoxGenres.setModel(new DefaultComboBoxModel<>(new Vector<>(genreList)));
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
//        middle.add(new JScrollPane(list));
        middle.add(new JScrollPane(scrollPane));


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
            new MainMenuWithTable();
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
        new EditMovieForm(MainMenuWithTable.this);
    }

    private void showMovieRequiredMessage() {
        Messages.showErrorMessage("Movie required!", "Please select a movie!");
    }

    private void removeMovie() {

        if (list.isSelectionEmpty()) {
            showMovieRequiredMessage();
            return;
        }

        if (Messages.hasConfirmed.apply("Are you sure you want to remove this movie?")) {
            db.delete("movies", "movie_id", getMovieID());
            model.remove(list.getSelectedIndex());
            search.setList(db.getMovieList());
        }
    }


    //    public void populateList() {
//        model.clear();
//        for (var movie : search.filterResults()) {
//            String content = MessageFormat.format("{0}    {1}", movie.title(), movie.genres());
//            model.addElement(content);
//
//        }
//
//
//    }
    void populateList() {
        ((DefaultTableModel) table.getModel()).setRowCount(0);
        List<Movie> movies = search.filterResults();
        int movieSize = movies.size();
        for (int i = 0; i < movieSize; i++) {
            Movie movie = movies.get(i);
            model2.addRow(new Object[0]);
            model2.setValueAt(movie.title(), i, 0);
            model2.setValueAt(movie.genres(), i, 1);


        }

    }


}


