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
import ru.clevertec.springboottask.service.ResidentService;
import ru.clevertec.springboottask.web.request.ResidenceRequest;
import ru.clevertec.springboottask.web.response.HouseResponse;
import ru.clevertec.springboottask.web.response.PersonResponse;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/residents")
@Tag(name = "ResidentController", description = "Documentation")
public class ResidentController {

    private final ResidentService residentService;
    private final HouseHistoryService houseHistoryService;
    private final HouseMapper houseMapper;
    private final PersonMapper personMapper;

    @Operation(summary = "Get residents by uuid house")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get residents by uuid house", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/{uuidHouse}")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> getResidents(@PathVariable UUID uuidHouse){
        return personMapper.dtoListToResponseList(residentService.getResidents(uuidHouse));
    }

    @Operation(summary = "Get house by uuid person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get house by uuid person", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = HouseResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "House not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/houses/{uuidPerson}")
    @ResponseStatus(HttpStatus.OK)
    public HouseResponse findHouseLive(
            @PathVariable UUID uuidPerson) {
        return houseMapper.dtoToResponse(residentService.getHouseLivePerson(uuidPerson));
    }

    @Operation(summary = "Change house by uuid person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Change house by uuid person", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String changeResidence(
            @Valid @RequestBody ResidenceRequest changeResidenceRequest) {
        residentService.changeResidence(changeResidenceRequest);
        return String.format("House with uuid %s add resident with uuid %s",
                changeResidenceRequest.getUuidHouse().toString(),
                changeResidenceRequest.getUuidPerson().toString());
    }

    @Operation(summary = "Find all tenants ever lived in house by house uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find all tenants ever lived in house by house uuid", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PersonResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "House not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/ever-lived/{uuidHouse}")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> findAllTenantsEverLivedInHouse(@PathVariable UUID uuidHouse){
        return personMapper.dtoListToResponseList(houseHistoryService.findAllTenantsEverLivedInHouse(uuidHouse));
    }

    @Operation(summary = "Find all houses ever lived person by person uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find all tenants ever lived in house by house uuid", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = HouseResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/houses/ever-lived/{uuidPerson}")
    @ResponseStatus(HttpStatus.OK)
    public List<HouseResponse> findAllHousesEverLivedPerson(@PathVariable UUID uuidPerson){
        return houseMapper.dtoListToResponseList(houseHistoryService.findAllHousesEverLivedPerson(uuidPerson));
    }

}
