package fr.unilasalle.flight.api.beans;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="planes")

public class Avion extends PanacheEntity {
    @Id
    @SequenceGenerator(name = "planes_sequence", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planes_sequence_inJavaCode")//Création de l'id dans la bdd
    private long Id;


    @NotBlank(message = "Operator cannot be null")
    @Column(nullable = false)
    private String operator;

    @NotBlank(message = "Model cannot be null")
    @Column(nullable = false)
    private String model;

    @NotBlank(message = "registration cannot be null")
    @Size(max = 6)
    @Column(nullable = false)
    private String registration;

    @NotNull(message = "capacity cannot be null")
    @Column(nullable = false)
    private int capacity;
}
