package ru.clevertec.springboottask.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.clevertec.springboottask.entity.dto.HouseDto;
import ru.clevertec.springboottask.entity.model.House;
import ru.clevertec.springboottask.web.request.HouseRequest;
import ru.clevertec.springboottask.web.response.HouseResponse;

import java.util.List;

@Mapper
public interface HouseMapper {

	@Mapping(ignore = true, target = "uuidHouse")
	@Mapping(ignore = true, target = "createDate")
	HouseDto requestToDto(HouseRequest houseRequest);

	HouseResponse dtoToResponse(HouseDto houseDto);

	@Mapping(ignore = true, target = "id")
	List<HouseDto> entityListToDtoList(List<House> houses);

	HouseDto entityToDto(House house);

	House dtoToEntity(HouseDto houseDto);

	List<HouseResponse> dtoListToResponseList(List<HouseDto> houses);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createDate", ignore = true)
	void mergeEntity(@MappingTarget House target, HouseRequest source);

}
