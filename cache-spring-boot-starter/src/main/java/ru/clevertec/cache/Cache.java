package ru.clevertec.cache;

import java.util.UUID;

public interface Cache<T> {

    T save(UUID uuid, T o);

    T getByUUID(UUID uuid);

    void delete(UUID uuid);

    void setSizeCache(Integer sizeCache);

}
