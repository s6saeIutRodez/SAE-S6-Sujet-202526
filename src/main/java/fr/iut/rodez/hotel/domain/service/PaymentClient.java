package fr.iut.rodez.hotel.domain.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentClient {
    
    public boolean authorize(String creditCard, BigDecimal amount) {
        if (creditCard == null || creditCard.length() < 8) return false;
        if (amount == null || amount.signum() <= 0) return false;
        
        return (creditCard.hashCode() & 1) == 0;
    }
}
