import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Pyplot{
    private ArrayList<String> args;
    public static final String pyplotFileName = "grapher.py";
    private int exitCode;

    public Pyplot(){
        this.args = new ArrayList<>();
        this.exitCode = 0;
    }

    /*
     *  Structure for grapher args is as follows:
     *  filename (ignored, start at 1)
     *  number of vectors
     *  vector name, vector x, vector y
     *  title
     *  display/save
     *  if save -> name (to default, pass in "<<NO_NAME>>")
     */

    @SafeVarargs
    public final void build(String... s){
        for (String x : s){
            args.add(x);
        }
    }

    @SafeVarargs
    public final void build(ArrayList<String>... a){
        for (ArrayList<String> v : a){
            for (String x : v){
                args.add(x);
            }
        }
    }

    public void run(){
        try {
            ProcessBuilder pb = new ProcessBuilder("python",pyplotFileName);
            pb.command().addAll(args);
            pb.redirectErrorStream(true);
            Process proc = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null)
                System.err.println("Python output #" + count++ + ": " + line);

            int exitCode = proc.waitFor();
            this.exitCode = exitCode;
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getExitCode(){
        return this.exitCode;
    }
}
