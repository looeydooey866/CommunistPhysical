import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class History {
    private FileWriter history;
    private File historyFile;

    public History(){
        this.history = null;
        this.historyFile = null;
    }

    public History(String histFile){
        this.historyFile = new File(histFile);
        try{
            historyFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void log(String s){
        try{
            history = new FileWriter(historyFile);
            history.write(s+System.lineSeparator());
            history.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String poll(){
        try{
            Scanner fileReader = new Scanner(historyFile);
            return fileReader.nextLine();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return "";
    }
}
