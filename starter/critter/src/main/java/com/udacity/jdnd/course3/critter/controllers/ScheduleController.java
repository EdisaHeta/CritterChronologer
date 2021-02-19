package com.udacity.jdnd.course3.critter.controllers;

import com.udacity.jdnd.course3.critter.DTOs.ScheduleDTO;
import com.udacity.jdnd.course3.critter.services.CustomerService;
import com.udacity.jdnd.course3.critter.services.EmployeeService;
import com.udacity.jdnd.course3.critter.services.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * Create schedule
     * @param scheduleDTO
     * @return ScheduleDTO
     */
    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        try {
            return scheduleService.createSchedule(scheduleDTO);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not create schedule!");
        }
    }

    /**
     * Gets list of all schedules
     * @return list of ScheduleDTOs
     */
    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        try {
            return scheduleService.getAllSchedules();
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not get Schedules!");
        }
    }

    /**
     * Gets schedule by petId
     * @param petId
     * @return list of ScheduleDTOs
     */
    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        try {
            return scheduleService.getScheduleForPet(petId);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not get Pet's Schedules!");
        }
    }

    /**
     * Get schedule by employee
     * @param employeeId
     * @return list of ScheduleDTOs
     */
    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        try {
            return scheduleService.getScheduleForEmployee(employeeId);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not get Employee's Schedules!");
        }
    }

    /**
     * Get schedule by customerId
     * @param customerId
     * @return list of ScheduleDTOs
     */
    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        try {
            return scheduleService.getScheduleForCustomer(customerId);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not get Customer's Schedules!");
        }
    }
}
