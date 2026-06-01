package fr.iut.rodez.hotel.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Spring Data JPA pour RoomTypeJpaEntity.
 * Cible l'entité JPA (et non le modèle domaine) — c'est l'adaptateur qui fait le mapping.
 */
interface JpaRoomTypeRepository extends JpaRepository<RoomTypeJpaEntity, Long> {}