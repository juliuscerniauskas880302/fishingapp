package scheduler;

import common.ApplicationVariables;
import dto.logbook.LogbookGetDTO;
import io.xlate.inject.Property;
import io.xlate.inject.PropertyResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.archive.ArchiveService;
import service.logbook.LogbookService;
import utilities.ObjectMapper;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class ArchiveScheduler {
    private static final Logger LOG = LogManager.getLogger(ArchiveScheduler.class);

    @Inject
    private LogbookService logbookService;
    @Inject
    private ArchiveService archiveService;

    ObjectMapper<LogbookGetDTO> mapper = new ObjectMapper<>();

    @Inject
    @Property(name = "scheduler.logbook.daysOld",
            resource = @PropertyResource(ApplicationVariables.PROPERTIES_FILE_PATH),
            defaultValue = "356")
    private int daysOld;

    @Schedule(dayOfWeek = "Mon", minute = "*/30", hour = "22,23")
    public void moveInactiveLogbooksToArchive() {
        LOG.info("Starting inactive/old logbook cleanup...");
        logbookService.findAllInactiveOrOldLogbooks(daysOld).stream().forEach(logbookGetDTO -> {
            String serialized = mapper.serialize(logbookGetDTO);
            archiveService.archive(serialized);
            logbookService.deleteById(logbookGetDTO.getId());
            LOG.info("Logbook {} has been archived and deleted from logbook table.", logbookGetDTO.getId());
        });
    }
}
