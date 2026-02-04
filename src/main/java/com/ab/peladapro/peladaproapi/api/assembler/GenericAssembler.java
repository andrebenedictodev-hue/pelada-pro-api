package com.ab.peladapro.peladaproapi.api.assembler;

import org.modelmapper.ModelMapper;

import java.util.List;

public abstract class GenericAssembler<Entity, ResponseDTO> {

    protected final ModelMapper strictModelMapper;
    private final Class<ResponseDTO> responseDtoClass;
    private final Class<Entity> entityClass;

    public GenericAssembler(ModelMapper modelMapper, Class<Entity> entityClass, Class<ResponseDTO> responseDtoClass) {
        this.strictModelMapper = modelMapper;
        this.entityClass = entityClass;
        this.responseDtoClass = responseDtoClass;
    }

    public ResponseDTO toDTO(Entity entity) {
        return strictModelMapper.map(entity, responseDtoClass);
    }

    public List<ResponseDTO> toCollectionDTO(List<Entity> entityCollection) {
        return entityCollection.stream().map(this::toDTO).toList();
    }

    public <T> Entity toEntity(T requestDto) {
        return strictModelMapper.map(requestDto, entityClass);
    }

    public <T> Entity toEntity(T requestDto, Entity entity) {
        strictModelMapper.map(requestDto, entity);
        return entity;
    }
}
