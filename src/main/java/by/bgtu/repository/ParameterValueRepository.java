package by.bgtu.repository;

import by.bgtu.model.SubjectParameterValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ParameterValueRepository extends JpaRepository<SubjectParameterValue, Integer> {
    List<SubjectParameterValue> findAll();

    @Transactional
    void deleteById(Integer id);
}