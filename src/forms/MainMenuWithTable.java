package forms;

import dbs.Database;
import enums.MovieEnum;
import models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public class MainMenuWithTable extends JFrame implements ActionListener {
    Database db = Database.getInstance();
    List<Movie> movieList = db.getMovieList();
    SearchMovies search = new SearchMovies(movieList);


    JButton btnEdit = new JButton("Edit");
    JButton btnAdd = new JButton("Add");
    JButton btnRemove = new JButton("Remove");

    JTextField txtTitle = new JTextField(40);
    JComboBox<String> comboBoxGenres = new JComboBox<>();

    JTable table = new JTable();

    DefaultTableModel tableModel = new DefaultTableModel() {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };


    void tableProp() {

        table.setModel(tableModel);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setPreferredSize(new Dimension(550, 600));

        List<String> columns = new ArrayList<>();
        columns.add("Title");
        columns.add("Genres");
        columns.forEach(tableModel::addColumn);
    }

    JScrollPane tableScrollPane() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        return scrollPane;
    }


    public MainMenuWithTable() {

        tableProp();

        comboBoxGenres.setModel(new DefaultComboBoxModel<>(new Vector<>(getGenres())));
        setResizable(true);
        setLayout(new BorderLayout());
        setSize(800, 500);
        setTitle("Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel top = new JPanel();
        top.add(new JLabel("Movie: "));
        top.add(txtTitle);
        top.add(new JLabel("Genre"));
        top.add(comboBoxGenres);

        JPanel middle = new JPanel();
        middle.add(new JScrollPane(tableScrollPane()));
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
        MyButton.handCursor.accept(List.of(btnEdit, btnAdd, btnRemove));
        comboBoxGenres.addActionListener(this);


        populateList();
        setVisible(true);

        txtTitle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                search.setTitle(txtTitle.getText());
                populateList();
            }
        });
    }

    private List<String> getGenres() {
        List<String> genres = new ArrayList<>(db.getMovieGenres());
        genres.addFirst("Any");
        return genres;
    }

    public static void main(String[] args) {
        try {
            new MainMenuWithTable();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

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

    private boolean isSelectionRequired() {
        return table.getSelectedRow() == -1;
    }

    private void editMovie() {

        if (isSelectionRequired()) {
            showMovieRequiredMessage();
            return;
        }
        MovieInfo.setMovieID(getSelectedMovieID());
        new EditMovieForm(MainMenuWithTable.this);
    }

    int getSelectedMovieID() {
        int selectedIndex = table.getSelectedRow();
        movieList = search.filterResults();
        return movieList.get(selectedIndex).id();
    }

    private void showMovieRequiredMessage() {
        Messages.showErrorMessage("Movie required!", "Please select a movie!");
    }

    private void removeMovie() {

        if (isSelectionRequired()) {
            showMovieRequiredMessage();
            return;
        }

        if (Messages.hasConfirmed.apply("Are you sure you want to remove this movie?")) {
            db.delete("movies", "movie_id", getSelectedMovieID());
            tableModel.removeRow(table.getSelectedRow());
            search.setList(db.getMovieList());
        }
    }


    void populateList() {
        ((DefaultTableModel) table.getModel()).setRowCount(0);
        List<Movie> movies = search.filterResults();
        int movieSize = movies.size();
        for (int i = 0; i < movieSize; i++) {
            Movie movie = movies.get(i);
            tableModel.addRow(new Object[0]);
            tableModel.setValueAt(movie.title(), i, MovieEnum.TITLE.getValue());
            tableModel.setValueAt(movie.genres(), i, MovieEnum.GENRE.getValue());


        }

    }


}


