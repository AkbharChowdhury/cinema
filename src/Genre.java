import java.awt.*;
import java.util.List;

public record Genre(int id, String type) {
    public static List<Genre> getSelectedGenres(List<Checkbox> checkboxes, List<Genre> list){
        List<String> selectedGenres = checkboxes.stream().filter(Checkbox::getState).map(Checkbox::getLabel).toList();
        return list.stream()
                .filter(genre -> selectedGenres.stream().anyMatch(label -> label.equals(genre.type())))
                .toList();
    }
}
