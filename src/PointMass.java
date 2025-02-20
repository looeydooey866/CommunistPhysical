import java.util.*;

public class PointMass{
    private int nForces = -1;
    private ArrayList<Force> forces = null;
    private double mass = 0.0;
    
    public PointMass(){
        this.nForces = 0;
        this.forces = new ArrayList<>();
        this.mass = 1.0;
    }

    public PointMass(ArrayList<Force> forces, double mass){
        this.nForces = forces.size();
        this.forces = forces;
        this.mass = mass;
    }

    public PointMass(PointMass other){
        this.nForces = other.nForces;
        this.forces = other.forces;
        this.mass = other.mass;
    }

    public PointMass(double mass){
        this.nForces = 0;
        this.forces = new ArrayList<>();
        this.mass = mass;
    }

    public void setForces(ArrayList<Force> forces){
        this.forces = forces;
        this.nForces = forces.size();
    }

    public void setMass(double mass){
        this.mass = mass;
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

    public void applyForce(Force force){
        this.forces.add(force);
        this.nForces++;
    }

    public void applyForce(Acceleration accel){
        Force f = new Force(accel,mass);
        this.applyForce(f);
    }

    public Force getResultant(){
        Force res = new Force();
        for (Force v : forces){
            res.add(v);
        }
        return res;
    }
}
