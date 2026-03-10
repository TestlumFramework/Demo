package com.knubisoft.testapi.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(final Class<?> resource, long id) {
        super(String.format("<%s> resource with id <%s> not found", resource.getSimpleName(), id));
    }

    public ResourceNotFoundException(final Class<?> resource, String name) {
        super(String.format("<%s> resource with name <%s> not found", resource.getSimpleName(), name));
    }

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    public ResourceNotFoundException(final Throwable cause) {
        super(cause);
    }

    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
