package ru.clevertec.springboottask.service;

import org.springframework.data.domain.Page;
import ru.clevertec.springboottask.entity.dto.PersonDto;
import ru.clevertec.springboottask.web.request.PersonRequest;

import java.util.UUID;

public interface PersonService {

    PersonDto save(PersonRequest personRequest, UUID houseUUID);

    void delete(UUID uuid);

    PersonDto update(UUID uuid, PersonRequest personRequest);

    PersonDto findByUUID(UUID uuid);

    Page<PersonDto> findAllWithPagination(Integer page, Integer size, String orderBy, String direction);

}
