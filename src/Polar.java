import static java.lang.Math.atan;

public class Polar {
    public double mag;
    public double theta;

    public Polar(double a, double b) {
        this.mag = a;
        this.theta = b;
    }

    public static Polar pol(Rect x){
        Polar cur = new Polar(0,0);
        cur.mag = x.getMagnitude();
        cur.theta = atan(x.y / x.x);
        return cur;
    }
}


