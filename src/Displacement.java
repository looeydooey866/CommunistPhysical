public class Displacement extends Vector{
    public Displacement(double a, double b, double c, double d) {
        super(a, b, c, d);
    }

    public Displacement(Polar x) {
        super(x);
    }

    public Displacement(Rect x) {
        super(x);
    }

    public Displacement(Point pI, Point pF){
        super(new Rect(pF.getX()-pI.getY(), pF.getY()-pI.getY()));
    }

    public Displacement(Velocity avgV, double T){
        super(avgV.getVelocity().scalarMult(T));
    }

    

    public Rect getDisp(){
        return this.recform;
    }

}
