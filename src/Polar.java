import java.util.Objects;

import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.StrictMath.abs;

public class Polar {
    private double mag = 0;
    private Angle theta = new Angle(0);
    public void setMag(double val){
        this.mag = val;
    }
    public double getMag(){
        return this.mag;
    }

    public void setThetaRad(double val){
        this.theta.setRad(val);
    }
    public void setThetaDeg(double val){this.theta.setDeg(val);}
    public void setTheta(Angle val){this.theta = val;}
    public void setThetaInput(double val){
        if (Settings.InputAngleFormat.equals(Angle.DEGREES)){
            this.setThetaDeg(val);
        }
        else if (Settings.InputAngleFormat.equals(Angle.RADIANS)){
            this.setThetaRad(val);
        }
    }

    public Angle getTheta(){
        return this.theta;
    }

    public Polar(double a, double b) {
        this.mag = a;
        this.theta.setRad(b);
    }

    public void addWith(Polar other){
        Rect cur = Rect.rec(this);
        Rect add = Rect.rec(other);
        cur.addWith(add);
        Polar res = pol(cur);
        this.setMag(res.getMag());
        this.setTheta(res.getTheta());
    }

    public void subtractWith(Polar other){
        Rect cur = Rect.rec(this);
        Rect add = Rect.rec(other);
        cur.subtractWith(add);
        Polar res = pol(cur);
        this.setMag(res.getMag());
        this.setTheta(res.getTheta());
    }

    public static Polar pol(Rect x){
        Polar cur = new Polar(0,0);
        cur.mag = x.getMagnitude();
        double theta = atan(abs(x.getY())/abs(x.getX()));
        double res;
        if (x.getY() >= 0){
            if (x.getX() >= 0){//quadrant 1
                res = theta;
            }
            else {//quadrant 2
                res = Math.PI - theta;
            }
        }
        else {
            if (x.getX() <= 0){//quadrant 3
                res = Math.PI + theta;
            }
            else {//quadrant 4
                res = Math.PI*2-theta;
            }
        }
        cur.theta.setRad(res);
        return cur;
    }

    public double getY(){
        return this.getMag() * Math.sin(this.theta.getRad());
    }

    public double getX(){
        return this.getMag() * Math.cos(this.theta.getRad());
    }

    public Polar scalarMult(double x){
        Polar cur = this;
        cur.setMag(cur.getMag() * abs(x));
        if (x < 0){
            cur.setThetaRad(cur.getTheta().getRad()+Math.PI);
        }
        return cur;
    }
}


