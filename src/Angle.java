public class Angle {
    public static final String RADIANS = "rad";
    public static final String DEGREES = "deg";
    
    private double rad;

    public void normalize() {
        while (this.getRad() < 0){
            this.add(Math.PI*2);
        }
        while (this.getRad() > Math.PI * 2){
            this.subtract(Math.PI*2);
        }
    }

    public double getRad(){
        return this.rad;
    }

    public double getDeg(){
        return this.rad * (180 / Math.PI);
    }
    public void setRad(double val){
        this.rad = val;
        normalize();
    }

    public void setDeg(double val){
        this.rad = val / (180 / Math.PI);
        normalize();
    }
    
    public Angle(Polar polska){
        this.rad = polska.getTheta().getRad();
        normalize();
    }

    public Angle(double x){
        this.rad = x;
        normalize();
    }

    public static double convertToRad(double x){
        return x / (180 / Math.PI);
    }

    public static double convertToDeg(double x){
        return x / (Math.PI / 180);
    }

    public void add(double x){
        this.setRad(this.getRad() + x);
        normalize();
    }

    public void subtract(double x){
        this.setRad(this.getRad() - x);
        normalize();
    }
}
