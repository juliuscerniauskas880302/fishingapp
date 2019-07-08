package strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.ResponseMessage;
import domain.Logbook;

import javax.ws.rs.core.Response;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileSavingStrategy implements SavingStrategy {
    private String path;

    public FileSavingStrategy(String path) {
        this.path = path;
    }

    @Override
    public Response save(Logbook logbook) {
        try {
            writeToFile(logbook);
        } catch (IOException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessage("Error accrued saving logbook: "
                    + logbook.toString()
                    + " to file: "
                    + path)).build();
        }
        return Response.status(Response.Status.OK).entity(new ResponseMessage("Logbook saved to file")).build();
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
