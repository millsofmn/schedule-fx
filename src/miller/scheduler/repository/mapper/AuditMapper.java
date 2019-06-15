package miller.scheduler.repository.mapper;

import miller.scheduler.domain.AbstractEntity;

import java.time.Instant;

public abstract class AuditMapper {

    public static void setAudit(AbstractEntity entity){
        if(entity.getCreateDate() == null){
            entity.setCreateDate(Instant.now());
        }
        if(entity.getCreatedBy() == null){
            entity.setCreatedBy("SYSTEM_ADMIN");
        }
        if(entity.getLastUpdate() == null){
            entity.setLastUpdate(Instant.now());
        }
        if(entity.getLastUpdatedBy() == null){
            entity.setLastUpdatedBy("SYSTEM_ADMIN");
        }
    }
}
