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

    @GetMapping
    public List<T> getAll() {
        return service.getAll();
    }

    @PostMapping
    public T create(@RequestBody T entity) {
        return service.create(entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable ID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<T> deleteById(@PathVariable ID id) {
        return service.deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
     public ResponseEntity<T> updateById(@PathVariable ID id, @RequestBody T newEntity) {
        return service.update(newEntity, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
}
