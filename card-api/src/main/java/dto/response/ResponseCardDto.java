package dto.response;

import jakarta.validation.constraints.NotBlank;


public class ResponseCardDto {

    private String cardNumber;

    private Long userId;

    private Double balance;

    private String message;

    public ResponseCardDto(String cardNumber, Long userId, Double balance) {
        this.cardNumber = cardNumber;
        this.userId = userId;
        this.balance = balance;
    }

    public ResponseCardDto(String cardNumber, Long userId, Double balance, String message) {
        this.cardNumber = cardNumber;
        this.userId = userId;
        this.balance = balance;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
