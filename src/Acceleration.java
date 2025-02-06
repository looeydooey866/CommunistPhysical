public class Acceleration extends Vector{
    public Acceleration(double a, double b, double c, double d) {
        super(a, b, c, d);
    }

    public Acceleration(Polar x) {
        super(x);
    }

    public Acceleration(Rect x) {
        super(x);
    }
    public Acceleration(Velocity v, double T){
        super(v.getVelocity().scalarMult(1/T));
    }

    public Rect getAcceleration(){
        return this.recform;
    }
}
