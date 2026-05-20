package fr.iut.rodez.hotel.infrastructure.config;

import fr.iut.rodez.hotel.domain.port.out.InventoryRepository;
import fr.iut.rodez.hotel.domain.service.AvailabilityDomainService;
import fr.iut.rodez.hotel.domain.service.PricingDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public PricingDomainService pricingDomainService() {
        return new PricingDomainService();
    }

    @Bean
    public AvailabilityDomainService availabilityDomainService(InventoryRepository inventoryRepository) {
        return new AvailabilityDomainService(inventoryRepository);
    }
}