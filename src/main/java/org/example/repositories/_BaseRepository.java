package org.example.repositories;

import org.example.entities._BaseEntity;

import java.util.List;
import java.util.Optional;

public interface _BaseRepository<T extends _BaseEntity> {
    void initialize();
    void create(T obj);
    List<T> readAll(String orderBy, String direction, int limit, int offset);
    Optional<T> read(int id);
    void update(int id, T obj);
    void delete(int id);
}
