public class Angle {
    public static double rad(double x){
        return x / (180 / Math.PI);
    }

    public static double deg(double x){
        return x * (180 / Math.PI);
    }
}
