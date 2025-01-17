import static java.lang.Math.atan;

public class Polar {
    private double mag;
    private Angle theta;
    public void setMag(double val){
        this.mag = val;
    }
    public double getMag(){
        return this.mag;
    }

    public void setTheta(double val){
        this.theta.setRad(val);
    }
    public double getTheta(){
        return this.theta.getRad();
    }
    public Polar(double a, double b) {
        this.mag = a;
        this.theta.setRad(b);
    }

    public static Polar pol(Rect x){
        Polar cur = new Polar(0,0);
        cur.mag = x.getMagnitude();
        cur.theta.setRad(atan(x.y / x.x));
        return cur;
    }
}


