package ejb;

import domain.Logbook;

import javax.xml.ws.Response;

public interface SaveStrategy {
    Response<?> save(Logbook logbook);
}
