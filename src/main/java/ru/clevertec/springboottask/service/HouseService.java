package ru.clevertec.springboottask.service;

import org.springframework.data.domain.Page;
import ru.clevertec.springboottask.entity.dto.HouseDto;
import ru.clevertec.springboottask.web.request.HouseRequest;

import java.util.UUID;

public interface HouseService {

    HouseDto save(HouseRequest houseRequest);

    void delete(UUID uuid);

    HouseDto update(UUID uuid, HouseRequest houseRequest);

    HouseDto findByUUID(UUID uuid);

    Page<HouseDto> findAllWithPaginationAndSorting(Integer page, Integer size, String orderBy, String direction);

}
