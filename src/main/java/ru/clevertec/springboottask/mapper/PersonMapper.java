package ru.clevertec.springboottask.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.clevertec.springboottask.entity.dto.PersonDto;
import ru.clevertec.springboottask.entity.model.Person;
import ru.clevertec.springboottask.web.request.PersonRequest;
import ru.clevertec.springboottask.web.response.PersonResponse;

import java.util.List;

@Mapper
public interface PersonMapper {

	@Mapping(ignore = true, target = "id")
	List<PersonDto> entityListToDtoList(List<Person> people);

	PersonDto entityToDto(Person person);

	Person requestToModel(PersonRequest personRequest);

	PersonResponse dtoToResponse(PersonDto personDto);

	List<PersonResponse> dtoListToResponseList(List<PersonDto> people);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createDate", ignore = true)
	void mergeEntity(@MappingTarget Person target, PersonRequest source);

}
