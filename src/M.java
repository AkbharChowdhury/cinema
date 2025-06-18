import dbs.Database;
import enums.MovieEnum;

import java.util.Collections;
import java.util.List;

public class M {
    public static void main(String[] args) {
//        Database db = Database.getInstance();
//        System.out.println(db.getAllGenres());
        var listToSort = new java.util.ArrayList<>(List.of("Adventure", "action"));
        Collections.sort(listToSort, String.CASE_INSENSITIVE_ORDER);
        var x = List.of("Adventure", "action").stream().sorted((a,b) -> a.compareToIgnoreCase(b));
        System.out.println(listToSort);
    }
}
