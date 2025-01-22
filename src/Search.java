
import lombok.Getter;
import models.Movie;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public final class Search {

    public void setList(List<Movie> list) {
        this.list = list;
    }

    @Getter
    private  List<Movie> list;


    public void setTitle(String title) {
        this.title = title;
    }


    public void setGenre(String genre) {
        this.genre = genre;
    }

    private String title = "";
    private String genre = "Any";
    private final Predicate<Movie> filterTitle = p -> StringUtils.containsIgnoreCase(p.title(), title);

    private Predicate<Movie> filterGenre() {
        return !"Any".equals(genre) ? p -> StringUtils.containsIgnoreCase(p.genres(), genre) : p -> true;
    }

    public Search(List<Movie> list) {
        this.list = list;
    }


    public List<Movie> filterResults() {
        return list.stream()
                .filter(filterTitle)
                .filter(filterGenre())
                .toList();
    }

    @Override
    public String toString() {
        return list.stream().toList().toString();
    }
}