package fr.adriencaubel.hotel.domain.api.dto;

import java.time.LocalDate;
import java.util.UUID;

public class AvailabilityResponse {
    
    public Long roomTypeId;
    public LocalDate from;
    public LocalDate to;
    public boolean available;
    public int remainingMin; // minimum remaining across days
    
    public AvailabilityResponse(Long roomTypeId, LocalDate from, LocalDate to,
                                boolean available, int remainingMin) {
        this.roomTypeId = roomTypeId;
        this.from = from;
        this.to = to;
        this.available = available;
        this.remainingMin = remainingMin;
    }
}
