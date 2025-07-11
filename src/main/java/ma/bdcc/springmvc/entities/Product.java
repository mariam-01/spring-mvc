package ma.bdcc.springmvc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 200)
    private String name;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull
    @Min(value = 0, message = "Le prix doit être positif")
    private double price;

    @NotNull
    @Min(value = 1, message = "La quantité minimale autorisée est 1")
    private double quantity;

}
