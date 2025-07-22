package vanh.notify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanh.notify.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
}