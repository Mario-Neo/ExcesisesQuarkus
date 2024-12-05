package service;

import client.UserClient;
import dto.CardDto;
import dto.response.ResponseCardDto;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import model.Card;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.CardRepository;
import service.Impl.CardServiceImpl;

import java.util.List;
import java.util.NoSuchElementException;

public interface CardService {


    public List<ResponseCardDto> findAllCards();

    public ResponseCardDto findCardById(Long id);

    public ResponseCardDto createCard(CardDto cardDto);

    public ResponseCardDto updateCard(Long id, CardDto updateCardDto );

    public boolean deleteCard(Long id);

}
