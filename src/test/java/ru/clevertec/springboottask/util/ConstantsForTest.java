package ru.clevertec.springboottask.util;

import ru.clevertec.springboottask.entity.enums.Sex;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConstantsForTest {

    public final static Long ID = 1L;
    public final static UUID UUID_HOUSE = UUID.fromString("c9362a9e-b783-46f9-9ffb-c8df6461634b");
    public final static Double AREA = 58.2;
    public final static String COUNTRY = "RB";
    public final static String CITY = "Minsk";
    public final static String STREET = "Pr. Pobeditelej";
    public final static Integer NUMBER = 156;
    public final static LocalDateTime CREATE_DATE = LocalDateTime.of(2023, 11, 17, 15, 00);
    public final static UUID UUID_PERSON = UUID.fromString("c7070b95-a77b-4183-9da8-4e4a2fee4bea");
    public final static String NAME = "Ivan";
    public final static String SURNAME = "Ivanov";
    public final static Sex SEX = Sex.MALE;
    public final static String PASSPORT_SERIES = "KH";
    public final static String PASSPORT_NUMBER = "1334567";
    public final static LocalDateTime UPDATE_DATE = LocalDateTime.of(2023, 11, 17, 15, 00);
    public final static Long LIVE_HOUSE_ID = 1L;

}
