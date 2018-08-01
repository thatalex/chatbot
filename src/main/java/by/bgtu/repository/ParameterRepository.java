package by.bgtu.repository;

import by.bgtu.model.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Integer> {
    List<Parameter> findAllByOrderByNameAsc();

    @Transactional
    void deleteById(Integer id);

    Parameter findByName(String name);
}
