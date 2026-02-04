package com.ab.peladapro.peladaproapi.domain.model.contracts;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@MappedSuperclass
public abstract class EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", updatable = false, nullable = false, unique = true)
    private String uuid;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @PrePersist
    public void generateUUID() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(OffsetDateTime deletedAt) {
        setDeletedAtRecursive(deletedAt, new HashSet<>());
    }

    protected void setDeletedAtRecursive(OffsetDateTime deletedAt, Set<Object> visited) {
        if (visited.contains(this)) {
            return;
        }
        visited.add(this);

        this.deletedAt = deletedAt;

        for (Field field : this.getClass().getDeclaredFields()) {
            if (!hasCascadeRemove(field)) {
                continue;
            }

            field.setAccessible(true);
            try {
                Object fieldValue = field.get(this);
                if (fieldValue == null) {
                    continue;
                }

                if (fieldValue instanceof EntityBase) {
                    ((EntityBase) fieldValue).setDeletedAtRecursive(deletedAt, visited);
                    continue;
                }

                if (Collection.class.isAssignableFrom(field.getType())) {
                    Collection<?> collection = (Collection<?>) fieldValue;
                    for (Object item : collection) {
                        if (item instanceof EntityBase) {
                            ((EntityBase) item).setDeletedAtRecursive(deletedAt, visited);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao propagar delecao", e);
            }
        }
    }

    private boolean hasCascadeRemove(Field field) {
        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        if (oneToMany != null && containsCascadeRemove(oneToMany.cascade())) {
            return true;
        }

        ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
        if (manyToMany != null && containsCascadeRemove(manyToMany.cascade())) {
            return true;
        }

        OneToOne oneToOne = field.getAnnotation(OneToOne.class);
        if (oneToOne != null && containsCascadeRemove(oneToOne.cascade())) {
            return true;
        }

        ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        if (manyToOne != null && containsCascadeRemove(manyToOne.cascade())) {
            return true;
        }

        return false;
    }

    private boolean containsCascadeRemove(CascadeType[] cascades) {
        for (CascadeType cascade : cascades) {
            if (cascade == CascadeType.REMOVE || cascade == CascadeType.ALL) {
                return true;
            }
        }
        return false;
    }
}
