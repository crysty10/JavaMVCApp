package ro.z2h.service;

import ro.z2h.domain.Department;

import java.util.List;

/**
 * Created by Dumitru on 12.11.2014.
 */
public interface DepartmentService {

    List<Department> findAllDepartments();
}
