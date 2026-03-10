package com.knubisoft.testapi.exception;

public class QueueNotFoundException extends RuntimeException {

    public QueueNotFoundException(final String queue) {
        super(String.format("Queue with name <%s> not found", queue));
    }

}
