package kr.co.zerobase.weather.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import kr.co.zerobase.weather.domain.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class JpaMemoRepositoryTest {

    @Autowired
    private JpaMemoRepository jpaMemoRepository;

    @Test
    void insertMemoTest() {
        // given
        Memo newMemo = new Memo(10, "this is jpa new memo~");

        // when
        jpaMemoRepository.save(newMemo);

        // then
        List<Memo> memoList = jpaMemoRepository.findAll();
        assertTrue(memoList.size() > 0);
    }

    @Test
    void findByIdTest() {
        // given
        Memo newMemo = new Memo(11, "jpa");

        // when
        Memo saved = jpaMemoRepository.save(newMemo);

        // then
        Optional<Memo> memo = jpaMemoRepository.findById(saved.getId());
        assertEquals("jpa", newMemo.getText());
    }
}