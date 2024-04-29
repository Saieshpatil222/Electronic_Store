package com.electronic.store.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Electronic Store Api !!"
                , description = "This Project is Developed By TextCode Technologies !!"
                , version = "1.0V"
                , contact = @Contact(name = "Saiesh Patil", email = "patilsaiesh180@gmail.com", url = "patilsaiesh180@gmail.com")
                , license = @License(name = "Open License", url = "bhushan@gmail.com")
        )
        , externalDocs = @ExternalDocumentation(description = "This is External Docs  ", url = "https:/www.instagram.com/saiesh/")
)
@SecurityScheme(
        name = "scheme1",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {


    //  :- we can do following thing with the help of Annotations Also
//    @Bean
//    public OpenAPI openAPI()
//    {
//        String schemeName="bearerScheme";
//        return new OpenAPI()
//                 .addSecurityItem(new SecurityRequirement()
//                      .addList(schemename)
    //                       )
//                .components(new Components()
//                        .addSecuritySchemes(schemeName,new SecurityScheme()
//                                                    .name(schemeName)
//                                .type(SecurityScheme.Type.HTTP)
//                                .bearerFormat("JWT")
//                                .scheme("bearer")
//
//                        ))
//                 .info(new io.swagger.v3.oas.models.info.Info()
//                            .title("Electronic Store Api !!")
//                .description("This is Project Developed By TextCode Technologies !!")
//                .version("1.0V")
//                .contact(new io.swagger.v3.oas.models.info.Contact().name("bhushan").email("bhushan@gmail.com").url("bhushan.com"))
//                .license(new io.swagger.v3.oas.models.info.License().name("Apache"))
//        )   .externalDocs(new io.swagger.v3.oas.models.ExternalDocumentation().description("Secure"));
//
//    }


//	@Bean
//	public Docket docket() {
//
//		Docket docket = new Docket(DocumentationType.SWAGGER_2);
//		docket.apiInfo(getApiInfo());
//		//Whole swagger configuration will handled by docket
//		// So we have to set in docket 1>SecurityContexts()
//									// 2>Security schemes
//		docket.securityContexts(Arrays.asList(getSecurityContext()));
//		docket.securitySchemes(Arrays.asList(getSchemes()));
//
//		ApiSelectorBuilder apiSelector = docket.select();
//		apiSelector.apis(RequestHandlerSelectors.any());
//		apiSelector.paths(PathSelectors.any());
//
//		Docket build = apiSelector.build();
//
//		return build;
//	}

//	private SecurityContext getSecurityContext() {
//
//		SecurityContext context = SecurityContext.builder().securityReferences(getSecurityRederences()).build();
//
//		return context;
//	}

//	private List<SecurityReference> getSecurityRederences() {
//
//		AuthorizationScope[] scope = { new AuthorizationScope("Global", "Access Every Thing") };
//
//		return Arrays.asList(new SecurityReference("JWT", scope));
//	}

//	private ApiKey getSchemes() {
//
//		//authorization key name ahe --> ani te aplyala header mdhe pass karayche
//		return new ApiKey("JWT", "Authorization", "header");
//	}

//	private ApiInfo getApiInfo() {
//
//		ApiInfo apiInfo = new ApiInfo("Electronic Store Backend :Apis",
//				"This is backend project created by TextCode Technologies", "1.0.0V",
//				"https://www.shubhamGajareJavaDevloper.com",
//				new Contact("bhushan", "https://www.instagram.com/shubssss_", "shubhamgajare777@gmail.com"),
//				"License of APIS", "https://www.shubhamGajareJavaDevloper.com/about", new ArrayDeque<>());
//
//		return apiInfo;
//	}

}
