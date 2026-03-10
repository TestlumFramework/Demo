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
@RequestMapping(value = "/queue/kafka", produces = MediaType.APPLICATION_JSON_VALUE)
public interface KafkaApi {

    @PostMapping("/send")
    @Operation(summary = "Send message", description = "Send a message to default topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(description = "Return string with your message"))}),
            @ApiResponse(responseCode = "204", description = "No content", content = @Content)
    })
    ResponseEntity<String> sendKafkaMessageToDefaultTopic(@Parameter(description = "Message for send", required = true)
                                                          @RequestBody String message);

    @GetMapping("/receive")
    @Operation(summary = "Get list of all messages", description = "Return list of all messages from default topic")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(description = "List of messages")))}
    )
    ResponseEntity<List<String>> receiveKafkaMessagesFromDefaultTopic();

    @PostMapping("/send/{topic}")
    @Operation(summary = "Send message", description = "Send a message to the topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(description = "Return string with your message"))}),
            @ApiResponse(responseCode = "204", description = "No content", content = @Content)
    })
    ResponseEntity<String> sendKafkaMessage(@Parameter(description = "Message for send", required = true)
                                            @RequestBody String message,
                                            @Parameter(description = "Topic for send", required = true)
                                            @PathVariable String topic);

    @GetMapping("/receive/{topic}")
    @Operation(summary = "Get list of all messages", description = "Return list of all messages")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(description = "List of messages")))}
    )
    ResponseEntity<List<String>> receiveKafkaMessages(@Parameter(description = "Topic for receive", required = true)
                                                      @PathVariable String topic);
}
