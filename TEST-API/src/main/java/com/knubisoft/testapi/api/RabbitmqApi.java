package com.knubisoft.testapi.api;

import com.knubisoft.testapi.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.List;

@ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))})
})
@RequestMapping(value = "/queue/rabbit",
        produces = MediaType.APPLICATION_JSON_VALUE)
public interface RabbitmqApi {

    @PostMapping("/send/{queue}")
    @Operation(summary = "Send message", description = "Send a message to the queue")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(description = "Return string with your message"))})
    ResponseEntity<String> sendMessageToQueue(@Parameter(description = "Message for send", required = true)
                                              @RequestBody String message,
                                              @Parameter(description = "Queue for send", required = true)
                                              @PathVariable String queue);

    @GetMapping("/receive/{queue}")
    @Operation(summary = "Get first in message", description = "Return first in message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(description = "Return string with your message"))}),
            @ApiResponse(responseCode = "404", description = "Messages not found", content = {@Content(
                    schema = @Schema(implementation = ErrorResponse.class))})
    })
    ResponseEntity<String> receiveMessageFromQueue(@Parameter(description = "Queue for send", required = true)
                                                   @PathVariable String queue);

    @GetMapping("/receive/all/{queue}")
    @Operation(summary = "Get list of all messages", description = "Return list of all messages")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(description = "List of messages")))}
    )
    ResponseEntity<List<String>> receiveAllMessagesFromQueue(@Parameter(description = "Queue for send", required = true)
                                                             @PathVariable String queue);
}
