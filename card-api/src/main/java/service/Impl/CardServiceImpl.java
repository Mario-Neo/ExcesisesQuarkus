package service.Impl;


import client.UserClient;
import dto.CardDto;
import dto.response.ResponseCardDto;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import model.Card;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import repository.CardRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.CardService;


@ApplicationScoped
@Authenticated
public class CardServiceImpl implements CardService {



    @Inject
    CardRepository cardRepository;


    @Inject
    @RestClient
    UserClient userClient;

    public ResponseCardDto toResponseCardDto(Card card) {
        return new ResponseCardDto(card.getCardNumber(), card.getUserId(), card.getBalance(), "Bienvenido");
    }

    public List<ResponseCardDto> findAllCards() {
        return cardRepository.listAll().stream()
                .map(this::toResponseCardDto)
                .collect(Collectors.toList());
    }

    public ResponseCardDto findCardById(Long id) {
        Card card = cardRepository.findById(id);
        if (card != null) {
            return toResponseCardDto(card);
        } else {
            throw new NoSuchElementException("La card con ese id no esta disponible: " + id);
        }
    }

    @Timeout(3000)
    @Fallback(fallbackMethod = "fallbackCreatedCard")
    @Transactional
    public ResponseCardDto createCard(CardDto cardDto) {
        Response userResponse = userClient.getUserById(cardDto.getUserId());
        if (userResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new IllegalArgumentException("Usuario no encontrado: " );
        }
        Card card = new Card();
        card.setCardNumber(cardDto.getCardNumber());
        card.setUserId(cardDto.getUserId());
        card.setBalance(cardDto.getBalance());
        cardRepository.persist(card);
        return toResponseCardDto(card);

    }

    public ResponseCardDto fallbackCreatedCard(CardDto cardDto) {
        return new ResponseCardDto(
                "00000000000",
                cardDto.getUserId(),
                0.0,
                "El servicio no esta disponible La tarjeta no pudo ser creada."
        );
    }



    @Transactional
    public ResponseCardDto updateCard(Long id, CardDto updateCardDto) {
        Card card = cardRepository.findById(id);
        if (card != null) {
            card.setCardNumber(updateCardDto.getCardNumber());
            card.setUserId(updateCardDto.getUserId());
            card.setBalance(updateCardDto.getBalance());
            cardRepository.persist(card);
            return toResponseCardDto(card);
        }
        throw new NoSuchElementException("El libro con el id:" + id + "que intentas actualizar no existe");
    }

    @Transactional
    public boolean deleteCard(Long id) {
        return cardRepository.deleteById(id);
    }


}
