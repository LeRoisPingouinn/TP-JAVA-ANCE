package fr.unilasalle.flight.api.beans;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
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
@Table(name="reserve")

public class reservations extends PanacheEntityBase {
    @Id
    @SequenceGenerator(name = "planes_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planes_sequence_inJavaCode")
    private long Id;


    @NotNull(message = "flight id cannot be null")
    @ManyToOne // Jointure
    @JoinColumn(name = "flights_id", referencedColumnName = "id")
    private Vol flight_id;

    @NotNull(message = "passenger id cannot be null")
    @ManyToOne // Jointure
    @JoinColumn(name = "passager_id", referencedColumnName = "id")
    private passenger passenger_id;
}
