package com.example.order.simulator.mapper;

import java.util.List;

public interface BaseMapper<Entity, Dto> {

    /**
     * Converts an entity to a DTO.
     *
     * @param entity the entity to convert
     * @return the converted DTO
     */
    Dto toDto(Entity entity);

    /**
     * Converts a DTO to an entity.
     *
     * @param dto the DTO to convert
     * @return the converted entity
     */
    Entity toEntity(Dto dto);

    /**
     * Converts a list of entities to a list of DTOs.
     *
     * @param entities the list of entities to convert
     * @return the list of converted DTOs
     */
    List<Dto> toDtoList(List<Entity> entities);

    /**
     * Converts a list of DTOs to a list of entities.
     *
     * @param dtos the list of DTOs to convert
     * @return the list of converted entities
     */
    List<Entity> toEntityList(List<Dto> dtos);

}
