/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jux.mtqiushui.dispatching.services;

import com.jux.mtqiushui.dispatching.model.Dependency;
import com.jux.mtqiushui.dispatching.repository.DependencyRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jux
 */
@Service
public class DependencyService {

    @Autowired
    DependencyRepository dependencyRepository;

    public Iterable<Dependency> getAll() throws Exception {
        return dependencyRepository.findAll();
    }

    public Dependency getOne(Long id) throws Exception {
        return dependencyRepository.findOne(id);
    }

    public void addList(List<Dependency> dependenciesList) throws Exception {
        dependencyRepository.save(dependenciesList);
    }

    public void deleteList(List<Dependency> dependenciesList) throws Exception {
        dependencyRepository.delete(dependenciesList);
    }
}
