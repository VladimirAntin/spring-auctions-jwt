package org.auctions.sf57;

import org.auctions.sf57.security.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Created by vladimir_antin on 8.5.17..
 */
@SpringBootApplication  // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class AuctionsApp {
	
    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/api/*");

        return registrationBean;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AuctionsApp.class, args);
    }
}
