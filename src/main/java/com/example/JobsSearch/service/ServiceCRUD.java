package com.example.JobsSearch.service;

import com.example.JobsSearch.payload.Response.ResponseObject;

import java.util.Collection;
import java.util.Optional;

public interface ServiceCRUD<T, B> {
    ResponseObject create(T object);

    ResponseObject update(Long id, T object);

    ResponseObject delete(Long id);

    Collection<B> getAll();

    Optional<B> getById(Long id);
}
