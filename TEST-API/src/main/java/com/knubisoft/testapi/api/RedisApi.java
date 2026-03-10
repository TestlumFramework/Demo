package com.knubisoft.testapi.api;

import com.knubisoft.testapi.dto.EmployeeDto;
import com.knubisoft.testapi.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))})
})
@RequestMapping(value = "/cache/redis/employee", produces = MediaType.APPLICATION_JSON_VALUE)
public interface RedisApi {

    @PostMapping("/put")
    @Operation(summary = "Add an employee", description = "Add employee from requested data to cache")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = EmployeeDto.class))}
    )
    ResponseEntity<EmployeeDto> putInRedisCache(@Parameter(description = "Employee to save in cache", required = true)
                                                @RequestBody @Valid EmployeeDto employeeDto);

    @GetMapping("/get/list")
    @Operation(summary = "Get all employees", description = "Get all employees from cache")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(description = "List of employees")))}
    )
    ResponseEntity<List<EmployeeDto>> getEmployeeList();

    @GetMapping("/get/{id}")
    @Operation(summary = "Get an employee", description = "Get an employee from requested data by id")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = EmployeeDto.class))}
    )
    ResponseEntity<EmployeeDto> getEmployeeById(@Parameter(description = "Id of employee",
            in = ParameterIn.PATH, required = true) @PathVariable int id);

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete an employee", description = "Delete an employee from requested data by id")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(description = "Return string with id of deleted employee"))})
    ResponseEntity<String> deleteEmployeeById(@Parameter(description = "Id of employee",
            in = ParameterIn.PATH, required = true) @PathVariable int id);
}
