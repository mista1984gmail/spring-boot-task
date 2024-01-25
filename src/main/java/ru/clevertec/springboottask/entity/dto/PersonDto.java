package ru.clevertec.springboottask.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.springboottask.entity.enums.Sex;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

	private UUID uuidPerson;
	private String name;
	private String surname;
	private Sex sex;
	private String passportSeries;
	private String passportNumber;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;

}
