package strategy;

import domain.Logbook;

import javax.ws.rs.core.Response;

public class SaveContext {
    private SaveStrategy saveStrategy;

    public void setSaveStrategy(SaveStrategy saveStrategy) {
        this.saveStrategy = saveStrategy;
    }

    public Response save(Logbook logbook) {
        return saveStrategy.create(logbook);
    }
}
