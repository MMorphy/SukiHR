package hr.petkovic.iehr.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.petkovic.iehr.entity.Task;
import hr.petkovic.iehr.service.TaskService;
import hr.petkovic.iehr.service.UserService;

@Controller
@RequestMapping("/task")
public class TaskController {
	Logger logger = LoggerFactory.getLogger(TaskController.class);

	private TaskService taskSer;
	private UserService userSer;

	public TaskController(TaskService taskService, UserService userService) {
		taskSer = taskService;
		userSer = userService;
	}

	@GetMapping(value = { "/", "/{username}" })
	public String getAllTasks(@PathVariable(required = false) String username, Model model) {
		if (username == null || username.isEmpty()) {
			model.addAttribute("activeTasks", taskSer.findAllActiveTasks());
			model.addAttribute("finishedTasks", taskSer.findAllFinishedTasks());
		} else {
			model.addAttribute("activeTasks", taskSer.findAllActiveTasksForUsername(username));
			model.addAttribute("finishedTasks", taskSer.findAllFinishedTasksForUsername(username));
		}
		return "task/list";
	}

	@GetMapping("/add")
	public String getTaskAdding(Model model) {
		model.addAttribute("users", userSer.findAllEnabledUsers());
		model.addAttribute("addTask", taskSer.getNewTask());
		return "task/add";
	}

	@PostMapping("/add")
	public String addTask(Task addTask) {
		taskSer.saveTask(addTask);
		return "redirect:/";
	}

	@GetMapping("/comment/{id}")
	public String getTaskCommenting(@PathVariable Long id, Model model) {
		model.addAttribute("editTask", taskSer.findTaskById(id));
		return "task/comment";
	}

	@PostMapping("/comment/{id}")
	public String commentTask(@PathVariable Long id, Task editTask) {
		taskSer.commentTask(id, editTask);
		return "redirect:/";
	}

	@GetMapping("/edit/{id}")
	public String getTaskEditing(@PathVariable Long id, Model model) {
		model.addAttribute("editTask", taskSer.findTaskById(id));
		model.addAttribute("users", userSer.findAllEnabledUsers());
		return "task/edit";
	}

	@PostMapping("/edit/{id}")
	public String editTask(@PathVariable Long id, Task editTask) {
		taskSer.editTask(id, editTask);
		return "redirect:/";
	}

	@PostMapping("/finish/{id}")
	public String finishTask(@PathVariable Long id) {
		taskSer.finishTask(id);
		return "redirect:/";
	}

	@PostMapping("/unfinish/{id}")
	public String unfinishTask(@PathVariable Long id) {
		taskSer.unfinishTask(id);
		return "redirect:/";
	}

	@PostMapping("/delete/{id}")
	public String deleteTask(@PathVariable Long id) {
		taskSer.deleteTask(id);
		return "redirect:/";
	}
}
