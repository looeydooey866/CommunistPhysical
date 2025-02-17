public class History {
    private String history;

    public History(){
        this.history = "";
    }

    public void log(String s){
        this.history += s + System.lineSeparator();
    }

    public String poll(){
        return this.history + System.lineSeparator();
    }
}
