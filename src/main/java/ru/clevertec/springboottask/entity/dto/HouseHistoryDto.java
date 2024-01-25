package ru.clevertec.springboottask.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.springboottask.entity.enums.PersonType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseHistoryDto {

	private Long houseId;
	private Long personId;
	private PersonType personType;

}