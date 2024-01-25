package ru.clevertec.springboottask.util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.springboottask.entity.dto.PersonDto;
import ru.clevertec.springboottask.entity.enums.Sex;
import ru.clevertec.springboottask.entity.model.Person;
import ru.clevertec.springboottask.web.request.PersonRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class PersonTestData {

    @Builder.Default
    private Long id = ConstantsForTest.ID;

    @Builder.Default
    private UUID uuidPerson = ConstantsForTest.UUID_PERSON;

    @Builder.Default
    private String name = ConstantsForTest.NAME;

    @Builder.Default
    private String surname = ConstantsForTest.SURNAME;

    @Builder.Default
    private Sex sex = ConstantsForTest.SEX;

    @Builder.Default
    private String passportSeries = ConstantsForTest.PASSPORT_SERIES;

    @Builder.Default
    private String passportNumber = ConstantsForTest.PASSPORT_NUMBER;

    @Builder.Default
    private LocalDateTime createDate = ConstantsForTest.CREATE_DATE;

    @Builder.Default
    private LocalDateTime updateDate = ConstantsForTest.UPDATE_DATE;

    @Builder.Default
    private Long liveHouseId = ConstantsForTest.LIVE_HOUSE_ID;

    public Person buildPerson() {
        return new Person(id, uuidPerson, name, surname, sex, passportSeries, passportNumber, createDate, updateDate);
    }

    public PersonDto buildPersonDto() {
        return new PersonDto(uuidPerson, name, surname, sex, passportSeries, passportNumber, createDate, updateDate);
    }

    public PersonRequest buildPersonRequest() {
        return new PersonRequest(name, surname, sex, passportSeries, passportNumber);
    }
}
