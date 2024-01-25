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
import ru.clevertec.springboottask.entity.dto.HouseHistoryDto;
import ru.clevertec.springboottask.entity.dto.PersonDto;
import ru.clevertec.springboottask.entity.enums.PersonType;
import ru.clevertec.springboottask.entity.model.House;
import ru.clevertec.springboottask.entity.model.Person;
import ru.clevertec.springboottask.exception.EntityNotFoundException;
import ru.clevertec.springboottask.mapper.PersonMapper;
import ru.clevertec.springboottask.repository.HouseRepository;
import ru.clevertec.springboottask.repository.PersonRepository;
import ru.clevertec.springboottask.service.HouseHistoryService;
import ru.clevertec.springboottask.service.PersonService;
import ru.clevertec.springboottask.util.Constants;
import ru.clevertec.springboottask.web.request.PersonRequest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final HouseRepository houseRepository;
    private final HouseHistoryService houseHistoryService;
    private final PersonMapper personMapper;

    /**
     * Создаёт новоый Person из PersonRequest
     * задает рандомный UUID и время создания
     *
     * @param personRequest PersonRequest с информацией о создании
     * @param houseUUID uuid House где будет жить Person
     * @return PersonDto
     */
    @Override
    @Transactional
    @SaveObjectToCache
    public PersonDto save(PersonRequest personRequest, UUID houseUUID) {
        House house = getHouseByUUID(houseUUID);
        Person personForSave = personMapper.requestToModel(personRequest);
        personForSave.setUuidPerson(UUID.randomUUID());
        personForSave.setCreateDate(LocalDateTime.now());
        personForSave.setUpdateDate(personForSave.getCreateDate());
        personForSave.setResidentHouse(house);
        Person personSaved = personRepository.save(personForSave);
        log.info("Person with uuid {} saved", personForSave.getUuidPerson());
        addTenantToHouseHistory(house.getId(), personSaved.getId());
        return personMapper.entityToDto(personSaved);
    }

    /**
     * Удаляет существующий Person
     *
     * @param uuid идентификатор Person для удаления
     * @throws EntityNotFoundException если Person не найден
     */
    @Override
    @Transactional
    @DeleteObjectFromCache
    public void delete(UUID uuid) {
        Person person = getByUUID(uuid);
        personRepository.deleteOwnHouses(person.getId());
        personRepository.delete(person);
        log.info("Person with uuid {} deleted", uuid);
    }

    /**
     * Обновляет уже существующий Person из информации полученной в PersonRequest
     *
     * @param uuid     идентификатор Person для обновления
     * @param personRequest PersonRequest с информацией об обновлении
     * @throws EntityNotFoundException если Person не найден
     */
    @Override
    @Transactional
    @UpdateObjectInCache
    public PersonDto update(UUID uuid, PersonRequest personRequest) {
        Person personFromDB = getByUUID(uuid);
        personMapper.mergeEntity(personFromDB, personRequest);
        personFromDB.setUpdateDate(LocalDateTime.now());
        log.info("Person with uuid {} updated", uuid);
        return personMapper.entityToDto(personRepository.save(personFromDB));
    }

    /**
     * Ищет Person по идентификатору
     *
     * @param uuid идентификатор Person
     * @return найденный PersonDto
     * @throws EntityNotFoundException если Person не найден
     */
    @Override
    @GetObjectFromCache
    public PersonDto findByUUID(UUID uuid) {
        log.info("Person with uuid {} found", uuid);
        return Optional.of(getByUUID(uuid))
                .map(personMapper::entityToDto)
                .get();
    }

    /**
     * Возвращает всех существующих Person
     *
     * @param page номер страницы
     * @param size количество Person на странице (по умолчанию 15)
     * @param orderBy по какому полю сортировать (по умолчанию "name")
     * @param direction как сотрировать (по умолчению "ASC")
     * @return лист с информацией о PersonDto
     */
    @Override
    public Page<PersonDto> findAllWithPagination(Integer page, Integer size, String orderBy, String direction) {
        log.info("Show Persons on the page {}", page );
        if (orderBy == null || orderBy.isEmpty()) {
            orderBy = Constants.DEFAULT_PERSON_ORDER_BY;
        }
        if (direction == null || direction.isEmpty()) {
            direction = Constants.DEFAULT_DIRECTION;
        }
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
        Page<Person> foundPersons = personRepository.findAll(pageRequest);
        return foundPersons.map(personMapper::entityToDto);
    }

    /**
     * Ищет Person по идентификатору
     *
     * @param uuid идентификатор Person
     * @return найденный Person
     * @throws EntityNotFoundException если Person не найден
     */
    private Person getByUUID(UUID uuid) {
        return personRepository.findByUuidPerson(uuid)
                .orElseThrow(() -> new EntityNotFoundException(Person.class, uuid));
    }

    /**
     * Ищет House по идентификатору
     *
     * @param uuid идентификатор House
     * @return найденный House
     * @throws EntityNotFoundException если House не найден
     */
    private House getHouseByUUID(UUID uuid) {
        return houseRepository.findByUuidHouse(uuid)
                .orElseThrow(() -> new EntityNotFoundException(House.class, uuid));
    }

    /**
     * Добавляет в HouseHistory информацию о том, что в House
     * начал жить Person
     *
     * @param houseId идентификатор House (id)
     * @param personId идентификатор Person (id)
     */
    private void addTenantToHouseHistory(Long houseId, Long personId) {
        HouseHistoryDto houseHistoryDto = new HouseHistoryDto();
        houseHistoryDto.setHouseId(houseId);
        houseHistoryDto.setPersonId(personId);
        houseHistoryDto.setPersonType(PersonType.TENANT);
        houseHistoryService.save(houseHistoryDto);
    }

}
