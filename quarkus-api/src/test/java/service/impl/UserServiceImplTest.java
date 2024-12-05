package service.impl;


import dto.Response.UserResponseDto;
import dto.UserCreateDto;
import model.User;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;


import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl underTest;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findUserById() {

        Long userId = 1L;
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        User user = new User();
        user.setId(userId);
        user.setName("Mario");
        user.setEmail("am4063228@gmail.com");
        user.setPassword("12345");

        when(userRepository.findByIdOptional(userId)).thenReturn(Optional.of(user));

        UserResponseDto result = underTest.findUserById(userId);


        verify(userRepository,times(1)).findByIdOptional(idCaptor.capture());

        Long captorId = idCaptor.getValue();

        assertEquals(userId, idCaptor.getValue(), "El ID capturado debería ser el mismo que el proporcionado");

    }



    @Test
    void createUser() {
        //Arrange
        ArgumentCaptor<User> userCaptor = forClass(User.class);
        UserCreateDto user = new UserCreateDto();
        user.setName("Mario");
        user.setEmail("am4063228@gmail.com");
        user.setPassword("12345");

        //Act
        underTest.createUser(user);

        //Assert
        verify(userRepository, times(1)).persist(userCaptor.capture());

        User captorUser = userCaptor.getValue();

        assertEquals("Mario", captorUser.getName());
        assertEquals("am4063228@gmail.com", captorUser.getEmail());

        assertNotEquals("12345", captorUser.getPassword());
        assertTrue(BcryptUtil.matches("12345", captorUser.getPassword()));


    }


    @Test
    void updateUser() {

        //Arrange
        ArgumentCaptor<User> userCaptor = forClass(User.class);
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Mario");
        user.setEmail("am4063228@gmail.com");
        user.setPassword("12345");

        UserCreateDto userDto = new UserCreateDto();
        userDto.setName("Alberto");
        userDto.setEmail("mar@gmail.com");

        when(userRepository.findByIdOptional(userId)).thenReturn(Optional.of(user));

        //Act
        UserResponseDto result = underTest.updateUser(userId,userDto);

        //Assert
        verify(userRepository, times(1)).persist(userCaptor.capture());

        User captorUser = userCaptor.getValue();
        assertEquals("Alberto", captorUser.getName(), "El nombre del usuario debería ser 'Alberto'");
        assertEquals("mar@gmail.com", captorUser.getEmail(), "El correo del usuario debería ser 'mar@gmail.com'");

    }

    @Test
    void deleteUser() {
        //Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Mario");
        user.setEmail("am4063228@gmail.com");
        user.setPassword("12345");

        when(userRepository.findById(userId)).thenReturn(user);
        when(userRepository.deleteById(userId)).thenReturn(true);

        //Act
       boolean result = underTest.deleteUser(userId);

        //Assert
        verify(userRepository, times(1)).deleteById(userId);

        assertTrue(result);





    }

    @Test
    void isEmpty() {


    }
}