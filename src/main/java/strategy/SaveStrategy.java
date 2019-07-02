package strategy;

import domain.Logbook;

import javax.ws.rs.core.Response;

public interface SaveStrategy {
    Response create(Logbook logbook);
}
