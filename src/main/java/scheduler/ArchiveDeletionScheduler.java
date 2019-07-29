package scheduler;

import common.ApplicationVariables;
import io.xlate.inject.Property;
import io.xlate.inject.PropertyResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.archive.ArchiveService;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class ArchiveDeletionScheduler {
    private static final Logger LOG = LogManager.getLogger(ArchiveDeletionScheduler.class);

    @Inject
    private ArchiveService archiveService;

    @Inject
    @Property(name = "scheduler.archive.difference",
            resource = @PropertyResource(ApplicationVariables.PROPERTIES_FILE_PATH),
            defaultValue = "1")
    private int difference;

    @Inject
    @Property(name = "scheduler.archive.interval",
            resource = @PropertyResource(ApplicationVariables.PROPERTIES_FILE_PATH),
            defaultValue = "year")
    private String interval;

    @Schedule(dayOfWeek = "Sun", minute = "*/30", hour = "22,23")
    public void deleteOldArchivedRecord() {
        LOG.info("Starting old archive cleanup...");
        archiveService.findAllArchiveToDelete(interval, difference).stream().forEach(logbookGetDTO -> {
            archiveService.deleteById(logbookGetDTO.getId());
        });
    }
}
