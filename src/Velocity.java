public class Velocity extends Vector{

    public Velocity(double a, double b, double c, double d) {
        super(a, b, c, d);
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

    public Velocity getVelocity(){
        return this;
    }

    public static void main(String[] args){
    }
}
