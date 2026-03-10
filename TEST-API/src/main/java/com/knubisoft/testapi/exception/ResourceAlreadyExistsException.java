package com.knubisoft.testapi.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(final Class<?> resource, final int id) {
        super(String.format("<%s> resource with id <%s> already exists", resource.getSimpleName(), id));
    }

    public ResourceAlreadyExistsException(final Class<?> resource, final String name) {
        super(String.format("<%s> resource with name <%s> already exists", resource.getSimpleName(), name));
    }

    public ResourceAlreadyExistsException(final String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(final Throwable cause) {
        super(cause);
    }

    public ResourceAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
