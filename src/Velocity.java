public class Velocity extends Vector{

    public Velocity(double a, double b, double c, double d) {
        super(a, b, c, d);
    }

    public Velocity(Vector v){
        super(v);
    }

    public Velocity(Polar x) {
        super(x);
    }

    public Velocity(Rect x) {
        super(x);
    }

    public Velocity(Displacement d, double T){
        super(d.getDisp().scale(1/T));
    }

    public Velocity(Acceleration avgacc, double T){
        super(avgacc.getAcceleration().scale(T));
    }
    
    public Velocity(Velocity other){ //as a constructor
        this(other.recform);
    }

    public Velocity getVelocity(){ //as a copy getter
        return new Velocity(this);
    }
}
