package utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtilities {
    private static final Logger LOG = LoggerFactory.getLogger(DateUtilities.class);

    public static Date parseDateFromString(String dateString, String datePattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            LOG.error("Cannot parse string {} to date.", dateString);
        }
        return date;
    }

}
