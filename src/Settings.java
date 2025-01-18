public class Settings {
    public static String InputAngleFormat = Angle.RADIANS;
    public static String OutputAngleFormat = Angle.DEGREES;
    public static int Precision = 5;
    public static String Style = Voicelines.Verbose;

    public static void setInputAngleFormat(String angleFormat) {
        InputAngleFormat = angleFormat;
    }

    public static void setOutputAngleFormat(String angleFormat) {
        OutputAngleFormat = angleFormat;
    }

    public static void setPrecision(int precision) {
        Precision = precision;
    }

    public static void setStyle(String style) {
        Style = style;
    }
}
