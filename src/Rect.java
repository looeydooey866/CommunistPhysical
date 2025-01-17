import static java.lang.Math.sqrt;

public class Rect {
    public double x;
    public double y;

    public Rect(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getMagnitude(){
        return sqrt(x*x + y*y);
    }

    public static Rect rec(Polar x){
        Rect cur = new Rect(0,0);
        cur.x = Math.cos(x.theta)*x.mag;
        cur.y = Math.sin(x.theta)*x.mag;
        return cur;
    }
}