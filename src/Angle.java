public class Angle {
    public static final String RADIANS = "rad";
    public static final String DEGREES = "deg";
    
    private double rad;

    public double getRad(){
        return this.rad;
    }

    public double getDeg(){
        return this.rad * (180 / Math.PI);
    }
    public void setRad(double val){
        this.rad = val;
    }

    public void setDeg(double val){
        this.rad = val * (180 / Math.PI);
    }
    
    public Angle(Polar polska){
        this.rad = polska.getTheta();
        
    }
    
    public static double toRad(Angle x){
        return x.getDeg() / (180 / Math.PI);
    }

    public static double toDeg(Angle x){
        return x.getRad() * (180 / Math.PI);
    }
}
