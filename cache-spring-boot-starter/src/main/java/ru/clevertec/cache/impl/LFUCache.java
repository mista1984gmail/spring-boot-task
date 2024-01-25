package ru.clevertec.cache.impl;

import lombok.extern.slf4j.Slf4j;
import ru.clevertec.cache.Cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class LFUCache implements Cache {

    private Map<UUID, Object> CACHE;
    private Map<UUID, Long> COUNTER_CACHE;
    private Integer SIZE_CACHE;

    public LFUCache() {
        CACHE = new HashMap<>();
        COUNTER_CACHE = new LinkedHashMap<>();
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
     * Если id, данного объекта нет в кэше, устанавливает
     * счетчик обращения к этому обхъекту на 1, иниче
     * к счетчику прибавляется 1.
     */
    @Override
    public Object save(UUID uuid, Object object) {
        checkCacheSize();
        if (uuid != null) {
            long count = (COUNTER_CACHE.containsKey(uuid)) ? COUNTER_CACHE.get(uuid) + 1L : 1L;
            log.info("Save object with uuid {} saved to cache", uuid);
            COUNTER_CACHE.put(uuid, count);
            CACHE.put(uuid, object);
        }
        return object;
    }

    /**
     * Возращает объект из кэша по переданному id,
     * если объекта нет с таким id - возвращает null.
     * <p>
     * Добавляет 1 к счетчику обращений к элементу.
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
            COUNTER_CACHE.put(uuid, COUNTER_CACHE.get(uuid) + 1L);
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
            COUNTER_CACHE.remove(uuid);
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
     * Удаляет элемент с самым маленьким количеством обращений к
     * этому элементу.
     * Если таких элементов несколько - удаляет самый последний
     * добавленный (правило FIFO)
     */
    private void deleteFromCache() {
        Long minCounter = COUNTER_CACHE.values()
                                       .stream()
                                       .mapToLong(v -> v)
                                       .min()
                                       .orElse(0L);
        if (minCounter != 0) {
            LinkedHashMap<UUID, Long> arraysMinElements = COUNTER_CACHE.entrySet()
                                                                       .stream()
                                                                       .filter(a -> a.getValue()
                                                                                     .toString()
                                                                                     .equals(minCounter.toString()))
                                                                       .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                                                               (o1, o2) -> o1, LinkedHashMap::new));
            long sizeArraysMinElements = arraysMinElements.entrySet()
                                                          .stream()
                                                          .count();
            UUID idForDelete = arraysMinElements.entrySet()
                                                .stream()
                                                .skip(sizeArraysMinElements - 1)
                                                .findFirst()
                                                .get()
                                                .getKey();
            delete(idForDelete);
        }
    }

}