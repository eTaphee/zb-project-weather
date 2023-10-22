package kr.co.zerobase.weather.repository;

import kr.co.zerobase.weather.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMemoRepository extends JpaRepository<Memo, Integer> {

}
