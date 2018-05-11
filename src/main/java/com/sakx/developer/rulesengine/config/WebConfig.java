package com.sakx.developer.rulesengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public WebMvcConfigurerAdapter adapter() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("swagger-ui.html")
						.addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
				registry.addResourceHandler("/webjars/**")
						.addResourceLocations("classpath:/META-INF/resources/webjars/");

				super.addResourceHandlers(registry);
			}
		};
	}


	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers(
						"/v2/api-docs",
						"/swagger-resources",
						"/swagger-resources/configuration/ui",
						"/swagger-resources/configuration/security")
				.permitAll();
		http.csrf().disable();
	}
}
