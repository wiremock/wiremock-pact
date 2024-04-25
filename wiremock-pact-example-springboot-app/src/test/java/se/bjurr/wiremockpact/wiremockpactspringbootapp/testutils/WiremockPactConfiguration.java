package se.bjurr.wiremockpact.wiremockpactspringbootapp.testutils;

import org.springframework.cloud.contract.wiremock.WireMockConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WiremockPactConfiguration {

	@Bean
    public WireMockConfigurationCustomizer configureWiremock() {
    	return (config) -> {
    		config.extensions(new WireMockPactExtension());
    	};
    }
}
