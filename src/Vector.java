import static java.lang.Math.abs;

public class Vector {
    Rect recform = new Rect(0,0);
    Polar polform = new Polar(0,0);

    public Vector(Rect x) {
        this.recform = x;
        this.polform = Polar.pol(x);
    }

    public Vector(Polar x){
        this.polform = x;
        this.recform = Rect.rec(x);
    }

    public Vector(double a, double b, double c, double d){
        this.recform = new Rect(a,b);
        this.polform = new Polar(c,d);
    }

    public static Vector sum(Vector lh, Vector rh){
        Rect cur = new Rect(0,0);
        cur.x = lh.recform.x + rh.recform.x;
        cur.y = lh.recform.y + rh.recform.y;
        return new Vector(cur);
    }

    public static Vector minus(Vector lh, Vector rh){
        Rect cur = new Rect(0,0);
        cur.x = lh.recform.x - rh.recform.x;
        cur.y = lh.recform.y - rh.recform.y;
        return new Vector(cur);
    }

    public static Vector difference(Vector lh, Vector rh){
        Rect cur = new Rect(0,0);
        cur.x = abs(lh.recform.x - rh.recform.x);
        cur.y = abs(lh.recform.y - rh.recform.y);
        return new Vector(cur);
    }

    public void add(Vector other){
        this.recform.x += other.recform.x;
        this.recform.y += other.recform.y;
        this.polform = Polar.pol(this.recform);
    }

    public void subtract(Vector other){
        this.recform.x -= other.recform.x;
        this.recform.y -= other.recform.y;
        this.polform = Polar.pol(this.recform);
    }

    public static void printRec(Vector x){
        System.out.println(String.format("X: %.5f, Y: %.5f",x.recform.x,x.recform.y));
    }

    public static void printPol(Vector x){
        System.out.println(String.format("Magnitude: %.5f, Direction: %.5f radians",x.polform.mag,x.polform.theta));
    }

    public static void printPolDeg(Vector x){
        System.out.println(String.format("Magnitude: %.5f, Direction: %.5f degrees",x.polform.mag,Angle.deg(x.polform.theta)));
    }

    public static void print(Vector x){
        printRec(x);
        printPolDeg(x);
    }
}