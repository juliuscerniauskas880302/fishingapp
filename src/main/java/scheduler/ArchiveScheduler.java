package scheduler;

import dto.logbook.LogbookGetDTO;
import service.archive.ArchiveService;
import service.logbook.LogbookService;
import utilities.ObjectMapper;

import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class ArchiveScheduler {

    @Inject
    private LogbookService logbookService;
    @Inject
    private ArchiveService archiveService;

    ObjectMapper<LogbookGetDTO> mapper = new ObjectMapper<>();

    @Schedules({
            @Schedule(dayOfWeek = "Fri", hour = "12")
    })
    public void moveInactiveLogbooksToArchive() {
        System.out.println("Trying to add unused logs to archive");
        logbookService.findAllInactiveLogbooks().stream().forEach(logbookGetDTO -> {
            String serialized = mapper.serialize(logbookGetDTO);
            archiveService.archive(serialized);
            logbookService.deleteById(logbookGetDTO.getId());
        });
    }
}
