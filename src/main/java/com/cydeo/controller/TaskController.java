package com.cydeo.controller;

import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/task")
public class TaskController {

    private final UserService userService;
    private final ProjectService projectService;
    private final TaskService taskService;

    public TaskController(UserService userService, ProjectService projectService, TaskService taskService) {
        this.userService = userService;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @GetMapping("/create")
    public String createTask(Model model) {

        model.addAttribute("task", new TaskDTO());

        model.addAttribute("projects", projectService.getAllProjects());

        model.addAttribute("employees", userService.getAllByRole("employee"));

        model.addAttribute("tasks", taskService.getAllTasks());

        return "/task/create";

    }

    @PostMapping("/create")
    public String insertTask(@Valid @ModelAttribute("task") TaskDTO task, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("projects", projectService.getAllProjects());

            model.addAttribute("employees", userService.getAllByRole("employee"));

            model.addAttribute("tasks", taskService.getAllTasks());

            return "/task/create";

        }

        taskService.save(task);

        return "redirect:/task/create";

    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {

        taskService.delete(id);

        return "redirect:/task/create";

    }

    @GetMapping("/update/{taskId}")
    public String editTask(@PathVariable("taskId") Long taskId, Model model) {

        model.addAttribute("task", taskService.getById(taskId));

        model.addAttribute("projects", projectService.getAllProjects());

        model.addAttribute("employees", userService.getAllByRole("employee"));

        model.addAttribute("tasks", taskService.getAllTasks());

        return "/task/update";

    }

    @PostMapping("/update/{id}")
    public String updateTask(@Valid @ModelAttribute("task") TaskDTO task, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("projects", projectService.getAllProjects());

            model.addAttribute("employees", userService.getAllByRole("employees"));

            model.addAttribute("tasks", taskService.getAllTasks());

            return "/task/update";

        }

        taskService.update(task);

        return "redirect:/task/create";

    }

    @GetMapping("/employee/pending-tasks")
    public String employeePendingTasks(Model model) {

        model.addAttribute("tasks", taskService.getAllTasksByStatusIsNot(Status.COMPLETE));

        return "/task/pending-tasks";

    }

    @GetMapping("/employee/archive")
    public String employeeArchivedTasks(Model model) {

        model.addAttribute("tasks", taskService.getAllTasksByStatus(Status.COMPLETE));

        return "/task/archive";

    }

    @GetMapping("/employee/edit/{id}")
    public String employeeEditTask(@PathVariable Long id, Model model) {

        model.addAttribute("task", taskService.getById(id));

        model.addAttribute("statuses", Status.values());

        model.addAttribute("tasks", taskService.getAllTasksByStatusIsNot(Status.COMPLETE));

        return "/task/status-update";

    }

    @PostMapping("/employee/update/{id}")
    public String employeeUpdateTask(@Valid @ModelAttribute("task") TaskDTO task, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("statuses", Status.values());

            model.addAttribute("tasks", taskService.getAllTasksByStatusIsNot(Status.COMPLETE));

            return "/task/status-update";

        }

        taskService.update(task);

        return "redirect:/task/employee/pending-tasks";

    }

}
