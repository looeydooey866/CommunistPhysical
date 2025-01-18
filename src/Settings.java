public class Settings {
    public static String InputAngleFormat = "rad";
    public static String OutputAngleFormat = "deg";
    public static int Precision = 5;
    public static boolean Verbose = true;

    public static void setInputAngleFormat(String angleFormat) {
        InputAngleFormat = angleFormat;
    }

    public static void setOutputAngleFormat(String angleFormat) {
        OutputAngleFormat = angleFormat;
    }

    public static void setPrecision(int precision) {
        Precision = precision;
    }

    public static void setVerbose(boolean verbose) {
        Verbose = verbose;
    }
}
