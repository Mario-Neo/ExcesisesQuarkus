package exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import java.util.NoSuchElementException;

public class NoSuchElementMappers implements ExceptionMapper<NoSuchElementException> {
    public static record NoSuchElementMessage(String message, String detail) {}

    @Override
    public Response toResponse(NoSuchElementException e) {
        var error = new NoSuchElementMessage(e.getMessage(), null);
        return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
    }
}
