package demo.unjuanable.common.framework.domain;

import java.time.LocalDateTime;

import static demo.unjuanable.common.framework.domain.ChangingStatus.*;


public abstract class AuditableEntity<T extends AuditableEntity<T>>  implements Persistent, Auditable<T> {
    protected ChangingStatus changingStatus = NEW;
    protected LocalDateTime createdAt;
    protected Long createdBy;
    protected LocalDateTime lastUpdatedAt;
    protected Long lastUpdatedBy;

    public AuditableEntity(LocalDateTime createdAt, Long createdBy) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    @Override
    public ChangingStatus getChangingStatus() {
        return changingStatus;
    }

    @Override
    public void toUpdate() {
        if (this.changingStatus == UNCHANGED) {
            this.changingStatus = UPDATED;
        }
    }

    @Override
    public void toDelete() {
        this.changingStatus = DELETED;
    }

    @Override
    public Long getCreatedBy() {
        return createdBy;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public T setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
        return (T)this;
    }

    @Override
    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    @Override
    public T setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return (T)this;
    }

    @Override
    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

}
