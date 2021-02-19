package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.DTOs.EmployeeDTO;
import com.udacity.jdnd.course3.critter.DTOs.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.repositories.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;


    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Create new Employee
     * @param employeeDTO
     * @return EmployeeDTO
     */
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee newEmployee = new Employee();
        newEmployee.setSkills(employeeDTO.getSkills());
        newEmployee.setName(employeeDTO.getName());
        newEmployee.setDaysAvailable(employeeDTO.getDaysAvailable());
        this.employeeRepository.save(newEmployee);
        BeanUtils.copyProperties(newEmployee, employeeDTO);
        return employeeDTO;
    }

    /**
     * Set days available for specific Employee
     * @param daysAvailable
     * @param employeeId
     */
    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Optional<Employee> findEmployee = this.employeeRepository.findById(employeeId);
        if (findEmployee.isPresent()) {
            findEmployee.get().setDaysAvailable(daysAvailable);
            this.employeeRepository.save(findEmployee.get());
        }
    }

    /**
     * Find Employees by services
     * @param employeeDTO
     * @return list of EmployeeDTOs
     */
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        int numberofDay = getDayNumber(employeeDTO.getDate());
        List<Employee> employees = employeeRepository.findDistinctBySkillsInAndDaysAvailableContaining(employeeDTO.getSkills(), DayOfWeek.of(numberofDay));

        for (Employee employee : employees) {
            EmployeeDTO newEmployeeDTO = new EmployeeDTO();
            BeanUtils.copyProperties(employee, newEmployeeDTO);
            employeeDTOList.add(newEmployeeDTO);
        }
        return employeeDTOList;
    }

    public static int getDayNumber(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day.getValue();
    }

    /**
     * Get Employee by id
     * @param employeeId
     * @return EmployeeDTO
     */
    public EmployeeDTO getEmployee(long employeeId) {
        Optional<Employee> employee  = employeeRepository.findById(employeeId);
        if(employee.isPresent()) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            BeanUtils.copyProperties(employee.get(), employeeDTO);
            return employeeDTO;
        } else
            return null;
    }
}
