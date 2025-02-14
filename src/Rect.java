import static java.lang.Math.sqrt;

public class Rect {
    private double x = 0;
    private double y = 0;
    
    public Rect(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Rect(Rect r){
        this.x = r.x;
        this.y = r.y;
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
        cur.x = x.getX();
        cur.y = x.getY();
        return cur;
    }


    public void addWith(Rect other){
        this.setX(this.getX() + other.getX());
        this.setY(this.getY() + other.getY());
    }

    public void subtractWith(Rect other){
        this.setX(this.getX() - other.getX());
        this.setY(this.getY() - other.getY());
    }


    public Rect scalarMult(double x){
        Rect cur = this;
        cur.setX(this.getX() * x);
        cur.setY(this.getY() * x);
        return cur;
    }
}
