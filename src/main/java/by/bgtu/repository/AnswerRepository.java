package by.bgtu.repository;

import by.bgtu.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findAllByOrderByValueAsc();

    Answer findById(Integer id);

    @Modifying
    @Transactional
    void deleteById(Integer id);

    Answer findByValue(String value);
}
