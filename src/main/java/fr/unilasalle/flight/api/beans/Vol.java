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
@Table(name="flights")

public class Vol extends PanacheEntity {
    @Id
    @SequenceGenerator(name = "planes_sequence", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planes_sequence_inJavaCode") //Création d'un id unique dans la bdd
    private long Id;


    @NotBlank(message = "number cannot be null")
    @Column(nullable = false)
    private String number;

    @NotBlank(message = "origin cannot be null")
    @Column(nullable = false)
    private String origin;

    @NotBlank(message = "destination cannot be null")
    @Column(nullable = false)
    private String destination;

    @NotNull(message = "information of departure cannot be null")
    @Column(nullable = false)
    private int departure_date;

    @NotNull(message = "information of departure cannot be null")
    @Column(nullable = false)
    private int departure_time;

    @NotNull(message = "information of arrival cannot be null")
    @Column(nullable = false)
    private int arrival_time;

    @NotNull(message = "information of arrival cannot be null")
    @Column(nullable = false)
    private int arrival_date;

    @NotNull(message = "plane id cannot be null")
    @Column(nullable = false)
    private int plane_id;
}
