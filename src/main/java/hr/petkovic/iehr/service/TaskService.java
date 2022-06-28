package hr.petkovic.iehr.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.Task;
import hr.petkovic.iehr.repo.TaskRepo;

@Service
public class TaskService {

	Logger logger = LoggerFactory.getLogger(TaskService.class);

	private TaskRepo taskRepo;

	public TaskService(TaskRepo taskRepository) {
		taskRepo = taskRepository;
	}

	public List<Task> findAllTasks() {
		return taskRepo.findAll();
	}

	public List<Task> findAllActiveTasksForUsername(String username) {
		return taskRepo.findAllByDoneAndUser_Username(false, username);
	}

	public Task getNewTask() {
		return new Task();
	}

	public Task saveTask(Task addTask) {
		return taskRepo.save(addTask);
	}

	public List<Task> findAllActiveTasks() {
		return taskRepo.findAllByDone(false);
	}

	public Task findTaskById(Long id) {
		try {
			return taskRepo.findById(id).get();
		} catch (Exception ex) {
			logger.error("Exception occured in findTaskById", ex);
		}
		return null;
	}

	public Task addComment(Long id, Task editTask) {
		Task old = findTaskById(id);
		old.setComment(editTask.getComment());
		return saveTask(old);
	}

	public Task finishTask(Long id) {
		Task old = findTaskById(id);
		if (old.getDone() == false) {
			old.setDone(true);
			return saveTask(old);
		}
		return old;
	}

	public Task commentTask(Long id, Task editTask) {
		Task old = findTaskById(id);
		old.setComment(editTask.getComment());
		return saveTask(old);
	}

	public Task editTask(Long id, Task editTask) {
		Task old = findTaskById(id);
		old.setComment(editTask.getComment());
		old.setDescription(editTask.getDescription());
		old.setUser(editTask.getUser());
		old.setDueDate(editTask.getDueDate());
		old.setDone(editTask.getDone());
		return saveTask(old);
	}

	public void deleteTask(Long id) {
		taskRepo.deleteById(id);
	}
}
