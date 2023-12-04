package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.passager;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class passagerRepository implements
        PanacheRepositoryBase<passager, Long> {

    public List<passager> findByOperator(String operatorParameter) {

        return find("operator", operatorParameter).list();
    }
}
