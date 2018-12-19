package com.jux.mtqiushui.dispatching.services;

import com.jux.mtqiushui.dispatching.model.Task;
import com.jux.mtqiushui.dispatching.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task getTask(Long id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID号是0或者负数，不符合要求。");
        }
        return taskRepository.findById(id);
    }

    public List<Task> getTasksByParentId(Long parentId) throws Exception {
        if (parentId <= 0) {
            throw new Exception("ID号是0或者负数，不符合要求。");
        }
        return taskRepository.findByParentId(parentId);
    }
}
