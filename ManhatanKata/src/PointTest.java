import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
class PointTest {
    @Test
    void distanciaEntreDosPuntosEnAmbosEjes() {
        Point a = new Point(1, 2);
        Point b = new Point(4, 6);
        assertEquals(7, Point.manhattanDistance(a, b));
    }
    @Test
    void mismoPunto(){
        Point a=new Point(3,3);
        Point b=new Point(3,3);
        assertEquals(0,Point.manhattanDistance(a,b));
    }
    @Test
    void soloCambiaX(){
        Point a=new Point(1,5);
        Point b=new Point(4,5);
        assertEquals(3,Point.manhattanDistance(a,b));
    }
    @Test
    void soloCambiaY(){
        Point a=new Point(2,1);
        Point b=new Point(2,7);
        assertEquals(6,Point.manhattanDistance(a,b));
    }
    @Test
    void coordenadasNegativas(){
        Point a=new Point(-2,-3);
        Point b=new Point(1,1);
        assertEquals(7,Point.manhattanDistance(a,b));
    }
    @Test
    void cruzando(){
        Point a=new Point(-5,4);
        Point b=new Point(5,-4);
        assertEquals(18,Point.manhattanDistance(a,b));
    }




}