package ru.clevertec.springboottask.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.springboottask.mapper.PersonMapper;
import ru.clevertec.springboottask.service.PersonService;
import ru.clevertec.springboottask.util.Constants;
import ru.clevertec.springboottask.web.request.PersonRequest;
import ru.clevertec.springboottask.web.response.HouseResponse;
import ru.clevertec.springboottask.web.response.PersonResponse;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/persons")
@Tag(name = "PersonController", description = "Documentation")
public class PersonController {

    private final PersonService personService;
    private final PersonMapper personMapper;

    @Operation(summary = "Save Person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Save Person", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PersonResponse save(
            @Valid @RequestBody PersonRequest personRequest,
            @RequestParam(value = "house_uuid") UUID houseUUID) {
        return personMapper.dtoToResponse(personService.save(personRequest, houseUUID));
    }

    @Operation(summary = "Get Person by uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Person", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponse findByUUID(@PathVariable UUID uuid) {
       return personMapper.dtoToResponse(personService.findByUUID(uuid));
    }

    @Operation(summary = "Delete Person by uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete the Person"),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteByUUID(@PathVariable UUID uuid) {
        personService.delete(uuid);
        return String.format("Person with uuid %s deleted", uuid.toString());
    }

    @Operation(summary = "Update Person by uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update the Person", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponse update(
            @Valid @RequestBody PersonRequest personRequest,
            @PathVariable UUID uuid) {
        return personMapper.dtoToResponse(personService.update(uuid, personRequest));
    }

    @Operation(summary = "Find all Persons with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find Persons with pagination", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = HouseResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PersonResponse> findAllWithPaginationAndSorting(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE) Integer page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(value = "orderBy", defaultValue = Constants.DEFAULT_PERSON_ORDER_BY) String orderBy,
            @RequestParam(value = "direction", defaultValue = Constants.DEFAULT_DIRECTION) String direction)
    {
        return personService.findAllWithPagination(page, size, orderBy, direction).map(personMapper::dtoToResponse);
    }

}
