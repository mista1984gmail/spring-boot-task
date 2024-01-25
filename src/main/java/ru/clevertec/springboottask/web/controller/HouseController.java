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
import ru.clevertec.springboottask.mapper.HouseMapper;
import ru.clevertec.springboottask.service.HouseSearchService;
import ru.clevertec.springboottask.service.HouseService;
import ru.clevertec.springboottask.util.Constants;
import ru.clevertec.springboottask.web.request.HouseRequest;
import ru.clevertec.springboottask.web.response.HouseResponse;

import java.util.List;
import java.util.UUID;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/houses")
@Tag(name = "HouseController", description = "Documentation")
public class HouseController {

    private final HouseService houseService;
    private final HouseSearchService houseSearchService;
    private final HouseMapper houseMapper;

    @Operation(summary = "Save House")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Save House", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = HouseResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public HouseResponse save(
            @Valid @RequestBody HouseRequest houseRequest) {
        return houseMapper.dtoToResponse(houseService.save(houseRequest));
    }

    @Operation(summary = "Get House by uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the House", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = HouseResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "House not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public HouseResponse findByUUID(@PathVariable UUID uuid) {
       return houseMapper.dtoToResponse(houseService.findByUUID(uuid));
    }

    @Operation(summary = "Delete House by uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete the House"),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "House not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteByUUID(@PathVariable UUID uuid) {
        houseService.delete(uuid);
        return String.format("House with uuid %s deleted", uuid.toString());
    }

    @Operation(summary = "Update House by uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update the House", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = HouseResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid uuid supplied"),
            @ApiResponse(responseCode = "404", description = "House not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public HouseResponse update(
            @Valid @RequestBody HouseRequest houseRequest,
            @PathVariable UUID uuid) {
        return houseMapper.dtoToResponse(houseService.update(uuid, houseRequest));
    }

    @Operation(summary = "Find all Houses with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find Houses with pagination", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = HouseResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<HouseResponse> findAll(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE) Integer page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(value = "orderBy", defaultValue = Constants.DEFAULT_HOUSE_ORDER_BY) String orderBy,
            @RequestParam(value = "direction", defaultValue = Constants.DEFAULT_DIRECTION) String direction)
    {
        return houseService.findAllWithPaginationAndSorting(page, size, orderBy, direction).map(houseMapper::dtoToResponse);
    }

    @Operation(summary = "Find all Houses by country, by city or street")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find Houses by country, by city or street", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = HouseResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/findBy")
    @ResponseStatus(HttpStatus.OK)
    public List<HouseResponse> findByCountryOrCityOrStreet(@RequestParam(value = "name") String name){
        return houseMapper.dtoListToResponseList(houseSearchService.findByCountryOrCityOrStreet(name));
    }

}
