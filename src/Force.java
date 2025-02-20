public class Force extends Vector{
    public Force(Vector v){
        super(v);
    }

    public Force(Rect r){
        super(r);
    }

    public Force(Polar p){
        super(p);
    }

    public Force(Acceleration a, double mass){
        super(a.getAcceleration().scale(mass));
    }

    public Force(){
        super(0,0,0,0);
    }

    public Force(Force other){
        super(other);
    }

    public Force getForce(){
        return new Force(this);
    }

    public Acceleration accel(double mass){
        return new Acceleration(this.getForce().scale(1/mass));
    }

    public static void main(String[] args){
        Force f = new Force(new Rect(15,20));
        double mass = 5/*kg*/;
        Acceleration accel = f.accel(mass);
        Vector.print(f);
        Vector.print(accel);
    }
}
