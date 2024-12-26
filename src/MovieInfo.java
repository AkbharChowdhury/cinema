import lombok.Getter;
import lombok.Setter;
public class MovieInfo {
    public static int getMovieID() {
        return movieID;
    }

    public static void setMovieID(int movieID) {
        MovieInfo.movieID = movieID;
    }

    @Getter
    @Setter
    private static int movieID;
    private MovieInfo(){

    }
}
