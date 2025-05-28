package com.humanbooster.electricity_business.controller;

import com.humanbooster.electricity_business.service.GenericService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class GenericController<T, ID> {
    private final GenericService<T, ID> service;

    public GenericController(GenericService<T,ID> service) {
        this.service = service;
    }

    public List<T> getAll() {
        return service.getAll();
    }

    public T create(@RequestBody T entity) {
        return service.create(entity);
    }

    public ResponseEntity<T> getById(ID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<T> deleteById(ID id) {
        return service.deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

     public ResponseEntity<T> updateById(ID id, T newEntity) {
        return service.update(newEntity, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
}
