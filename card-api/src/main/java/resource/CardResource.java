package resource;


import dto.CardDto;
import dto.response.ResponseCardDto;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Card;
import service.Impl.CardServiceImpl;
import jakarta.ws.rs.*;

import java.util.List;
import java.util.NoSuchElementException;


@Path("/cards")

public class CardResource {


    @Inject
    CardServiceImpl cardService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ResponseCardDto> getListAllCards(){
        return cardService.findAllCards();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getCardById(@PathParam("id") Long id) {
        try {
            ResponseCardDto responseCardDto = cardService.findCardById(id);
            return Response.ok(responseCardDto).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Card no encontrada: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCard(@Valid CardDto cardDto) {
        try {
            ResponseCardDto responseCardDto = cardService.createCard(cardDto);
            return Response.status(Response.Status.CREATED)
                    .entity(responseCardDto)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al crear la card: " + e.getMessage())
                    .build();
        }
    }



    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCard(@PathParam("id") Long id, @Valid CardDto updateCardDto) {
        try {
            ResponseCardDto responseCardDto = cardService.updateCard(id, updateCardDto);
            return Response.ok(responseCardDto).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Card no encontrada: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCard(@PathParam("id") Long id) {
        return cardService.deleteCard(id) ? Response.ok("Card eliminada").build()
                : Response.status(Response.Status.NOT_FOUND).entity("No se encontró la card con este ID").build();
    }


}
