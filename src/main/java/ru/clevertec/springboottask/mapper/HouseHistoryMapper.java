package ru.clevertec.springboottask.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.springboottask.entity.dto.HouseHistoryDto;
import ru.clevertec.springboottask.entity.model.HouseHistory;

@Mapper
public interface HouseHistoryMapper {

	HouseHistory dtoToEntity(HouseHistoryDto houseHistoryDto);

}
