import static java.lang.Math.sqrt;

public class Rect {
    private double x;
    private double y;
    
    public Rect(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setX(double val){
        this.x = val;
    }
    
    public void setY(double val){
        this.y = val;
    }

    public void setCoords(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }
    
    public double getMagnitude(){
        return sqrt(x*x + y*y);
    }

    public static Rect rec(Polar x){
        Rect cur = new Rect(0,0);
        cur.x = Math.cos(x.getTheta())*x.getMag();
        cur.y = Math.sin(x.getTheta())*x.getMag();
        return cur;
    }
}
