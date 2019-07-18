package domain.config;

import domain.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

@Entity
@NamedQueries({
        @NamedQuery(name = "configuration.findAll", query = "SELECT c FROM Configuration c"),
        @NamedQuery(name = "configuration.findValueByKey", query = "SELECT c.value FROM Configuration c where c.key = :key")
}
)
public class Configuration extends BaseEntity {

    @Column(unique = true)
    private String key;
    private String value;
    private String description;
    @Version
    private Integer version;

    public Configuration() {
    }

    public Configuration(String key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }
}
