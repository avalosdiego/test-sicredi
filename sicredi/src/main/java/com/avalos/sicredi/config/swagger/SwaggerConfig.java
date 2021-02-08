package com.avalos.sicredi.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket apiVersion1() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Versão API 1")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.avalos.sicredi"))
				.paths(PathSelectors.ant("/**/v1/**"))
				.build()
				.apiInfo(new ApiInfoBuilder().version("1").title("API V1").description("Documentação API V1").build());
	}
	
	@Bean
	public Docket apiVersion2() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Versão API 2")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.avalos.sicredi"))
				.paths(PathSelectors.ant("/**/v2/**"))
				.build()
				.apiInfo(new ApiInfoBuilder().version("2").title("API V2").description("Documentação API V2").build());
	}

}
