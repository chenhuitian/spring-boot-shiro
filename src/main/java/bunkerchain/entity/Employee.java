package bunkerchain.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;

public class Employee implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static java.util.List<Employee> employees = new ArrayList<>();
    private int no;
    private String name;
    private String designation;
    private String gender;
    private Instant instant = Instant.now();
    public Employee(int no, String name, String designation, String gender ) {
        this.no = no;
        this.name = name;
        this.designation = designation;
        this.gender = gender;	
    }
    public int getNo() {
        return no;
    }
    public void setNo(int no) {
        this.no = no;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public Instant getInstant() {
        return instant;
    }
    public void setInstant(Instant instant) {
        this.instant = instant;
    }
    public static java.util.List<Employee> getEmployee() {
//        java.util.List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "Bob", "Developer", "Male"));
        employees.add(new Employee(2, "Joy", "Sr. Developer", "Male"));
        employees.add(new Employee(3, "John", "CEO", "Male"));
        employees.add(new Employee(4, "Bat", "Developer", "Male"));
        employees.add(new Employee(5, "Jolly", "Developer", "Female"));
        employees.add(new Employee(6, "Bobby", "Developer", "Female"));
        return employees;
    }
    public static java.util.List<Employee> addEmployee(Employee e) {
    	employees.add(e);
    	return employees;
    }
}
