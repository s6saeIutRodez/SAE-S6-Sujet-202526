package fr.adriencaubel.hotel.api.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BookingRequest {
    
    @NotNull
    public Long roomTypeId;
    
    @NotNull
    public LocalDate from;
    
    @NotNull
    public LocalDate to;

    @NotNull
    public String nomPrenomEmail;
    
    @NotNull
    public BigDecimal amount;
    
    public int quantity = 1;

    public List<String> options = List.of();
}
