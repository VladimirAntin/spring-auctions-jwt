package org.auctions.sf57;

import org.auctions.sf57.security.JwtFilter;
import org.auctions.sf57.storage.StorageProperties;
import org.auctions.sf57.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Created by vladimir_antin on 8.5.17..
 * @GitHub https://github.com/VladimirAntin/jwt-angular-spring
 */
@SpringBootApplication  // same as @Configuration @EnableAutoConfiguration @ComponentScan
@EnableConfigurationProperties(StorageProperties.class)
public class AuctionsApp {
	
    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/api/*");

        return registrationBean;
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AuctionsApp.class, args);
    }
}
