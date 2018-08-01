package by.bgtu.repository;


import by.bgtu.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer> {
    List<Word> findAll();

    Word findByValue(String value);
}
