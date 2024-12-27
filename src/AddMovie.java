import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

public class AddMovie extends JFrame implements ActionListener {
    DB db = DB.getInstance();
    JTextField txtTitle = new JTextField(20);
    String[] genres = db.genres().toArray(new String[0]);
    JButton btnAdd = new JButton("AddMovie");
    List<Checkbox> checkboxes;

    public AddMovie() {

        setTitle("Add Movie");

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        JPanel middle = new JPanel();

        JPanel top = new JPanel();
        top.add(new JLabel("models.Movie"));
        top.add(txtTitle);
        middle.setLayout(new GridLayout(genres.length, 2));

        addGenres(middle);

        content.add(top, BorderLayout.NORTH);
        content.add(middle, BorderLayout.CENTER);
        content.add(btnAdd, BorderLayout.SOUTH);

        setContentPane(content);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        btnAdd.addActionListener(this);
        autofocus();
        setVisible(true);

    }

    private void addGenres(JPanel middle) {
        List<String> genreLabelsSorted = Arrays.stream(genres).sorted().toList();
        checkboxes = genreLabelsSorted.stream().map(Checkbox::new).toList();
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
        if (e.getSource() == btnAdd) {
            boolean hasSelectedGenre = checkboxes.stream().anyMatch(Checkbox::getState);

            if (!hasSelectedGenre) {
                JOptionPane.showMessageDialog(null, "Please choose a genre");
                return;
            }

            List<String> selectedGenres = checkboxes.stream().filter(Checkbox::getState).map(Checkbox::getLabel).toList();
            String genreFormatted = String.join("|", selectedGenres);
            System.out.println(txtTitle.getText() + "  " + genreFormatted);
        }
    }



    public static void main() {
        new AddMovie();
    }

}

