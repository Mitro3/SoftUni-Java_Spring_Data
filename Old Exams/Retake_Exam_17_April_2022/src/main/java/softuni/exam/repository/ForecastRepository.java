package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.DayOfWeek;
import softuni.exam.models.entity.Forecast;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    Optional<Forecast> findByCityIdAndDayOfWeek(Long city, DayOfWeek dayOfWeek);
//    @Query("SELECT c.cityName, f.maxTemperature, f.minTemperature, f.sunrise, f.sunset FROM Forecast AS f " +
//            "JOIN City AS c " +
//            "WHERE f.dayOfWeek = :day AND c.population < :minPopulation " +
//            "ORDER BY f.maxTemperature DESC, f.id ASC")
//    List<Forecast> findAllByDayOfWeeKAndCityMinPopulation(DayOfWeek day, int minPopulation);

    List<Forecast> findAllByDayOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc(DayOfWeek dayOfWeek, int population);
}
