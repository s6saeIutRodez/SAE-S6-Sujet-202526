package fr.iut.rodez.hotel.domain.api.dto;

import java.util.UUID;

public class BookingResponse {
    
    public UUID bookingId;
    public String status;
    public String message;
    
    public BookingResponse(UUID bookingId, String status, String message) {
        this.bookingId = bookingId;
        this.status = status;
        this.message = message;
    }
}
