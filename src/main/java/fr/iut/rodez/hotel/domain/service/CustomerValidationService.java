package fr.iut.rodez.hotel.domain.service;

public class CustomerValidationService {
    public static CustomerDto splitCustomer(String input) {

        String[] parts = input.trim().split(" ");

        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid format");
        }

        String firstName = parts[0];
        String lastName = parts[1];
        String email = parts[2];

        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }

        return new CustomerDto(firstName, lastName, email);
    }
}
