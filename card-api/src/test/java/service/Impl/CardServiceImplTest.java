package service.Impl;

import client.UserClient;
import dto.CardDto;
import dto.response.ResponseCardDto;
import jakarta.ws.rs.core.Response;
import model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.CardRepository;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardServiceImplTest {
    @Mock
    CardRepository cardRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    CardServiceImpl underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findCardById() {

        //Arrange
        ArgumentCaptor<Long> cardCaptor = forClass(Long.class);
        Long cardId = 1L;
        Long cardId2 = 2L;
        Card card = new Card();
        card.setId(cardId);
        card.setCardNumber("1234567891012345");
        card.setUserId(1L);
        when(cardRepository.findById(cardId)).thenReturn(card);

        //Act

        ResponseCardDto cardDto = underTest.findCardById(cardId);

        //Assert
        verify(cardRepository).findById(cardCaptor.capture());

        Long captorValue = cardCaptor.getValue();

        assertEquals(cardId, cardCaptor.getValue());
        assertEquals("1234567891012345",cardDto.getCardNumber());

    }

    @Test
    void createCard() {
        //Arrange
        ArgumentCaptor<Card> cardCaptor = forClass(Card.class);
        CardDto card = new CardDto();
        card.setCardNumber("1234567891012345");
        card.setUserId(1L);
        card.setBalance(100.0);
        Response useResponse = Response.ok().build();

        when(userClient.getUserById(1L)).thenReturn(useResponse);


        //Act
        ResponseCardDto cardDto = underTest.createCard(card);

        //Assert
        verify(cardRepository).persist(cardCaptor.capture());

        Card captorValue = cardCaptor.getValue();

        assertEquals("1234567891012345", captorValue.getCardNumber());
        assertEquals(1L, captorValue.getUserId());
        assertEquals(100.0, captorValue.getBalance(), 0.01);


        assertNotNull(cardDto);
        assertEquals("1234567891012345", cardDto.getCardNumber());
        assertEquals(1L, cardDto.getUserId());
        assertEquals(100.0, cardDto.getBalance(), 0.01);
    }

    @Test
    void updateCard() {
        //Arrange
        ArgumentCaptor<Card> cardCaptor = forClass(Card.class);
        Long cardId = 1L;
        Card card = new Card();
        card.setId(cardId);
        card.setCardNumber("1234567891012345");
        card.setUserId(1L);
        card.setBalance(100.0);

        CardDto cardDto = new CardDto();
        cardDto.setCardNumber("123456789");
        cardDto.setUserId(1L);
        cardDto.setBalance(150.0);

        when(cardRepository.findById(cardId)).thenReturn(card);

        //Act
        ResponseCardDto result = underTest.updateCard(cardId, cardDto);

        //Arrange
        verify(cardRepository).persist(cardCaptor.capture());

        Card captorValue = cardCaptor.getValue();



        assertEquals(result.getCardNumber(), captorValue.getCardNumber());
        assertEquals(result.getUserId(), captorValue.getUserId());
        assertEquals(result.getBalance(), captorValue.getBalance(), 0.01);

    }

    @Test
    void deleteCard() {

        //Arrange
        ArgumentCaptor<Long> cardCaptor = forClass(Long.class);
        Long cardId = 1L;

        when(cardRepository.deleteById(cardId)).thenReturn(true);

        //Act

        Boolean result = underTest.deleteCard(cardId);

        //Assert

        verify(cardRepository).deleteById(cardCaptor.capture());

        Long cardValue = cardCaptor.getValue();
        assertTrue(result);


    }
}