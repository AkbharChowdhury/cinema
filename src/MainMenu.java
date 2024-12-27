import models.Movie;
import models.MovieInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.List;

public class MainMenu extends JFrame implements ActionListener {
    DB db = DB.getInstance();
    List<Movie> movieList = db.movieList();
    Search search  = new Search(movieList);
    JButton button = new JButton("Edit");
    JButton btnAdd = new JButton("Add");
    DefaultListModel<String> model = new DefaultListModel<>();
    JList<String> list = new JList<>(model);
    JTextField textField = new JTextField(30);
    JComboBox<String> comboBox = new JComboBox<>();
    List<String> genreList = db.getMovieGenres();

    public MainMenu() {
        list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        setResizable(false);
        setLayout(new BorderLayout());
        setSize(600, 300);
        setTitle("Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        top.add(new JLabel("Movie: "));
        top.add(textField);
        top.add(new JLabel("Genre"));

        top.add(comboBox);
        comboBox.addItem("Any");
        genreList.forEach(comboBox::addItem);


        JPanel middle = new JPanel();
        middle.add(new JScrollPane(list));

        JPanel south = new JPanel();
        south.add(button);
        south.add(btnAdd);
        add(BorderLayout.NORTH, top);
        add(BorderLayout.CENTER, middle);
        add(BorderLayout.SOUTH, south);

        button.addActionListener(this);
        btnAdd.addActionListener(this);
        comboBox.addActionListener(this);

        populateList();
        list.setPreferredSize(new Dimension(550, 600));

        setVisible(true);

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                search.setTitle(textField.getText());
                populateList();
            }
        });
    }

    public static void main() {
        try {
            new MainMenu();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            MovieInfo.setMovieID(movieList.get(list.getSelectedIndex()).id());
            new EditMovieForm();
        }
        if (e.getSource() == btnAdd) {
            new AddMovie();

        }
        if (e.getSource() == comboBox){
            search.setGenre(comboBox.getSelectedItem().toString());
            populateList();
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