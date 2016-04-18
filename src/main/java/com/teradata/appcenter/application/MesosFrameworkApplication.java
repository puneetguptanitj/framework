package com.teradata.appcenter.application;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate4.HibernateTransactionManager;

import com.google.common.base.Predicate;
import com.teradata.appcenter.entity.MyFramework;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableSwagger2
public class MesosFrameworkApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context =  SpringApplication.run(MesosFrameworkApplication.class, args);
		 context.getBean(MyFramework.class).init(); 
	}
	@Bean
	public Docket appApi() {
		return new Docket(DocumentationType.SWAGGER_2)
		.groupName("Mesos fwk API Latest")
		.useDefaultResponseMessages(false)
		.apiInfo(apiInfo())
		.select()
		.paths(new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return input.startsWith("/tasks");
			}
		})
		.build();
	}
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
		.title("Mesos fwk API")
		.description("API for mesos fwk")
		.termsOfServiceUrl("http://uda.io")
		.contact("appcenter")
		.license("Git Hub")
		.licenseUrl("https://github.td.teradata.com/appcenter/app-service")
		.version("1.0")
		.build();
	}
	@Bean
	public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf) {
		return hemf.getSessionFactory();
	}

	@Bean
	public HibernateTransactionManager getTransactionManager(
			SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(
				sessionFactory);

		return transactionManager;
	}

}
