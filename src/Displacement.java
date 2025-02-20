public class Displacement extends Vector{
    public Displacement(double a, double b, double c, double d) {
        super(a, b, c, d);
    }

    public Displacement(Vector v){
        super(v);
    }

    public Displacement(Polar x) {
        super(x);
    }

    public Displacement(Rect x) {
        super(x);
    }

    public Displacement(Point pi, Point pf){
        super(new Rect(pf.getX()-pi.getX(), pf.getY()-pi.getY()));
    }

    public Displacement(Velocity avgV, double T){
        super(avgV.getVelocity().scale(T));
    }

    public Displacement(Displacement other){
        this(other.recform);
    }

    public Displacement getDisp(){
        return new Displacement(this);
    }


    public static void main(String[] args){
        Point st = new Point(1,3);
        Point end = new Point(7,1);
        Displacement s = new Displacement(st,end);
        Vector.print(s.getDisp());

        System.out.println("=================");
        Displacement s2 = new Displacement(s);
        Vector.print(s2.getDisp());
        //test
        Displacement x = new Displacement(s2);
    }
}
