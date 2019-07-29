package domain;

import domain.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@NamedQueries(
        @NamedQuery(name = "archive.findAll", query = "SELECT a FROM Archive a")
)
public class Archive extends BaseEntity {

    @Temporal(TemporalType.DATE)
    private Date dateArchived;
    @Lob
    private String serialized;

    public Archive() {
        this.dateArchived = new Date();
    }

    public Archive(String serialized) {
        this();
        this.serialized = serialized;
    }

    public Archive(Date dateArchived, String serialized) {
        this.dateArchived = dateArchived;
        this.serialized = serialized;
    }

    public Date getDateArchived() {
        return dateArchived;
    }

    public void setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
    }

    public String getSerialized() {
        return serialized;
    }

    public void setSerialized(String serialized) {
        this.serialized = serialized;
    }
}
