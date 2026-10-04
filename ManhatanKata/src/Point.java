public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    //Math.abs te da el valor absoluto
    public static int manhattanDistance(Point a, Point b){
        int resultado=Math.abs(a.x-b.x)+Math.abs(b.y-a.y);
        return resultado;
    }
}
