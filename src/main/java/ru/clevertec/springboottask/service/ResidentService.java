package ru.clevertec.springboottask.service;

import ru.clevertec.springboottask.entity.dto.HouseDto;
import ru.clevertec.springboottask.entity.dto.PersonDto;
import ru.clevertec.springboottask.web.request.ResidenceRequest;

import java.util.List;
import java.util.UUID;

public interface ResidentService {

    List<PersonDto> getResidents(UUID uuidHouse);
    HouseDto getHouseLivePerson(UUID uuidPerson);
    void changeResidence(ResidenceRequest changeResidenceRequest);

}
