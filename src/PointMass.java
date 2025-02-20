public class PointMass{
    private String name = null;
    private int nForces = -1;
    private ArrayList<Vector> forces = null;
    
    public PointMass(){
        this.name = "";
        this.nForces = 0;
        this.forces = new ArrayList<>();
    }

    public PointMass(String name){
        this.name = name;
    }

    public PointMass(String name, ArrayList<Vector> forces){
        this.name = name;
        this.nForces = forces.size()
        this.forces = forces;
    }

    public PointMass(PointMass other){
        this.name = other.name;
        this.nForces = other.nForces;
        this.forces = other.forces;
    }

    public void setName(String s){
        this.name = s;
    }

    public void setForces(ArrayList<Vector> forces){
        this.forces = forces;
        this.nForces = forces.size();
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<Vector> getForces(){
        return this.forces;
    }

    public int getN(){
        return this.nForces;
    }

    public void applyForce(Vector force){
        this.forces.add(force);
        this.nForces++;
    }

    public Vector getResultant(){
        Vector res = new Vector(0,0,0,0);
        for (Vector v : forces){
            res.add(v);
        }

        return res;
    }
}
