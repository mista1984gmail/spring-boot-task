package ru.clevertec.cache.conditional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.aspect.CacheAspect;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.impl.LFUCache;
import ru.clevertec.cache.impl.LRUCache;
import ru.clevertec.util.Constants;

@Configuration
public class CacheFactory {

    @Value("${cache.type}")
    private String typeCache;

    @Value("${cache.size}")
    private Integer sizeCache;

    @Bean
    @Conditional(CacheConditional.class)
    public CacheAspect cacheAspect() {
        Cache cache = null;
        if (typeCache.equals(Constants.CACHE_TYPE_LRU)) {
            cache = new LRUCache();
            cache.setSizeCache(sizeCache);
        }
        if (typeCache.equals(Constants.CACHE_TYPE_LFU)) {
            cache = new LFUCache();
            cache.setSizeCache(sizeCache);
        }
        return new CacheAspect(cache, objectMapper());
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

}
