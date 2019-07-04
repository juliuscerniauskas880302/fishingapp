package strategy;

import domain.Logbook;

import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaveStrategy implements SaveStrategy {
    private final String FILE_PATH = "C:\\datafiles\\input\\logbook.log";

    @Override
    public Response create(Logbook logbook) {
        try {
            writeToFile(logbook.toJson());
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok("Logbook saved on file").build();
    }

    private void writeToFile(JsonObject obj) throws IOException {
        try (FileWriter file = new FileWriter(FILE_PATH, true)) {
            file.write(String.valueOf(obj));
        }
    }
}
