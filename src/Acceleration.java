public class Acceleration extends Vector{
    public Acceleration(double a, double b, double c, double d) {
        super(a, b, c, d);
    }

    public Acceleration(Vector v){
        super(v);
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

    public Acceleration(Acceleration other){
        this(other.getAcceleration().recform);
    }

    public Acceleration getAcceleration(){
        return new Acceleration(this.recform);
    }
}
