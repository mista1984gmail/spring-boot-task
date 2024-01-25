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
import ru.clevertec.springboottask.service.ResidentService;
import ru.clevertec.springboottask.web.request.ResidenceRequest;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResidentServiceImpl implements ResidentService {

    private final HouseRepository houseRepository;
    private final PersonRepository personRepository;
    private final HouseHistoryService houseHistoryService;
    private final PersonMapper personMapper;
    private final HouseMapper houseMapper;

    /**
     * Находит всех нынешних жильцов (Person) дома (House)
     *
     * @param uuidHouse идентификатор House
     * @return List<PersonDto> список нынешних владельцев
     * @throws EntityNotFoundException если House не найден
     */
    @Override
    public List<PersonDto> getResidents(UUID uuidHouse) {
        House house = getHouseByUUID(uuidHouse);
        log.info("Find all residents by House uuid {}", uuidHouse);
        return personMapper.entityListToDtoList(house.getResidents());
    }

    /**
     * Находит дом (House), где проживает человек (Person)
     *
     * @param uuidPerson идентификатор Person
     * @return HouseDto дом, где проживает человек
     * @throws EntityNotFoundException если Person не найден
     */
    @Override
    public HouseDto getHouseLivePerson(UUID uuidPerson) {
        Person person = getPersonByUUID(uuidPerson);
        log.info("Find House where Person with uuid {} live", uuidPerson);
        return houseMapper.entityToDto(person.getResidentHouse());
    }

    /**
     * Меняет место жительства человека (Person)
     *
     * @param residenceRequest информация о новом жильце (Person) и о доме (House), которым он будет жить
     * @throws EntityNotFoundException если House или Person не найден
     */
    @Override
    @Transactional
    public void changeResidence(ResidenceRequest residenceRequest) {
        Person person = getPersonByUUID(residenceRequest.getUuidPerson());
        House house = getHouseByUUID(residenceRequest.getUuidHouse());
        person.setResidentHouse(house);
        personRepository.save(person);
        addTenantToHouseHistory(house.getId(), person.getId());
        log.info("Set Person with uuid {} House with uuid {} for live", residenceRequest.getUuidPerson(), residenceRequest.getUuidHouse());
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