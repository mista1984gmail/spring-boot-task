package ru.clevertec.cache.impl;

import lombok.extern.slf4j.Slf4j;
import ru.clevertec.cache.Cache;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class LRUCache implements Cache {

    private Map<UUID, Object> CACHE;
    private Map<UUID, LocalDateTime> LAST_ACCESS_CACHE;
    private Integer SIZE_CACHE;

    public LRUCache() {
        CACHE = new HashMap<>();
        LAST_ACCESS_CACHE = new HashMap<>();
    }

    /**
     * Устанавливает размер кэша.
     */
    @Override
    public void setSizeCache(Integer sizeCache) {
        SIZE_CACHE = sizeCache;
    }

    /**
     * Сохраняет переданный id объекта и сам объект в кэш.
     * Сохраняет время, передачи объекта в кэш.
     */
    @Override
    public Object save(UUID uuid, Object object) {
        checkCacheSize();
        if (uuid != null) {
            log.info("Save object with uuid {} saved to cache", uuid);
            CACHE.put(uuid, object);
            LAST_ACCESS_CACHE.put(uuid, LocalDateTime.now());
        }
        return object;
    }

    /**
     * Возращает объект из кэша по переданному id,
     * если объекта нет с таким id - возвращает null.
     * <p>
     * Сохраняет время, обращения к объекту из кэша.
     *
     * @param uuid объекта для отображения
     * @return объект по id
     */
    @Override
    public Object getByUUID(UUID uuid) {
        Object object = null;
        if (CACHE.containsKey(uuid)) {
            log.info("Get object from cache");
            object = CACHE.get(uuid);
            LAST_ACCESS_CACHE.put(uuid, LocalDateTime.now());
        }
        return object;
    }

    /**
     * Удаляет объект из кэша по переданному id,
     *
     * @param uuid объекта для удаления
     */
    @Override
    public void delete(UUID uuid) {
        if (CACHE.containsKey(uuid)) {
            CACHE.remove(uuid);
            LAST_ACCESS_CACHE.remove(uuid);
        }
    }

    /**
     * Проверяет существующий размер кэша с установленным.
     * Если существующий размер кэша равен или больше
     * установленного значения - вызывает метод для удаления
     * элементов из кэша.
     */
    private void checkCacheSize() {
        if (CACHE.size() >= SIZE_CACHE) {
            deleteFromCache();
        }
    }

    /**
     * Удаляет элемент из кэша.
     * <p>
     * Удаляет элемент с самым старым временем
     * добавления в кэш.
     */
    private void deleteFromCache() {
        UUID idForDelete = LAST_ACCESS_CACHE.entrySet()
                                            .stream()
                                            .sorted(Map.Entry.comparingByValue())
                                            .findFirst()
                                            .get()
                                            .getKey();
        delete(idForDelete);
    }

}