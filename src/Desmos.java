public class Desmos{
    private String graph;
    private final String label = "V_{%s}=\\ \\left(%s\\cos %s,%s\\sin %s\\right)\n";
    private final String vec = "y\\cos %f+x\\sin %f=\\left(x\\cos %f-y\\sin %f\\right)\\left\\{y\\%ce0\\right\\}\\left\\{x^{2}+y^{2}\\le%f^{2}\\right\\}\n";
    public Desmos(){
        this.graph = "";
    }

    public void insertLabel(String name, double theta, double magnitude){
        graph += String.format(label,name,magnitude,theta,magnitude,theta);
    }

    public void insertLabel(String name, Vector v){
        insertLabel(name, v.polform.getTheta().getRad(), v.polform.getMag());
    }

    public void insertVec(String name, double theta, double magnitude){
        double a = Math.PI / 4 - theta; // for matrix mult
        graph += String.format(vec, a, a, a, a, (theta <= Math.PI ? 'g' : 'l'), magnitude);
    }

    public void insertVec(String name, Vector v){
        insertVec(name, v.polform.getTheta().getRad(), v.polform.getMag());
    }

    public void display(){
        System.out.println(graph);
    }

    public void clear(){
        this.graph = "";
    }
}
