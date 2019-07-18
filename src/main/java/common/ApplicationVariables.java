package common;

public class ApplicationVariables {
    /**
     * Controller paths
     */
    public static final String APPLICATION_PATH = "api";
    public static final String CATCH_PATH = "/catches";
    public static final String CONFIG_PATH = "/configs";
    public static final String DEPARTURE_PATH = "/departures";
    public static final String END_OF_FISHING_PATH = "/ends";
    public static final String LOGBOOK_PATH = "/logs";
    public static final String ARRIVAL_PATH = "/arrivals";

    /**
     * Properties file path
     */
    public static final String PROPERTIES_FILE_PATH = "classpath:application.properties";

    /**
     * Date patterns
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * URI
     */
    public static final String HTTP_LOGS_URI = "http://localhost:8080/fishingapp/api/logs/";
    public static final String HTTP_LOGS_LIST_URI = "http://localhost:8080/fishingapp/api/logs/logs/";
}
