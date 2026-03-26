package fr.adriencaubel.hotel.domain.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {
    private  String firstName;
    private  String lastName;
    private  String email;

    public CustomerDto(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
