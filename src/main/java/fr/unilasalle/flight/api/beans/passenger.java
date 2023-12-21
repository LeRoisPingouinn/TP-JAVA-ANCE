package fr.unilasalle.flight.api.beans;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="passager")

public class passenger extends PanacheEntityBase {
    @Id
    @SequenceGenerator(name = "planes_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planes_sequence_inJavaCode")
    private long Id;


    @NotBlank(message = "surname id cannot be null")
    @Column(nullable = false)
    private String surname;

    @NotBlank(message = "firstname id cannot be null")
    @Column(nullable = false)
    private String firstname;

    @NotBlank(message = "email address cannot be null")
    @Column(nullable = false)
    private String email_address;
}
