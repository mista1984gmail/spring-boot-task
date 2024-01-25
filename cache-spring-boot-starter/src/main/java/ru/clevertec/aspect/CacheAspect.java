package ru.clevertec.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import ru.clevertec.cache.Cache;
import ru.clevertec.dto.HouseDto;
import ru.clevertec.dto.PersonDto;

import java.util.UUID;

@Aspect
@RequiredArgsConstructor
public class CacheAspect {

    private final Cache cache;
    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(ru.clevertec.aspect.annotation.SaveObjectToCache)")
    public void saveMethod() {
    }

    @Pointcut("@annotation(ru.clevertec.aspect.annotation.DeleteObjectFromCache)")
    public void deleteMethod() {
    }

    @Pointcut("@annotation(ru.clevertec.aspect.annotation.GetObjectFromCache)")
    public void getMethod() {
    }

    @Pointcut("@annotation(ru.clevertec.aspect.annotation.UpdateObjectInCache)")
    public void updateMethod() {
    }

    /**
     * Вызывает метод сохранения полученного объекта в кэш.
     */
    @Around(value = "saveMethod()")
    public Object doSaveProfiling(ProceedingJoinPoint pjp) throws Throwable {
        return profilingAndSaveToCache(pjp);
    }

    /**
     * Вызывает метод удаления объекта из кэша по id.
     */
    @Around(value = "deleteMethod()")
    public Object doDeleteProfiling(ProceedingJoinPoint pjp) throws Throwable {
        UUID uuidForDelete = (UUID) pjp.getArgs()[0];
        pjp.proceed();
        cache.delete(uuidForDelete);
        return uuidForDelete;
    }

    /**
     * Получает объект из кеша по id, если такой там есть.
     * Если такого объекта по id в кэше нет - вызывает метод
     * получения объекта из базы данных.
     */
    @Around(value = "getMethod()")
    public Object doGetProfiling(ProceedingJoinPoint pjp) throws Throwable {
        UUID uuidForGet = (UUID) pjp.getArgs()[0];
        Object o;
        o = cache.getByUUID(uuidForGet);
        if (o == null) {
            return profilingAndSaveToCache(pjp);
        }
        return o;
    }

    /**
     * Вызывает метод обновления переданного объекта в кэше.
     */
    @Around(value = "updateMethod()")
    public Object doUpdateProfiling(ProceedingJoinPoint pjp) throws Throwable {
        return profilingAndSaveToCache(pjp);
    }

    private Object profilingAndSaveToCache(ProceedingJoinPoint pjp) throws Throwable {
        final Signature signature = pjp.getSignature();
        final Class<?> clazz = ((MethodSignature)signature).getReturnType();
        if(clazz.getSimpleName().equals("HouseDto")){
            Object house = pjp.proceed();
            HouseDto houseDto = objectMapper.convertValue(house, HouseDto.class);
            cache.save(houseDto.getUuidHouse(), house);
            return house;
        }
        else if(clazz.getSimpleName().equals("PersonDto")){
            Object person = pjp.proceed();
            PersonDto personDto = objectMapper.convertValue(person, PersonDto.class);
            cache.save(personDto.getUuidPerson(), person);
            return person;
        }
        else {
            Object o = pjp.proceed();
            return o;
        }
    }

}