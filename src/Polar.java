import static java.lang.Math.atan;

public class Polar {
    private double mag;
    private double theta;
    public void setMag(double val){
        this.mag = val;
    }
    public double getMag(){
        return this.mag;
    }

    public void setTheta(double val){
        this.theta = val;
    }
    public double getTheta(){
        return this.theta
    }
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


