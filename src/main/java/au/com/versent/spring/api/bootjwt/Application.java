package au.com.versent.spring.api.bootjwt;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.regex;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import au.com.versent.spring.api.security.OIDCDefaultAccessTokenConverter;
import au.com.versent.spring.api.security.OIDCUserAuthenticationConverter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ImplicitGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.LoginEndpoint;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
	}

	/**
	 * An opinionated WebApplicationInitializer to run a SpringApplication from
	 * a traditional WAR deployment. Binds Servlet, Filter and
	 * ServletContextInitializer beans from the application context to the
	 * servlet container.
	 *
	 * @link http://docs.spring.io/spring-boot/docs/current/api/index.html?org/
	 *       springframework/boot/context/web/SpringBootServletInitializer.html
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	@Configuration
	@EnableResourceServer
	@EnableWebSecurity
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) {
			// RSA Config
			resources.resourceId("SampleConfidentialApp");
			// HMAC Config
			// resources.resourceId("testResource");
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/me").authenticated().and().sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}

		@Bean
		public JwtTokenStore tokenStore() {
			JwtTokenStore store = new JwtTokenStore(tokenEnhancer());
			return store;
		}

		@Bean
		public JwtAccessTokenConverter tokenEnhancer() {
			final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
			jwtAccessTokenConverter.setAccessTokenConverter(tokenConverter());
			// RSA Config
			jwtAccessTokenConverter.setVerifierKey(
					"-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm2I2+GHcXXzwyjqMP6E4shjxfpAfgqbCY/nF5oTq0SkcRKvsdJzuLbmufkqx1rQqxwF/aZnbZppcVtR4TAhExmo2NnV7WjSwdd+EynQJrkWlsuK1UQ3JHMo5iAAEQ11xoMBIsUwfg5HYKCELmjnWetwhm5aUJ9Gq45v9kzeZki2oCoVe5LQfVVHEYssr+SfVrhi6+OffeefgCRse6vv5T4zlh4xXKDNUsBxYYB3Vg97tDcdgpfx8BudpBx+1ITk9Dazu8eegXN5KdRqJGgM5LSRIWjK+OumR1a2ReUcXlglWTVfsG43UUUby2bql3E3uc7XpxzQaPpt4aDqfOYMUxwIDAQAB-----END PUBLIC KEY-----");

			// HMAC Config
			//jwtAccessTokenConverter.setSigningKey("Passw0rd");
			return jwtAccessTokenConverter;
		}

		@Bean
		public AccessTokenConverter tokenConverter() {
			OIDCDefaultAccessTokenConverter oidcAccessTokenConverter = new OIDCDefaultAccessTokenConverter();
			oidcAccessTokenConverter.setUserTokenConverter(userTokenConverter());
			return oidcAccessTokenConverter;
		}

		@Bean
		public UserAuthenticationConverter userTokenConverter() {
			OIDCUserAuthenticationConverter oidcUserAuthenticationConverter = new OIDCUserAuthenticationConverter();
			return oidcUserAuthenticationConverter;
		}

	}

	@Configuration
	@EnableSwagger2
	protected static class SwaggerConfig {
		@Bean
		public Docket api() {
			return new Docket(DocumentationType.SWAGGER_2).securitySchemes(newArrayList(oauth()))
					.securityContexts(newArrayList(securityContext())).groupName("sampleapi").apiInfo(apiInfo())
					.select().paths(regex("/me")).build();
		}

		@Bean
		SecurityScheme oauth() {
			return new OAuthBuilder().name("petstore_auth").grantTypes(grantTypes()).scopes(scopes()).build();
		}

		List<AuthorizationScope> scopes() {
			return newArrayList(new AuthorizationScope("Customer.info id_token token", "Authorise"));
		}

		List<GrantType> grantTypes() {
			GrantType grantType = new ImplicitGrantBuilder()
					.loginEndpoint(new LoginEndpoint("http://petstore.swagger.io/api/oauth/dialog")).build();
			return newArrayList(grantType);
		}

		@Bean
		SecurityContext securityContext() {
			AuthorizationScope readScope = new AuthorizationScope("Customer.info id_token token", "read your details");
			AuthorizationScope[] scopes = new AuthorizationScope[1];
			scopes[0] = readScope;
			SecurityReference securityReference = SecurityReference.builder().reference("petstore_auth").scopes(scopes)
					.build();

			return SecurityContext.builder().securityReferences(newArrayList(securityReference)).forPaths(regex("/me"))
					.build();
		}

		private ApiInfo apiInfo() {
			Contact contact = new Contact("Nicholas Irving", "http://www.versent.com.au",
					"nicholas.irving@versent.com.au");
			return new ApiInfoBuilder().title("Spring REST Sample with Swagger")
					.description("Spring REST Sample with Swagger").termsOfServiceUrl("http://www.versent.com.au/")
					.contact(contact).license("Apache License Version 2.0").licenseUrl("http://www.versent.com.au/")
					.version("1.0").build();
		}

	}

}
