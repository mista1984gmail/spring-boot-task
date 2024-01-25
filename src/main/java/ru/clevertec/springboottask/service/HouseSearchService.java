package ru.clevertec.springboottask.service;


import ru.clevertec.springboottask.entity.dto.HouseDto;

import java.util.List;

public interface HouseSearchService {

    List<HouseDto> findByCountryOrCityOrStreet(String name);

}
