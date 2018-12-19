/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jux.mtqiushui.dispatching.services;

import com.jux.mtqiushui.dispatching.model.Assignment;
import com.jux.mtqiushui.dispatching.repository.AssignmentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jux
 */
@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public Iterable<Assignment> getAllAssignment() throws Exception {
        return assignmentRepository.findAll();
    }
    
    public Assignment getAssignmentById(Long id) throws Exception {
        return assignmentRepository.findOne(id);
    }
    
    public void addList(List<Assignment> assignmentsList) throws Exception {
        assignmentRepository.save(assignmentsList);
    }
    
    public void updateList(List<Assignment> assignmentsList)  throws Exception {
        assignmentRepository.save(assignmentsList);
    }
    
    public void deleteList(List<Assignment> assignmentsList)  throws Exception {
        assignmentRepository.delete(assignmentsList);
    }
}
