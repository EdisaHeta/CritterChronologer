package com.udacity.jdnd.course3.critter.controllers;

import com.udacity.jdnd.course3.critter.DTOs.CustomerDTO;
import com.udacity.jdnd.course3.critter.DTOs.EmployeeDTO;
import com.udacity.jdnd.course3.critter.DTOs.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.services.CustomerService;
import com.udacity.jdnd.course3.critter.services.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final EmployeeService employeeService;
    private final CustomerService customerService;

    public UserController(EmployeeService employeeService, CustomerService customerService) {
        this.employeeService = employeeService;
        this.customerService = customerService;
    }

    /**
     * Save customer
     * @param customerDTO
     * @return CustomerDTO
     */
    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        try {
            return this.customerService.saveCustomer(customerDTO);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not save Customer!");
        }
    }

    /**
     * Get list of all customers
     * @return list of CustomerDTOs
     */
    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        try{
            return this.customerService.getList();
        } catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not get Customers!");
        }
    }

    /**
     * Get owner by petId
     * @param petId
     * @return CustomerDTO
     */
    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        try{
            return this.customerService.getOwnerByPet(petId);
        } catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not get Pet's Owner!");
        }
    }

    /**
     * Save employee
     * @param employeeDTO
     * @return EmployeeDTO
     */
    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            return this.employeeService.saveEmployee(employeeDTO);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not create Employee!");
        }
    }

    /**
     * Get employee by id
     * @param employeeId
     * @return EmployeeDTO
     */
    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        try {
            return this.employeeService.getEmployee(employeeId);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not get Employee!");
        }
    }

    /**
     * Set list of days availability
     * @param daysAvailable
     * @param employeeId
     */
    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try {
            this.employeeService.setAvailability(daysAvailable, employeeId);
        } catch(Exception e) {
            throw new UnsupportedOperationException("Could not set Availability!");
        }
    }

    /**
     * find Employees by Services
     * @param employeeDTO
     * @return list of EmployeeDto
     */
    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        try {
            return this.employeeService.findEmployeesForService(employeeDTO);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not find Employees!");
        }
    }
}
