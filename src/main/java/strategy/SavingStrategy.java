package strategy;

import domain.Logbook;

import javax.ws.rs.core.Response;

public interface SavingStrategy {
    Response save(Logbook logbook);
}