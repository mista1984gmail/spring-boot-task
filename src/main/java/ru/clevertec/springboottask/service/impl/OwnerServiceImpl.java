package ru.clevertec.springboottask.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.springboottask.entity.dto.HouseDto;
import ru.clevertec.springboottask.entity.dto.HouseHistoryDto;
import ru.clevertec.springboottask.entity.dto.PersonDto;
import ru.clevertec.springboottask.entity.enums.PersonType;
import ru.clevertec.springboottask.entity.model.House;
import ru.clevertec.springboottask.entity.model.Person;
import ru.clevertec.springboottask.exception.EntityNotFoundException;
import ru.clevertec.springboottask.mapper.HouseMapper;
import ru.clevertec.springboottask.mapper.PersonMapper;
import ru.clevertec.springboottask.repository.HouseRepository;
import ru.clevertec.springboottask.repository.PersonRepository;
import ru.clevertec.springboottask.service.HouseHistoryService;
import ru.clevertec.springboottask.service.OwnerService;
import ru.clevertec.springboottask.web.request.OwnerRequest;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerServiceImpl implements OwnerService {

    private final HouseRepository houseRepository;
    private final PersonRepository personRepository;
    private final HouseHistoryService houseHistoryService;
    private final PersonMapper personMapper;
    private final HouseMapper houseMapper;

    /**
     * Находит всех нынешних владельцев (Person) дома (House)
     *
     * @param uuidHouse идентификатор House
     * @return List<PersonDto> список нынешних владельцев
     * @throws EntityNotFoundException если House не найден
     */
    @Override
    public List<PersonDto> findAllOwners(UUID uuidHouse) {
        House house = getHouseByUUID(uuidHouse);
        log.info("Find all owners by House uuid {}", uuidHouse);
        return personMapper.entityListToDtoList(house.getOwners());
    }

    /**
     * Находит все дома (House), которыми на данных момент владеет человек (Person)
     *
     * @param uuidPerson идентификатор Person
     * @return List<HouseDto> список всех домов
     * @throws EntityNotFoundException если House не найден
     */
    @Override
    public List<HouseDto> findAllHousesPerson(UUID uuidPerson) {
        Person person = getPersonByUUID(uuidPerson);
        log.info("Find all Houses own by Person with uuid {}", uuidPerson);
        return houseMapper.entityListToDtoList(person.getOwnHouses());
    }

    /**
     * Добавляет нового владельца дома (House)
     *
     * @param ownerRequest информация о новом владельце (Person) и о доме (House), которым он будет владеть
     * @throws EntityNotFoundException если House или Person не найден
     */
    @Override
    @Transactional
    public void addOwner(OwnerRequest ownerRequest) {
        Person person = getPersonByUUID(ownerRequest.getUuidPerson());
        House house = getHouseByUUID(ownerRequest.getUuidHouse());
        List<House> ownHouses = person.getOwnHouses();
        if(!ownHouses.contains(house)){
            ownHouses.add(house);
            List<Person> owners = house.getOwners();
            owners.add(person);
        }
        houseRepository.save(house);
        personRepository.save(person);
        addOwnerToHouseHistory(house.getId(), person.getId());
        log.info("Add owner with uuid {} by House with uuid {}", ownerRequest.getUuidPerson(), ownerRequest.getUuidHouse());
    }

    /**
     * Удаляет владельца дома
     *
     * @param ownerRequest
     * @throws EntityNotFoundException если House или Person не найден
     */
    @Override
    @Transactional
    public void removeOwner(OwnerRequest ownerRequest) {
        Person person = getPersonByUUID(ownerRequest.getUuidPerson());
        House house = getHouseByUUID(ownerRequest.getUuidHouse());
        List<House> ownHouses = person.getOwnHouses();
        if(ownHouses.contains(house)){
            ownHouses.remove(house);
            List<Person> owners = house.getOwners();
            owners.remove(person);
        }
        houseRepository.save(house);
        personRepository.save(person);
        log.info("Remove owner with uuid {} at House with uuid {}", ownerRequest.getUuidPerson(), ownerRequest.getUuidHouse());
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
     * Ищет Person по идентификатору
     *
     * @param uuid идентификатор Person
     * @return найденный Person
     * @throws EntityNotFoundException если Person не найден
     */
    private Person getPersonByUUID(UUID uuid) {
        return personRepository.findByUuidPerson(uuid)
                .orElseThrow(() -> new EntityNotFoundException(Person.class, uuid));
    }

    /**
     * Добавляет в HouseHistory информацию о том, что House
     * начал владеть Person
     *
     * @param houseId идентификатор House (id)
     * @param personId идентификатор Person (id)
     */
    private void addOwnerToHouseHistory(Long houseId, Long personId) {
        HouseHistoryDto houseHistoryDto = new HouseHistoryDto();
        houseHistoryDto.setHouseId(houseId);
        houseHistoryDto.setPersonId(personId);
        houseHistoryDto.setPersonType(PersonType.OWNER);
        houseHistoryService.save(houseHistoryDto);
    }

}
