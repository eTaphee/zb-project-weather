package kr.co.zerobase.weather.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import java.util.List;
import java.util.Optional;
import kr.co.zerobase.weather.domain.Memo;
import kr.co.zerobase.weather.repository.JdbcMemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class JdbcMemoRepositoryTest {

    @Autowired
    private JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest() {
        // given
        Memo newMemo = new Memo(1, "this is new memo~");

        // when
        jdbcMemoRepository.save(newMemo);

        // then
        Optional<Memo> result = jdbcMemoRepository.findById(1);
        assertEquals("this is new memo~", result.get().getText());
    }

    @Test
    void findAllMemoTest() {
        // given

        // when
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println(memoList);

        // then
        assertNotNull(memoList);
        assertEquals(0, memoList.size());
    }
}
