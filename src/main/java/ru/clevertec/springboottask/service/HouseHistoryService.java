package ru.clevertec.springboottask.service;


import ru.clevertec.springboottask.entity.dto.HouseDto;
import ru.clevertec.springboottask.entity.dto.HouseHistoryDto;
import ru.clevertec.springboottask.entity.dto.PersonDto;
import ru.clevertec.springboottask.entity.model.HouseHistory;

import java.util.List;
import java.util.UUID;

public interface HouseHistoryService {

	HouseHistory save(HouseHistoryDto houseHistory);

	List<PersonDto> findAllTenantsEverLivedInHouse(UUID uuidHouse);

	List<PersonDto> findAllOwnersEverOwnedHouse(UUID uuidHouse);

	List<HouseDto> findAllHousesEverLivedPerson(UUID uuidPerson);

	List<HouseDto> findAllHousesEverOwnedPerson(UUID uuidPerson);

}
