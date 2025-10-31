package org.example.onlineshop.common.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MappingTarget;

/**
 * BaseMapper برای تبدیل Entity ↔ DTO
 * همه Mapperهای ماژولار می‌تونن اینو implement کنند
 */
public interface BaseMapper<E, D> {

    /**
     * تبدیل DTO به Entity
     */
    E toEntity(D dto);

    /**
     * تبدیل Entity به DTO
     */
    D toDto(E entity);

    /**
     * آپدیت Entity با داده‌های DTO
     */
    @InheritInverseConfiguration
    void updateEntityFromDto(D dto, @MappingTarget E entity);
}
