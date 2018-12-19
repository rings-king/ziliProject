/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jux.mtqiushui.dispatching.services;

import com.jux.mtqiushui.dispatching.model.Resource;
import com.jux.mtqiushui.dispatching.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jux
 */
@Service
public class ResourceService {
    @Autowired
    ResourceRepository resourceRepository;
    public Iterable<Resource> getAllResource() throws Exception{
        return resourceRepository.findAll();
    }
}
