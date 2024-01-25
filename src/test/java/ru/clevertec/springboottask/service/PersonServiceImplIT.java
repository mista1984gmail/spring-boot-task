package ru.clevertec.springboottask.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.springboottask.entity.dto.PersonDto;
import ru.clevertec.springboottask.entity.enums.Sex;
import ru.clevertec.springboottask.entity.model.House;
import ru.clevertec.springboottask.entity.model.Person;
import ru.clevertec.springboottask.exception.EntityNotFoundException;
import ru.clevertec.springboottask.mapper.PersonMapper;
import ru.clevertec.springboottask.repository.HouseRepository;
import ru.clevertec.springboottask.repository.PersonRepository;
import ru.clevertec.springboottask.service.impl.PersonServiceImpl;
import ru.clevertec.springboottask.util.ConstantsForTest;
import ru.clevertec.springboottask.util.PersonTestData;
import ru.clevertec.springboottask.web.request.PersonRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true",
        "spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER"})
public class PersonServiceImplIT {

    private final static Long HOUSE_ID = 5l;
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12")
            .withUsername("username")
            .withPassword("password")
            .withExposedPorts(5432)
            .withReuse(true);

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
    }

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private PersonServiceImpl personService;

    @Test
    void shouldCreatePerson() {
        //given
        PersonRequest personRequestForSave = PersonTestData.builder()
                                                           .build()
                                                           .buildPersonRequest();

        //when
        House houseFromDb = houseRepository.findById(HOUSE_ID).get();
        PersonDto personSaved = personService.save(personRequestForSave, houseFromDb.getUuidHouse());

        //then
        assertNotNull(personSaved.getUuidPerson());
        assertEquals(personSaved.getName(), personRequestForSave.getName());
        assertEquals(personSaved.getSurname(), personRequestForSave.getSurname());
        assertEquals(personSaved.getSex(), personRequestForSave.getSex());
        assertEquals(personSaved.getPassportSeries(), personRequestForSave.getPassportSeries());
        assertEquals(personSaved.getPassportNumber(), personRequestForSave.getPassportNumber());
    }

    @Test
    void shouldFindPersonByUUID() {
        //given
        PersonRequest personRequestForSave = PersonTestData.builder()
                                                           .withPassportSeries("TR")
                                                           .build()
                                                           .buildPersonRequest();

        //when
        House houseFromDb = houseRepository.findById(HOUSE_ID).get();
        PersonDto personSaved = personService.save(personRequestForSave, houseFromDb.getUuidHouse());;
        PersonDto personFromDBByUUID = personService.findByUUID(personSaved.getUuidPerson());

        //then
        assertNotNull(personSaved.getUuidPerson());
        assertEquals(personFromDBByUUID.getName(), personRequestForSave.getName());
        assertEquals(personFromDBByUUID.getSurname(), personRequestForSave.getSurname());
        assertEquals(personFromDBByUUID.getSex(), personRequestForSave.getSex());
        assertEquals(personFromDBByUUID.getPassportSeries(), personRequestForSave.getPassportSeries());
        assertEquals(personFromDBByUUID.getPassportNumber(), personRequestForSave.getPassportNumber());
    }

    @Test
    void shouldNotGetPersonByUUIDAndThrowsEntityNotFoundException() {
        //given
        UUID uuid = ConstantsForTest.UUID_PERSON;
        String errorMessage = "Person with uuid: " + uuid + " not found";

        //when

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            personService.findByUUID(uuid);
        });

        //then
        assertEquals(errorMessage, thrown.getMessage());
    }

    @Test
    void shouldNotDeleteByUUIDAndThrowsEntityNotFoundException() {
        //given
        UUID uuid = ConstantsForTest.UUID_PERSON;
        String errorMessage = "Person with uuid: " + uuid + " not found";

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            personService.delete(uuid);
        });

        //then
        assertEquals(errorMessage, thrown.getMessage());
    }

    @Test
    void shouldDeleteByUUID() {
        //given
        PersonRequest personRequestForSave = PersonTestData.builder()
                                                           .withPassportSeries("SM")
                                                           .build()
                                                           .buildPersonRequest();
        House houseFromDb = houseRepository.findById(HOUSE_ID).get();
        PersonDto personSaved = personService.save(personRequestForSave, houseFromDb.getUuidHouse());
        UUID uuidFromDB = personSaved.getUuidPerson();
        String errorMessage = "Person with uuid: " + uuidFromDB + " not found";

        //when
        personService.delete(uuidFromDB);
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            personService.delete(uuidFromDB);
        });

        //then
        assertEquals(errorMessage, thrown.getMessage());
    }

    @Test
    void shouldNotUpdateByUUIDAndThrowsEntityNotFoundException() {
        //given
        UUID uuid = ConstantsForTest.UUID_PERSON;
        PersonRequest personRequestForSave = PersonTestData.builder()
                                                           .build()
                                                           .buildPersonRequest();
        String errorMessage = "Person with uuid: " + uuid + " not found";

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            personService.update(uuid, personRequestForSave);
        });

        //then
        assertEquals(errorMessage, thrown.getMessage());
    }

    @Test
    void shouldUpdateByUUID() {
        //given
        PersonRequest personRequestForSave = PersonTestData.builder()
                                                           .withPassportSeries("SD")
                                                           .build()
                                                           .buildPersonRequest();

        PersonRequest personRequestForUpdate = PersonTestData.builder()
                                                        .withName("Petr")
                                                        .withSurname("Petrov")
                                                        .withSex(Sex.MALE)
                                                        .withPassportSeries("LM")
                                                        .withPassportNumber("7654321")
                                                        .build()
                                                        .buildPersonRequest();

        //when
        House houseFromDb = houseRepository.findById(HOUSE_ID).get();
        PersonDto personSaved = personService.save(personRequestForSave, houseFromDb.getUuidHouse());
        UUID uuid = personSaved.getUuidPerson();
        personService.update(uuid, personRequestForUpdate);
        Person actual = personRepository.findByUuidPerson(uuid).get();

        //then
        assertNotNull(personSaved.getUuidPerson());
        assertEquals(actual.getName(), personRequestForUpdate.getName());
        assertEquals(actual.getSurname(), personRequestForUpdate.getSurname());
        assertEquals(actual.getSex(), personRequestForUpdate.getSex());
        assertEquals(actual.getPassportSeries(), personRequestForUpdate.getPassportSeries());
        assertEquals(actual.getPassportSeries(), personRequestForUpdate.getPassportSeries());
    }

    @Test
    void shouldFindFivePersons() {
        //give

        //when
        Page<PersonDto> houses = personService.findAllWithPagination(0, 5, "", "");

        //then
        assertEquals(houses.stream()
                           .toList()
                           .size(), 5);
    }

}
