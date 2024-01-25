package ru.clevertec.springboottask.util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.springboottask.entity.dto.HouseDto;
import ru.clevertec.springboottask.entity.model.House;
import ru.clevertec.springboottask.web.request.HouseRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class HouseTestData {

    @Builder.Default
    private Long id = ConstantsForTest.ID;

    @Builder.Default
    private UUID uuidHouse = ConstantsForTest.UUID_HOUSE;

    @Builder.Default
    private Double area = ConstantsForTest.AREA;

    @Builder.Default
    private String country = ConstantsForTest.COUNTRY;

    @Builder.Default
    private String city = ConstantsForTest.CITY;

    @Builder.Default
    private String street = ConstantsForTest.STREET;

    @Builder.Default
    private Integer number = ConstantsForTest.NUMBER;

    @Builder.Default
    private LocalDateTime createDate = ConstantsForTest.CREATE_DATE;

    public House buildHouse() {
        return new House(id, uuidHouse, area, country, city, street, number, createDate);
    }

    public HouseDto buildHouseDto() {
        return new HouseDto(uuidHouse, area, country, city, street, number, createDate);
    }

    public HouseRequest buildHouseRequest() {
        return new HouseRequest(area, country, city, street, number);
    }
}
