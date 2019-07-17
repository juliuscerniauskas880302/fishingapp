package strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Logbook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileSavingStrategy implements SavingStrategy {
    private static final Logger LOG = LogManager.getLogger(FileSavingStrategy.class);

    private String path;

    public FileSavingStrategy(String path) {
        this.path = path;
    }

    @Override
    public void save(Logbook logbook) {
        try {
            writeToFile(logbook);
        } catch (IOException ex) {
            LOG.error("Error occurred saving Logbook '{}' to file {}.", logbook.getId(), path);
        }
    }

    private void writeToFile(Logbook logbook) throws IOException {

        String fileName = path + getTodayDate();
        try (FileWriter file = new FileWriter(fileName)) {
            ObjectMapper mapperObj = new ObjectMapper();
            String json = mapperObj.writeValueAsString(logbook);
            file.write(json);
        }
    }

    private String getTodayDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss'.log'");
        return simpleDateFormat.format(new Date());
    }

}
