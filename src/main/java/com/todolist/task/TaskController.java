package com.todolist.task;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    private final String NOT_FOUND_BODY = "";
    private final String DELETED_BODY = "";

    @PostMapping
    public ResponseEntity<Object> saveTask(@RequestBody @Valid TaskDto taskDto){
        var taskModel = new TaskModel();
        BeanUtils.copyProperties(taskDto, taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.save(taskModel));
    }

    @GetMapping
    public ResponseEntity<List<TaskModel>> getAllTasks(){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneTask(@PathVariable(value = "id") UUID id){
        Optional<TaskModel> taskModelOptional = taskService.findById(id);
        if (taskModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND_BODY);
        }
        return ResponseEntity.status(HttpStatus.OK).body(taskModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable(value = "id") UUID id){
        Optional<TaskModel> parkingSpotModelOptional = taskService.findById(id);
        if (parkingSpotModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND_BODY);
        }
        taskService.delete(parkingSpotModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(DELETED_BODY);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable(value = "id") UUID id,
                                                           @RequestBody @Valid TaskDto taskDto){
        Optional<TaskModel> taskModelOptional = taskService.findById(id);
        if (taskModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND_BODY);
        }
        var taskModel = new TaskModel();
        BeanUtils.copyProperties(taskDto, taskModel);
        taskModel.setId(taskModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(taskService.save(taskModel));
    }
}