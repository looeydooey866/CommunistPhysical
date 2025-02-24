import java.io.*;
import java.util.*;

public class History {
    private FileWriter history;
    private File historyFile;
    private Scanner fileReader;
    public Deque<String> logs;
    private int line;

    public History(){
        this.history = null;
        this.historyFile = null;
        this.fileReader = null;
        this.logs = new ArrayDeque<>();
        this.line=0;
    }

    public History(String histFile){
        this.historyFile = new File(histFile);
        this.logs = new ArrayDeque<>();
        this.line = 0;
        if (!historyFile.exists()){
            try{
                historyFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void log(String s){
        this.logs.addLast(s);
    }

    public void write(){
        try{
            history = new FileWriter(historyFile);
            while (!logs.isEmpty()){
                history.write(logs.removeFirst() + System.lineSeparator());
            }
            history.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void clear(){
        try {
            history = new FileWriter(historyFile);
            history.write("");
            history.close();
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public String poll(){
        try{
            this.fileReader = new Scanner(historyFile);
            for (int i=0;i<line&&fileReader.hasNextLine();i++){
                fileReader.nextLine();
            }
            this.line++;
            return fileReader.nextLine();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch(Exception e){
            System.err.println(e.toString());
        }
        return "";
    }

    public String totalPoll(){
        String ret = "";
        try {
            this.fileReader = new Scanner(historyFile);
            while (fileReader.hasNextLine()){
                ret += fileReader.nextLine() + "\n";
            }
            return ret;
        } catch(Exception e){
            System.err.println(e.toString());
        }
        return "";
    }

    public String getFileName(){
        return this.historyFile.getName();
    }
}
