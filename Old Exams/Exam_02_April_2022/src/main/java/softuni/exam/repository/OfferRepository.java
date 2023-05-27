package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Offer;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {


    @Query("SELECT o FROM Offer o " +
            "JOIN Apartment a ON a.id = o.apartment.id " +
            "WHERE a.apartmentType = :apType " +
            "ORDER BY a.area DESC, o.price ASC")
    List<Offer> findAllByApartment_ApartmentTypeOrderByApartment_AreaDescPriceAsc(ApartmentType apType);
}
