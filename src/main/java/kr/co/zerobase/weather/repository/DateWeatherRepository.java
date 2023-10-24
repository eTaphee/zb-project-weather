package kr.co.zerobase.weather.repository;

import java.time.LocalDate;
import kr.co.zerobase.weather.domain.DateWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateWeatherRepository extends JpaRepository<DateWeather, LocalDate> {

}
