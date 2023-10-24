package kr.co.zerobase.weather.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import kr.co.zerobase.weather.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findAllByDate(LocalDate date);

    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<Diary> getFirstByDate(LocalDate date);

    void deleteAllByDate(LocalDate date);
}
