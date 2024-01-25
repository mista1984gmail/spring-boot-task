package ru.clevertec.springboottask.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.springboottask.entity.dto.HouseDto;
import ru.clevertec.springboottask.mapper.HouseMapper;
import ru.clevertec.springboottask.repository.HouseRepository;
import ru.clevertec.springboottask.service.HouseSearchService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HouseSearchServiceImpl implements HouseSearchService {

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;

    /**
     * Производит поиск всех домов по названию
     * страны (country), города (city) или улицы (street)
     *
     * @param name - название страны, города или улицы
     * @return лист с информацией о HouseDto
     */
    @Override
    public List<HouseDto> findByCountryOrCityOrStreet(String name) {
        log.info("Find all Houses with country or city or street equals {}", name);
        return houseMapper.entityListToDtoList(houseRepository.findByCountryOrCityOrStreet(name));
    }

}
