package com.patika.paycore.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {


    @Version
    private Integer version;

    @Column(name = "created_date", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdDate;

    @Column(name = "updated_date", insertable = false)
    @UpdateTimestamp
    private OffsetDateTime updatedDate;
}