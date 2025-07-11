package ma.bdcc.springmvc;

import ma.bdcc.springmvc.entities.Product;
import ma.bdcc.springmvc.repository.ProductRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication//(exclude = SecurityAutoConfiguration.class)
public class SpringMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMvcApplication.class, args);

    }

    /*@Bean
    CommandLineRunner start (ProductRepo productRepo) {
        return args -> {
            productRepo.save(Product.builder()
                    .name("first product")
                    .description("first product description")
                    .quantity(100)
                    .price(20)
                    .build());
            var products = productRepo.findAll();
            products.forEach(System.out::println);
        };
    }*/

}
