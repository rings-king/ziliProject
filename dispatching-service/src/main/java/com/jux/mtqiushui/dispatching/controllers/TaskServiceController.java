package com.jux.mtqiushui.dispatching.controllers;

import com.jux.mtqiushui.dispatching.model.Task;
import com.jux.mtqiushui.dispatching.services.TaskService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value="v1/tasks")
public class TaskServiceController {
    @Autowired
    private TaskService taskService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceController.class);


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Task> getTask(@PathVariable("id") Long id) {
        LOGGER.info(String.format("task-service getTask() 被调用: {%s} 参数 {%s}", taskService.getClass().getName(), id));

        Task task;
        try {
            task = taskService.getTask(id);
        } catch (Exception ex) {
            LOGGER.error(String.format("调用异常：getTask(),错误信息：{%s}", ex.toString()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //material.setMaterialComment("OLD::" + material.getMaterialComment());
        return task != null ? new ResponseEntity<>(task, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @RequestMapping(value = "/getbyparentid/{parentId}", method = RequestMethod.GET)
    public ResponseEntity<List<Task>> getTasksByParentId(@PathVariable("parentId") Long parentId) {
        LOGGER.info(String.format("task-service getTaskByParentId() 被调用: {%s} 参数 {%s}", taskService.getClass().getName(), parentId));
        
        List<Task> tasks;
        try {
            tasks = taskService.getTasksByParentId(parentId);
        } catch (Exception ex) {
            LOGGER.error(String.format("调用异常：getTasksByParentId(),错误信息：{%s}", ex.toString()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //material.setMaterialComment("OLD::" + material.getMaterialComment());
        return tasks != null ? new ResponseEntity<>(tasks, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
