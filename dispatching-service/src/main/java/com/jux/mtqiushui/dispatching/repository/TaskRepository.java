package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

    public Task findById(Long id);
    
    public List<Task> findByParentId(Long parentId);
    //public License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
