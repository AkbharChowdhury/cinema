public class TestA {
    public static void main(String[] args) {
        Database d = Database.getInstance();
        System.out.println(d.getMovieName(44));
    }
}
