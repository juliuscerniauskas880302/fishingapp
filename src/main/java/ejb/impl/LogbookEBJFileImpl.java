package ejb.impl;

import domain.Logbook;
import ejb.LogbookEJB;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class LogbookEBJFileImpl {

    public List<Logbook> findAll() {
        return null;
    }


    public Logbook findById(Long id) {
        return null;
    }


    public Response create(Logbook logbook) {
        try {
            writeToFile(logbook.toJson());
        } catch (IOException e) {
            return Response.serverError().build();
        }
        return Response.ok("Logbook saved in file").build();
    }


    public Response update(Long id, Logbook logbook) {
        return null;
    }


    public Response remove(Long id) {
        return null;
    }

    private void writeToFile(JsonObject obj) throws IOException {
        try (FileWriter file = new FileWriter("/logbook.txt")) {
            file.write(String.valueOf(obj));
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + obj);
        }
    }
}
