import java.util.*;

public class PointMass{
    private int nForces = 0;
    private ArrayList<Force> forces = new ArrayList<>();
    private double mass = 1.0;
    
    public PointMass(int nForces, ArrayList<Force> forces, double mass){
        assert(nForces == forces.size());
        this.nForces = nForces;
        this.forces = forces;
        this.mass = mass;
    }

    public PointMass(ArrayList<Force> forces, double mass){
        this(forces.size(),forces,mass);
    }

    public PointMass(PointMass other){
        this(other.nForces,other.forces,other.mass);
    }

    public PointMass(double mass){
        this(0,new ArrayList<>(),mass);
    }

    public void setForces(ArrayList<Force> forces){
        this.nForces = forces.size();
        this.forces = forces;
    }

    public void setMass(double mass){
        this.mass = mass; //how is the mass of a particle going to change?
                          //either way, this should not be changed as if an acceleration is applied before the mass change, this will be erroneous.
                          //why did i just get mad at myself
    }

    public ArrayList<Force> getForces(){
        return this.forces;
    }

    public int getN(){
        return this.nForces;
    }

    public double getMass(){
        return this.mass;
    }

    public PointMass getPointMass(){
        return new PointMass(this.nForces,this.forces,this.mass);
    }

    public void applyForce(Force force){
        this.forces.add(force);
        this.nForces++;
    }

    public void applyForce(String name, Acceleration accel){
        Force f = new Force(name, accel,mass);
        this.applyForce(f);
    }

    public void applyForce(Acceleration accel){
        this.applyForce("a",accel);
    }

    public Force resultant(String name){
        Force res = new Force(name);
        for (Force v : forces){
            res.add(v);
        }
        return res;
    }

    public Force resultant(){
        return resultant("∑f");
    }
}
