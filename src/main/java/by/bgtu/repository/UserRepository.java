package by.bgtu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.bgtu.model.User;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	 User findByEmail(String email);

    User findById(Integer id);

    @Transactional
    void deleteById(Integer id);
}
