import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class printFormatter extends Formatter {

    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

    public String format(LogRecord record) {
        StringBuilder formattedString = new StringBuilder(1000);
        formattedString.append("|").append(record.getSourceClassName()).append(".");
        formattedString.append(record.getSourceMethodName()).append("| - ");
        formattedString.append(df.format(new Date(record.getMillis()))).append(" - ");
        formattedString.append(formatMessage(record));
        formattedString.append("\n");
        return formattedString.toString();
    }

}