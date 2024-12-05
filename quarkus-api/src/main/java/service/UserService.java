package service;


import dto.Response.UserResponseDto;
import dto.UserCreateDto;
import model.User;


import java.util.List;
import java.util.Optional;


public interface UserService {

     List<UserResponseDto> findAllUsers();

     UserResponseDto findUserById(Long id);

     UserResponseDto createUser(UserCreateDto userCreateDto);
    
     UserResponseDto updateUser(Long id, UserCreateDto updateUserCreateDto);
    
     boolean deleteUser(Long id);
        
     boolean isEmpty();

     Optional<User> sacarCorreoDeUsuario(String email);


}
