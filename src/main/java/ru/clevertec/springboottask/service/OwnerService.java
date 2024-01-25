package ru.clevertec.springboottask.service;


import ru.clevertec.springboottask.entity.dto.HouseDto;
import ru.clevertec.springboottask.entity.dto.PersonDto;
import ru.clevertec.springboottask.web.request.OwnerRequest;

import java.util.List;
import java.util.UUID;

public interface OwnerService {

    List<PersonDto> findAllOwners(UUID uuidHouse);
    List<HouseDto> findAllHousesPerson(UUID uuidPerson);

    void addOwner(OwnerRequest ownerRequest);

    void removeOwner(OwnerRequest ownerRequest);

}
