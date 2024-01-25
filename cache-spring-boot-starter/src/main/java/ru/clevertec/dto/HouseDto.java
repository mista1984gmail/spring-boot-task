package ru.clevertec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseDto {

	private UUID uuidHouse;
	private Double area;
	private String country;
	private String city;
	private String street;
	private Integer number;
	private LocalDateTime createDate;

}
