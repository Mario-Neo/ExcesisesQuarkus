package exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.NoSuchElementException;

@Provider
public class NosSuchElementExceptionMapper implements ExceptionMapper<NoSuchElementException> {


    public static record NoSuchElementMessage(String message, String detail){

    }
    @Override
    public Response toResponse(NoSuchElementException e) {
        var error = new NoSuchElementMessage(e.getMessage(),null);
        return Response.status(404).entity(error).build();
    }
}
