public class Point {
    private double x, y;
    private Rect coords;
    
    public Point(double x, double y){
        this.x = x;
        this.y = y;
        this.coords = new Rect(x,y);
        
    }
    
    public Point(){
        this.x = 0;
        this.y = 0;
        this.coords = new Rect(x,y);
    }

    public void setCoords(double x, double y){
        this.x = x;
        this.y = y;
        this.coords.setCoords(this.x, this.y);
    }
}
