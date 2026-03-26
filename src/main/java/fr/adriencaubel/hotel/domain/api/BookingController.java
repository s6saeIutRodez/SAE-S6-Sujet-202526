package fr.adriencaubel.hotel.domain.api;

import fr.adriencaubel.hotel.domain.api.dto.BookingRequest;
import fr.adriencaubel.hotel.domain.Booking;
import fr.adriencaubel.hotel.domain.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bookings")
public class BookingController {
    
    private final HotelService hotelService;
    
    public BookingController(HotelService hotelService) {
        this.hotelService = hotelService;
    }
    
    @PostMapping
    public Booking reserve(@RequestBody @Valid BookingRequest req) {
        return hotelService.reserveRoom(req);
    }
}
