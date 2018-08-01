package by.bgtu.repository;

import by.bgtu.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    List<Subject> findAllByOrderByNameAsc();

    Subject findByName(String name);

    Subject findById(Integer id);
}
