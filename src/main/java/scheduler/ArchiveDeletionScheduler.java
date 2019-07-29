package scheduler;

import domain.enums.Interval;
import service.archive.ArchiveService;

import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class ArchiveDeletionScheduler {

    @Inject
    private ArchiveService archiveService;

    @Schedules({
            @Schedule(dayOfWeek = "Mon", hour = "12")
    })
    public void deleteOldArchivedRecord() {
        System.out.println("Trying to delete from archive");
        archiveService.findAllArchiveToDelete(Interval.YEAR.getName(), "-1").stream().forEach(logbookGetDTO -> {
            archiveService.deleteById(logbookGetDTO.getId());
        });
    }
}
