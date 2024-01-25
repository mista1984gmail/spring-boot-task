package ru.clevertec.springboottask.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.aspect.annotation.DeleteObjectFromCache;
import ru.clevertec.aspect.annotation.GetObjectFromCache;
import ru.clevertec.aspect.annotation.SaveObjectToCache;
import ru.clevertec.aspect.annotation.UpdateObjectInCache;
import ru.clevertec.springboottask.entity.dto.HouseDto;
import ru.clevertec.springboottask.entity.model.House;
import ru.clevertec.springboottask.exception.EntityNotFoundException;
import ru.clevertec.springboottask.mapper.HouseMapper;
import ru.clevertec.springboottask.repository.HouseRepository;
import ru.clevertec.springboottask.service.HouseService;
import ru.clevertec.springboottask.util.Constants;
import ru.clevertec.springboottask.web.request.HouseRequest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;

    /**
     * Создаёт новоый House из HouseRequest
     * задает рандомный UUID и время создания
     *
     * @param houseRequest HouseRequest
     */
    @Override
    @Transactional
    @SaveObjectToCache
    public HouseDto save(HouseRequest houseRequest) {
        HouseDto houseToSave = houseMapper.requestToDto(houseRequest);
        houseToSave.setUuidHouse(UUID.randomUUID());
        houseToSave.setCreateDate(LocalDateTime.now());
        log.info("House with uuid {} saved", houseToSave.getUuidHouse());
        House houseSaved = houseRepository.save(houseMapper.dtoToEntity(houseToSave));
        return houseMapper.entityToDto(houseSaved);
    }

    /**
     * Удаляет существующий House
     * Используется для уделения soft delete
     *
     * @param uuid идентификатор House для удаления
     * @throws EntityNotFoundException если House не найден
     */
    @Override
    @Transactional
    @DeleteObjectFromCache
    public void delete(UUID uuid) {
        houseRepository.delete(getByUUID(uuid));
        log.info("House with uuid {} deleted", uuid);
    }

    /**
     * Обновляет уже существующий House из информации полученной в HouseRequest
     *
     * @param uuid     идентификатор House для обновления
     * @param houseRequest HouseRequest с информацией об обновлении
     * @throws EntityNotFoundException если House не найден
     */
    @Override
    @Transactional
    @UpdateObjectInCache
    public HouseDto update(UUID uuid, HouseRequest houseRequest) {
        House houseFromDB = getByUUID(uuid);
        houseMapper.mergeEntity(houseFromDB, houseRequest);
        log.info("House with uuid {} updated", uuid);
        return houseMapper.entityToDto(houseRepository.save(houseFromDB));
    }

    /**
     * Ищет House по идентификатору
     *
     * @param uuid идентификатор House
     * @return найденный HouseDto
     * @throws EntityNotFoundException если House не найден
     */
    @Override
    @GetObjectFromCache
    public HouseDto findByUUID(UUID uuid) {
        log.info("House with uuid {} found", uuid);
        return Optional.of(getByUUID(uuid))
                .map(houseMapper::entityToDto)
                .get();
    }

    /**
     * Возвращает все существующие House
     *
     * @param page номер страницы
     * @param size количество House на странице (по умолчанию 15)
     * @param orderBy по какому полю сортировать (по умолчанию "city")
     * @param direction как сотрировать (по умолчению "ASC")
     * @return лист с информацией о HouseDto
     */
    @Override
    public Page<HouseDto> findAllWithPaginationAndSorting(Integer page, Integer size, String orderBy, String direction) {
        log.info("Show Houses on the page {}", page );
        if (orderBy == null || orderBy.isEmpty()) {
            orderBy = Constants.DEFAULT_HOUSE_ORDER_BY;
        }
        if (direction == null || direction.isEmpty()) {
            direction = Constants.DEFAULT_DIRECTION;
        }
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
        Page<House> foundHouses = houseRepository.findAll(pageRequest);
        return foundHouses.map(houseMapper::entityToDto);
    }

    /**
     * Ищет House по идентификатору
     *
     * @param uuid идентификатор House
     * @return найденный House
     * @throws EntityNotFoundException если House не найден
     */
    private House getByUUID(UUID uuid) {
        return houseRepository.findByUuidHouse(uuid)
                .orElseThrow(() -> new EntityNotFoundException(House.class, uuid));
    }

}
