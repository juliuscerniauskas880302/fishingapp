package strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.ResponseMessage;
import domain.Logbook;

import javax.ws.rs.core.Response;
import java.io.FileWriter;
import java.io.IOException;

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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ResponseMessage("Error accrued saving logbook: "
                    + logbook.toString()
                    + " to file: "
                    + path)).build();
        }
        return Response.status(Response.Status.OK).entity(new ResponseMessage("Logbook saved to file")).build();
    }

    private void writeToFile(Logbook logbook) throws IOException {
        try (FileWriter file = new FileWriter(path)) {
            ObjectMapper mapperObj = new ObjectMapper();
            String json = mapperObj.writeValueAsString(logbook);
            file.write(json);
        }
    }
}
