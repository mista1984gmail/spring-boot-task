package ru.clevertec.springboottask.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.springboottask.mapper.HouseMapper;
import ru.clevertec.springboottask.mapper.PersonMapper;
import ru.clevertec.springboottask.service.HouseHistoryService;
import ru.clevertec.springboottask.service.OwnerService;
import ru.clevertec.springboottask.web.request.OwnerRequest;
import ru.clevertec.springboottask.web.response.HouseResponse;
import ru.clevertec.springboottask.web.response.PersonResponse;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owners")
@Tag(name = "OwnerController", description = "Documentation")
public class OwnerController {

    private final OwnerService ownerService;
    private final HouseHistoryService houseHistoryService;
    private final HouseMapper houseMapper;
    private final PersonMapper personMapper;

    @Operation(summary = "Get owners by uuid house")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get owners by uuid house", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/{uuidHouse}")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> getOwnersByUuidHouse(@PathVariable UUID uuidHouse){
        return personMapper.dtoListToResponseList(ownerService.findAllOwners(uuidHouse));
    }

    @Operation(summary = "Add owner to house")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add owner to house", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OwnerRequest.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String addOwner(@Valid @RequestBody OwnerRequest ownerRequest){
        ownerService.addOwner(ownerRequest);
        return String.format("House with uuid %s add owner with uuid %s", ownerRequest.getUuidHouse().toString(), ownerRequest.getUuidPerson().toString());
    }

    @Operation(summary = "Remove owner from house")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Remove owner from house", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OwnerRequest.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public String removeOwner(@Valid @RequestBody OwnerRequest ownerRequest){
        ownerService.removeOwner(ownerRequest);
        return String.format("Owner with uuid %s removed from House with uuid %s", ownerRequest.getUuidPerson().toString(), ownerRequest.getUuidHouse().toString());
    }

    @Operation(summary = "Get all houses by owner uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Remove owner from house", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = HouseResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/houses/{uuidPerson}")
    @ResponseStatus(HttpStatus.OK)
    public List<HouseResponse> getAllHousesOwner(@PathVariable UUID uuidPerson){
        return houseMapper.dtoListToResponseList(ownerService.findAllHousesPerson(uuidPerson));
    }

    @Operation(summary = "Find all owners ever owned house by house uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find all owners ever owned house by house uuid", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "House not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/ever-owned/{uuidHouse}")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> findAllOwnersEverOwnedHouse(@PathVariable UUID uuidHouse){
        return personMapper.dtoListToResponseList(houseHistoryService.findAllOwnersEverOwnedHouse(uuidHouse));
    }

    @Operation(summary = "Find all houses ever owned by person by person uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find all houses ever owned by person by person uuid", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = HouseResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/houses/ever-owned/{uuidPerson}")
    @ResponseStatus(HttpStatus.OK)
    public List<HouseResponse> findAllHousesEverOwnedPerson(@PathVariable UUID uuidPerson){
        return houseMapper.dtoListToResponseList(houseHistoryService.findAllHousesEverOwnedPerson(uuidPerson));
    }

}
