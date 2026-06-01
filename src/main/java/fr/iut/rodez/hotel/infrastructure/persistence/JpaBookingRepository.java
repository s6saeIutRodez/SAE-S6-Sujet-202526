package fr.iut.rodez.hotel.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Spring Data JPA pour BookingJpaEntity.
 */
interface JpaBookingRepository extends JpaRepository<BookingJpaEntity, Long> {}