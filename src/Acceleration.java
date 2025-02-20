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
        super(v.getVelocity().scale(1/T));
    }

    public Acceleration getAcceleration(){
        return this;
    }
}
