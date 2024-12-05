package resource;

import dto.Response.UserResponseDto;
import dto.UserCreateDto;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.User;

import service.impl.UserServiceImpl;

import java.util.List;
import java.util.NoSuchElementException;

@Path("/users")
public class UserResource {


    @Inject
    private UserServiceImpl userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserResponseDto> getListAllUsers() {
        return userService.findAllUsers();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        try {
            UserResponseDto userResponseDto = userService.findUserById(id);
            return Response.ok(userResponseDto).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("el usuario no fue encontrado: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid UserCreateDto userCreateDto) {
        try {
            UserResponseDto userResponseDto = userService.createUser(userCreateDto);
            return Response.status(Response.Status.CREATED)
                    .entity(userResponseDto)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("error al crear el usuario: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, @Valid UserCreateDto updateUserCreateDto) {
        try {
            UserResponseDto userResponseDto = userService.updateUser(id, updateUserCreateDto);
            return Response.ok(userResponseDto).build();
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("este suario no fue encontrado: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        return userService.deleteUser(id) ? Response.ok("El usuario ha sido eliminado").build()
                : Response.status(Response.Status.NOT_FOUND).entity("No se ha podido eliminar el usuario").build();
    }

    @GET
    @Path("/all")
    public Response responseGetAllUsers() {
        if (userService.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No hay usuarios")
                    .build();
        } else {
            int countUsers = userService.findAllUsers().size();
            return Response.ok()
                    .entity("Total de usuarios: " + countUsers)
                    .header("X-WithHead", "buenos días")
                    .build();
        }
    }

    @GET
    @Path("/correo/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public User correoConsulta(@PathParam("email") String email) {
        return userService.sacarCorreoDeUsuario(email)
                .orElseThrow(() -> new NoSuchElementException("No hay registro del correo: " + email));
    }
}
