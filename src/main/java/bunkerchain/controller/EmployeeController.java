package bunkerchain.controller;

import java.util.List;


import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bunkerchain.entity.Employee;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	
    @RequestMapping("/list")
    @RequiresPermissions("employee:list")
    public List<Employee> getEmployees() {
        return Employee.getEmployee();
    }
    
    @PostMapping("/add")
    @RequiresPermissions("employee:add")
    public List<Employee> addEmployees(@RequestBody Employee employee) {
        return Employee.addEmployee(employee);
    }
    
}