public class Force extends Vector{
    private String name = "force f";

    public void setName(String s){
        if (s == null){
            this.name = "force f";
        }
        else {
            this.name = s;
        }
    }

    public String getName(){return this.name;}

    public Force(String s, Vector v){
        super(v);
        this.setName(s);
    }

    public Force(String s,Rect r){
        super(r);
        this.setName(s);
    }

    public Force(String s, Polar p){
        super(p);
        this.setName(s);
    }

    public Force(String s, Acceleration a, double mass){
        super(a.getAcceleration().scale(mass));
        this.setName(s);
    }

    public Force(){
        super(0,0,0,0);
        this.setName(null);
    }

    public Force(Force other){
        this(other.getName(),other.recform);
    }

    public Force(String s){
        super(0,0,0,0);
        this.setName(s);
    }

    public Force getForce(){
        return new Force(this);
    }

    public Acceleration accel(double mass){
        return new Acceleration(this.getForce().scale(1/mass));
    }

    public static void main(String[] args){
        Force f = new Force("Wind",new Rect(15,20));
        double mass = 5/*kg*/;
        Acceleration accel = f.accel(mass);
        Vector.print(f);
        Vector.print(accel);
    }
}
