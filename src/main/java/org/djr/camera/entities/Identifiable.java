package org.djr.camera.entities;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by djr4488 on 12/6/16.
 */
@MappedSuperclass
public abstract class Identifiable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "meta__created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "meta__last_updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedAt;
    @Column(name = "meta__version")
    @Version
    private long version;

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
