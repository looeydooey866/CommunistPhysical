import java.text.DecimalFormat;

public class Settings {
    public static String InputAngleFormat = Angle.RADIANS;
    public static String OutputAngleFormat = Angle.DEGREES;
    public static String Style = Voicelines.Verbose;
    public static DecimalFormat df = new DecimalFormat("0.00000");
    public static String OSPathDelimiter = "/";

    public static void setInputAngleFormat(String angleFormat) {
        InputAngleFormat = angleFormat;
    }

    public static void setOutputAngleFormat(String angleFormat) {
        OutputAngleFormat = angleFormat;
    }

    public static void setPrecision(int precision) {
        df.setMaximumFractionDigits(precision);
    }

    public static void setStyle(String style) {
        Style = style;
    }

    public static void setOSPathDelimiter(String pathDelimiter) {OSPathDelimiter = pathDelimiter;}
}
