package com.knubisoft.testapi.api;

import com.knubisoft.testapi.dto.NewsDto;
import com.knubisoft.testapi.exception.ErrorResponse;
import com.knubisoft.testapi.model.postgres.PostgresNews;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
@RequestMapping(value = "/postgres")
public interface PostgresNewsApi {

    @GetMapping("/news/{id}")
    @Operation(summary = "Get news by id", description = "Return news by id")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = PostgresNews.class))}
    )
    ResponseEntity<PostgresNews> getNews(@Parameter(description = "Id of news", in = ParameterIn.PATH, required = true)
                                         @PathVariable Long id);

    @GetMapping("/news/by")
    @Operation(summary = "Search list of news by name", description = "Return list of news by name")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = PostgresNews.class)))}
    )
    ResponseEntity<List<PostgresNews>> getNewsByName(
            @Parameter(description = "Name of news to be searched", in = ParameterIn.QUERY, required = true)
            @RequestParam("name") String name);

    @PostMapping("/news")
    @Operation(summary = "Add a news", description = "Create news from requested data")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = PostgresNews.class))}
    )
    ResponseEntity<?> createNews(@Parameter(description = "Data for news", required = true)
                                 @RequestBody @Valid NewsDto dto);

    @GetMapping("/news")
    @Operation(summary = "Get list of all news", description = "Return list of all news")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = PostgresNews.class)))}
    )
    ResponseEntity<List<PostgresNews>> getAllNews();
}
