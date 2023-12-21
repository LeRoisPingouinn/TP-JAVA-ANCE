package fr.unilasalle.flight.api.beans;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="flights")

public class Vol extends PanacheEntityBase {
    @Id
    @SequenceGenerator(name = "flights_sequence", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flights_sequence") //Création d'un id unique dans la bdd
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
    private LocalDate departure_date;

    @NotNull(message = "information of departure cannot be null")
    @Column(nullable = false)
    private LocalTime departure_time;

    @NotNull(message = "information of arrival cannot be null")
    @Column(nullable = false)
    private LocalTime arrival_time;

    @NotNull(message = "information of arrival cannot be null")
    @Column(nullable = false)
    private LocalDate arrival_date;

    @NotNull(message = "plane id cannot be null")
    @ManyToOne // Jointure
    @JoinColumn(name = "planes_id", referencedColumnName = "id")
    private Avion plane_id;
}
