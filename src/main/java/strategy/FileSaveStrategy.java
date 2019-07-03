package strategy;

import domain.Logbook;

import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaveStrategy implements SaveStrategy {
    @Override
    public Response create(Logbook logbook) {
        try {
            writeToFile(logbook.toJson());
        } catch (IOException e) {
            return Response.status(404).build();
        }
        return Response.ok("Logbook saved on file").build();
    }

    private void writeToFile(JsonObject obj) throws IOException {
        try (FileWriter file = new FileWriter("C:\\Users\\julius.cerniauskas\\Desktop\\logbook.log")) {
            file.write(String.valueOf(obj));
        }
    }
}
