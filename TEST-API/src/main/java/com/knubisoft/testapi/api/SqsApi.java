package com.knubisoft.testapi.api;

import com.knubisoft.testapi.dto.MessageResponse;
import com.knubisoft.testapi.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

@ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))})
})
@RequestMapping(value = "/queue/sqs",
        produces = MediaType.APPLICATION_JSON_VALUE)
public interface SqsApi {

    @PostMapping("/send")
    @Operation(summary = "Send message", description = "Send a message to the queue")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(description = "Return string with your message"))})
    ResponseEntity<String> sendMessageToSqs(@Parameter(description = "Message for send", required = true)
                                            @RequestBody String message);

    @GetMapping("/receive")
    @Operation(summary = "Get first in message", description = "Return first in message")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(description = "List of message with its` metadata")))}
    )
    ResponseEntity<List<MessageResponse>> receiveMessageFromSqs();

    @PostMapping("/send/{queue}")
    @Operation(summary = "Send message", description = "Send a message to the queue")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(description = "Return string with your message"))})
    ResponseEntity<String> sendMessageToSqsNewQueue(@Parameter(description = "Message for send", required = true)
                                                    @RequestBody String message,
                                                    @Parameter(description = "Queue for send", required = true)
                                                    @PathVariable String queue);

    @GetMapping("/receive/{queue}")
    @Operation(summary = "Get first in message", description = "Return first in message")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(description = "List of message with its` metadata")))}
    )
    ResponseEntity<List<MessageResponse>> receiveMessageFromSqsNewQueue(@Parameter(description = "Queue for receive", required = true)
                                                                @PathVariable String queue);
}
