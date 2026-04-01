// infrastructure/persistence/JpaBookingRepository.java
package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaBookingRepository extends JpaRepository<Booking, Long> {}