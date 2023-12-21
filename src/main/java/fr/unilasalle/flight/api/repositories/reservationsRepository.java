package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.reservations;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class reservationsRepository implements
        PanacheRepositoryBase<reservations, Long> {

    public List<reservations> findByflight_idParameter(long flight_idParameter) {

        return find("flight_id.id", flight_idParameter).list();
    }

}
