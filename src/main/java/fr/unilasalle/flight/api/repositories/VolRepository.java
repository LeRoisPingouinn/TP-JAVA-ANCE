package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Vol;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class VolRepository implements
        PanacheRepositoryBase<Vol, Long> {

    public List<Vol> findbyDest(String DestParameter) {

        return find("destination", DestParameter).list();
    }

}
