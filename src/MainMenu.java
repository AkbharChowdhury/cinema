import models.Movie;
import models.MovieInfo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.List;

public class MainMenu extends JFrame implements ActionListener {
    DB db = DB.getInstance();
    List<Movie> movieList = db.movieList();
    JButton button = new JButton("Edit models.Movie");
    JButton btnAdd = new JButton("Add models.Movie");
    int selectedListInvoiceItem;
    DefaultListModel<String> model = new DefaultListModel<>();
    JList<String> list = new JList<>(model);


    public MainMenu() {
        list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener((ListSelectionEvent _) -> selectedListInvoiceItem = list.getSelectedIndex());

        setResizable(false);
        setLayout(new BorderLayout());
        setSize(600, 300);
        setTitle("Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();

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

        populateList();
        list.setPreferredSize(new Dimension(550, 600));

        setVisible(true);
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
    }


    public void populateList() {
        for (var movie : movieList) {
            String content = MessageFormat.format("{0}    {1}", movie.title(), movie.genres());
            model.addElement(content);
        }

    }


}