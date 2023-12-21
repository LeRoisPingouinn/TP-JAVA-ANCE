package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.passenger;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class passengerRepository implements
        PanacheRepositoryBase<passenger, Long> {
    public List<passenger> findByReservationParameter(long reservation_idParameter) {

        return find("reservation_id.id", reservation_idParameter).list();
    }



        public passenger findByEmail(String emailAddress) {
            return find("email_address", emailAddress).firstResult();
        }

}

