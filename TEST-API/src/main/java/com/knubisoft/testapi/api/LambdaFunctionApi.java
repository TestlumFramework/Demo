package com.knubisoft.testapi.api;

import com.knubisoft.testapi.exception.ErrorResponse;
import com.knubisoft.testapi.model.MongoBook;
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
@RequestMapping(value = "/lambda")
public interface LambdaFunctionApi {

    @GetMapping("/books/{title}")
    @Operation(summary = "Get MongoBook by title", description = "Return MongoBook by title")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = MongoBook.class))}
    )
    ResponseEntity<MongoBook> getBookByTitle(
            @Parameter(description = "Title of MongoBook to be searched", in = ParameterIn.PATH, required = true)
            @PathVariable("title") String title);

    @GetMapping("/books")
    @Operation(summary = "Get list of all MongoBooks", description = "Return ordered list of all MongoBooks")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = MongoBook.class)))}
    )
    ResponseEntity<List<MongoBook>> getAllBooks();

    @PostMapping("/books")
    @Operation(summary = "Add a MongoBook", description = "Create new MongoBook from requested data")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = MongoBook.class))}
    )
    ResponseEntity<MongoBook> addMongoBook(@Parameter(description = "Data for MongoBook", required = true)
                                           @RequestBody @Valid MongoBook mongoBook);
}
