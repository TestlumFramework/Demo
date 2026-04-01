package com.knubisoft.testapi.api;

import com.knubisoft.testapi.dto.websocket.stomp.StompSelfMessage;
import com.knubisoft.testapi.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;

@ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))})
})
public interface WebSocketApi {

    @MessageMapping("/ping")
    void somePing(@RequestBody final String arg);

    @MessageMapping("/test")
    void someTest(@RequestBody final String arg);

    @MessageMapping("/server")
    void somePeriodicMessages();

    @MessageMapping("/self/create")
    void createSelf(@RequestBody final StompSelfMessage message);

    @MessageMapping("/self/get")
    void getSelf(@RequestBody final StompSelfMessage message);

    @MessageMapping("/self/delete")
    void deleteSelf(@RequestBody final StompSelfMessage message);

    @MessageMapping("/self/reset")
    void resetSelf();
}