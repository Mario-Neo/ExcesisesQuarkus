package service.impl;

import dto.UserCreateDto;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import model.User;
import repository.UserRepository;
import service.UserService;
import io.quarkus.elytron.security.common.BcryptUtil;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import dto.Response.UserResponseDto;


@ApplicationScoped
@Authenticated
public class UserServiceImpl implements UserService {


    @Inject
    private UserRepository userRepository;

    public UserResponseDto toUserResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public List<UserResponseDto> findAllUsers() {
        return userRepository.listAll()
                .stream().map(this::toUserResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public UserResponseDto findUserById(Long id) {
        return userRepository.findByIdOptional(id)
                .map(this::toUserResponseDto)
                .orElseThrow(() -> new NoSuchElementException("El usuario con el id " + id + " no existe"));
    }


    @Override
    @Transactional
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        try {
            User user = new User();
            user.setName(userCreateDto.getName());
            user.setEmail(userCreateDto.getEmail());
            user.setPassword(BcryptUtil.bcryptHash(userCreateDto.getPassword()));
            userRepository.persist(user);
            return toUserResponseDto(user);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el usuario", e);
        }
    }

    public UserResponseDto updateUser(Long id, UserCreateDto updateUserCreateDto) {
        User user = userRepository.findByIdOptional(id)
                .orElseThrow(() -> new NoSuchElementException("El usuario con el id " + id + " que intentas actualizar no existe"));
        user.setName(updateUserCreateDto.getName());
        user.setEmail(updateUserCreateDto.getEmail());
        userRepository.persist(user);
        return toUserResponseDto(user);
    }

    @Override
    @Transactional
    public boolean deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    @Override
    public boolean isEmpty() {
        return findAllUsers().isEmpty();
    }

    @Override
    public Optional<User> sacarCorreoDeUsuario(String email) {
        return userRepository.listAll().stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny();
    }


}


