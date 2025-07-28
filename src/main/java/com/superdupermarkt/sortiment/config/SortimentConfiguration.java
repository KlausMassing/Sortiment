package com.superdupermarkt.sortiment.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.superdupermarkt.sortiment.domain.repository.KalkulationRepository;
import com.superdupermarkt.sortiment.domain.service.KalkulationService;
import com.superdupermarkt.sortiment.domain.util.ParserFactory;

@Configuration
public class SortimentConfiguration {

    @Bean("parserFactory")
    public FactoryBean<?> serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        // Set the factory interface that Spring will implement
        factoryBean.setServiceLocatorInterface(ParserFactory.class);
        return factoryBean;
    }

    @Bean
    public KalkulationService kalkulationService(KalkulationRepository kalkulationRepository) {
        KalkulationService kalkulationService = new KalkulationService(kalkulationRepository);
        // laden der Kalkulationen für Käse und Wein
        kalkulationService.initKalkulation();
        return kalkulationService;

    }

}
