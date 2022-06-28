package hr.petkovic.iehr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.petkovic.iehr.entity.Task;

public interface TaskRepo extends JpaRepository<Task, Long> {

	List<Task> findAllByUser_Username(String username);

	List<Task> findAllByDoneAndUser_Username(Boolean done, String username);
	
	List<Task> findAllByDone(Boolean done);
}
