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
import ru.clevertec.springboottask.entity.dto.HouseDto;
import ru.clevertec.springboottask.entity.model.House;
import ru.clevertec.springboottask.exception.EntityNotFoundException;
import ru.clevertec.springboottask.mapper.HouseMapper;
import ru.clevertec.springboottask.repository.HouseRepository;
import ru.clevertec.springboottask.service.impl.HouseServiceImpl;
import ru.clevertec.springboottask.util.ConstantsForTest;
import ru.clevertec.springboottask.util.HouseTestData;
import ru.clevertec.springboottask.web.request.HouseRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true",
        "spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER"})
public class HouseServiceImplIT {

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
    private HouseRepository houseRepository;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private HouseServiceImpl houseService;

    @Test
    void shouldCreateHouse() {
        //given
        HouseRequest houseRequestForSave = HouseTestData.builder()
                                                        .build()
                                                        .buildHouseRequest();

        //when
        HouseDto houseSaved = houseService.save(houseRequestForSave);

        //then
        assertNotNull(houseSaved.getUuidHouse());
        assertEquals(houseSaved.getArea(), houseRequestForSave.getArea());
        assertEquals(houseSaved.getCountry(), houseRequestForSave.getCountry());
        assertEquals(houseSaved.getCity(), houseRequestForSave.getCity());
        assertEquals(houseSaved.getStreet(), houseRequestForSave.getStreet());
        assertEquals(houseSaved.getNumber(), houseRequestForSave.getNumber());
    }

    @Test
    void shouldFindHouseByUUID() {
        //given
        HouseRequest houseRequestForSave = HouseTestData.builder()
                .build()
                .buildHouseRequest();

        //when
        HouseDto houseSaved = houseService.save(houseRequestForSave);
        UUID uuid = houseSaved.getUuidHouse();
        HouseDto houseFromDBByUUID = houseService.findByUUID(uuid);

        //then
        assertNotNull(houseSaved.getUuidHouse());
        assertEquals(houseFromDBByUUID.getArea(), houseRequestForSave.getArea());
        assertEquals(houseFromDBByUUID.getCountry(), houseRequestForSave.getCountry());
        assertEquals(houseFromDBByUUID.getCity(), houseRequestForSave.getCity());
        assertEquals(houseFromDBByUUID.getStreet(), houseRequestForSave.getStreet());
        assertEquals(houseFromDBByUUID.getNumber(), houseRequestForSave.getNumber());
    }

    @Test
    void shouldNotGetHouseByUUIDAndThrowsEntityNotFoundException() {
        //given
        UUID uuid = ConstantsForTest.UUID_HOUSE;
        String errorMessage = "House with uuid: " + uuid + " not found";

        //when

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            houseService.findByUUID(uuid);
        });

        //then
        assertEquals(errorMessage, thrown.getMessage());
    }

    @Test
    void shouldNotDeleteByUUIDAndThrowsEntityNotFoundException() {
        //given
        UUID uuid = ConstantsForTest.UUID_HOUSE;
        String errorMessage = "House with uuid: " + uuid + " not found";

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            houseService.delete(uuid);
        });

        //then
        assertEquals(errorMessage, thrown.getMessage());
    }

    @Test
    void shouldDeleteByUUID() {
        //given
        HouseRequest houseRequestForSave = HouseTestData.builder()
                .build()
                .buildHouseRequest();

        //when
        HouseDto houseSaved = houseService.save(houseRequestForSave);
        UUID uuidFromDB = houseSaved.getUuidHouse();
        String errorMessage = "House with uuid: " + uuidFromDB + " not found";
        houseService.delete(uuidFromDB);
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            houseService.delete(uuidFromDB);
        });

        //then
        assertEquals(errorMessage, thrown.getMessage());
    }

    @Test
    void shouldNotUpdateByUUIDAndThrowsEntityNotFoundException() {
        //given
        UUID uuid = ConstantsForTest.UUID_HOUSE;
        HouseRequest houseRequestForUpdate = HouseTestData.builder()
                                                        .build()
                                                        .buildHouseRequest();
        String errorMessage = "House with uuid: " + uuid + " not found";

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            houseService.update(uuid, houseRequestForUpdate);
        });

        //then
        assertEquals(errorMessage, thrown.getMessage());
    }

    @Test
    void shouldUpdateByUUID() {
        //given
        HouseRequest houseRequestForSave = HouseTestData.builder()
                                                        .build()
                                                        .buildHouseRequest();

        HouseRequest houseRequestForUpdate = HouseTestData.builder()
                                                        .withArea(54.5)
                                                        .withCountry("RB")
                                                        .withCity("Grodno")
                                                        .withStreet("Mira")
                                                        .withNumber(55)
                                                        .build()
                                                        .buildHouseRequest();

        //when
        HouseDto houseSaved = houseService.save(houseRequestForSave);
        UUID uuid = houseSaved.getUuidHouse();
        houseService.update(uuid, houseRequestForUpdate);
        House actual = houseRepository.findByUuidHouse(uuid).get();

        //then
        assertNotNull(houseSaved.getUuidHouse());
        assertEquals(actual.getArea(), houseRequestForUpdate.getArea());
        assertEquals(actual.getCountry(), houseRequestForUpdate.getCountry());
        assertEquals(actual.getCity(), houseRequestForUpdate.getCity());
        assertEquals(actual.getStreet(), houseRequestForUpdate.getStreet());
        assertEquals(actual.getNumber(), houseRequestForUpdate.getNumber());
    }

    @Test
    void shouldFindFiveHouses() {
        //give

        //when
        Page<HouseDto> houses = houseService.findAllWithPaginationAndSorting(0, 5, "", "");

        //then
        assertEquals(houses.stream().toList().size(), 5);
    }

}
