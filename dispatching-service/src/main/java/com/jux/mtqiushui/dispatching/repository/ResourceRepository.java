/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jux
 */
@Repository
public interface ResourceRepository extends CrudRepository<Resource, Long>{
}
