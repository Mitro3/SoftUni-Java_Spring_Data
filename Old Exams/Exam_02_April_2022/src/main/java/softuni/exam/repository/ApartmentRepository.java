package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Town;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    Optional<Apartment> findApartmentByAreaAndTown(Double area, Town town);


//    @Query("SELECT a FROM Apartment a " +
//            "JOIN Offer o ON a.id = o.id " +
//            "WHERE a.apartmentType = :apType " +
//            "ORDER BY a.area DESC, o.price ASC")
//    List<Apartment> findAllByApartmentType(ApartmentType apType);

//    @Query("SELECT a.apartmentType, a.area, a.town FROM Apartment AS a " +
//            "JOIN Town AS t ON a.town.id = t.id " +
//            "WHERE a.town.townName = :townName AND a.area = :area")
//    Optional<Apartment> findApartmentByTownName(String townName);

}
