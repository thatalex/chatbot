package by.bgtu.repository;

import by.bgtu.model.KeyWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface KeyWordRepository extends JpaRepository<KeyWord, Integer> {
    List<KeyWord> findAllByOrderByValueAsc();

    KeyWord findByValue(String value);
}
