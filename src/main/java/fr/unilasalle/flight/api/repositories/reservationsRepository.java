package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.reservations;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class reservationsRepositories implements
        PanacheRepositoryBase<reservations, Long> {

    public List<reservations> findByOperator(String operatorParameter) {

        return find("operator", operatorParameter).list();
    }
}
