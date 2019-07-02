package strategy;

import domain.Logbook;
import org.json.simple.JSONObject;

import javax.ws.rs.core.Response;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaveStrategy implements SaveStrategy {
    @Override
    public Response create(Logbook logbook) {
        JSONObject obj = new JSONObject();
        obj.put(Logbook.class.getCanonicalName(), logbook.toJson());
        try {
            writeToFile(obj);
        } catch (IOException e) {
            return Response.serverError().build();
        }
        return Response.ok("Logbook saved in file").build();
    }

    private void writeToFile(JSONObject obj) throws IOException {
        try (FileWriter file = new FileWriter("/logbook.txt")) {
            file.write(obj.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + obj);
        }
    }
}
