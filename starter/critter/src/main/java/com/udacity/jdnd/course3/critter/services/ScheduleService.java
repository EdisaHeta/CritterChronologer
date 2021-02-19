package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.DTOs.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.repositories.ScheduleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ScheduleService {

    private final PetRepository petRepository;
    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    private static final String GET_SCHEDULE_BY_PET = "select sch.*\n" +
            "from schedule sch\n" +
            "left join pet_schedule psch on psch.schedule_id = sch.id\n" +
            "where psch.pet_id = :petId";

    private static final String GET_SCHEDULE_BY_EMPLOYEE = "select sch.*\n" +
            "from schedule sch\n" +
            "left join employee_schedule esch on esch.schedule_id = sch.id\n" +
            "where esch.employee_id = :employeeId";

    private static final String GET_SCHEDULE_BY_CUSTOMER = "select sch.*\n" +
            "from schedule sch\n" +
            "left join pet_schedule psch on psch.schedule_id = sch.id\n" +
            "left join pet p on p.id = psch.pet_id\n" +
            "left join customer cus on cus.id = p.owner_id\n" +
            "where cus.id = :customerId";

    public ScheduleService(PetRepository petRepository, ScheduleRepository scheduleRepository, EmployeeRepository employeeRepository) {
        this.petRepository = petRepository;
        this.scheduleRepository = scheduleRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Create new Schedule
     * @param scheduleDTO
     * @return ScheduleDTO
     */
    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        Schedule newSchedule = new Schedule();
        newSchedule.setActivities(scheduleDTO.getActivities());
        newSchedule.setDate(scheduleDTO.getDate());

        Set<Pet> findPets = new HashSet<>();
        scheduleDTO.getPetIds().forEach(petId -> {
            Optional<Pet> findPet = petRepository.findById(petId);
            if(findPet.isPresent()) {
                findPets.add(findPet.get());
            }
        });
        newSchedule.setPets(findPets);
        Set<Employee> findEmployees = new HashSet<>();
        scheduleDTO.getEmployeeIds().forEach(employeeId -> {
            Optional<Employee> findEmployee = employeeRepository.findById(employeeId);
            if(findEmployee.isPresent()) {
                findEmployees.add(findEmployee.get());
            }
        });
        newSchedule.setEmployees(findEmployees);
        Schedule createdSchedule = this.scheduleRepository.save(newSchedule);
        BeanUtils.copyProperties(createdSchedule, scheduleDTO);
        ArrayList scheduledPetIds = new ArrayList();
        createdSchedule.getPets().forEach(pet -> {
            scheduledPetIds.add(pet.getId());
        });
        ArrayList scheduledEmployeeIds = new ArrayList();
        createdSchedule.getEmployees().forEach(employee -> {
            scheduledEmployeeIds.add(employee.getId());
        });
        scheduleDTO.setPetIds(scheduledPetIds);
        scheduleDTO.setEmployeeIds(scheduledEmployeeIds);
        return scheduleDTO;
    }

    /**
     * Get list of Schedules
     * @return list of ScheduleDTOs
     */
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> scheduleList = this.scheduleRepository.findAll();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(Schedule schedule: scheduleList) {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            BeanUtils.copyProperties(schedule, scheduleDTO);
            ArrayList employeeIds = new ArrayList<>();
            schedule.getEmployees().forEach(employee -> {
                employeeIds.add(employee.getId());
            });
            ArrayList petIds = new ArrayList<>();
            schedule.getPets().forEach(pet -> {
                petIds.add(pet.getId());
            });
            scheduleDTO.setEmployeeIds(employeeIds);
            scheduleDTO.setPetIds(petIds);
            scheduleDTOS.add(scheduleDTO);
        }
        return scheduleDTOS;
    }

    /**
     * Get Schedule by petId
     * @param petId
     * @return list of ScheduleDTOs
     */
    public List<ScheduleDTO> getScheduleForPet(long petId) {
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        List<Schedule> scheduleList = jdbcTemplate.query(
                GET_SCHEDULE_BY_PET,
                new MapSqlParameterSource().addValue("petId", petId),
                new BeanPropertyRowMapper<>(Schedule.class));
        for(Schedule schedule: scheduleList) {
            Optional<Schedule> findSchedule = scheduleRepository.findById(schedule.getId());
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            if(findSchedule.isPresent()) {
                BeanUtils.copyProperties(findSchedule.get(), scheduleDTO);
            }
            ArrayList employeeIds = new ArrayList<>();
            ArrayList petIds = new ArrayList<>();
            findSchedule.get().getEmployees().forEach(employee -> {
                employeeIds.add(employee.getId());
            });
            Collections.sort(employeeIds);
            findSchedule.get().getPets().forEach(pet -> {
                petIds.add(pet.getId());
            });
            Collections.sort(petIds);
            scheduleDTO.setPetIds(petIds);
            scheduleDTO.setEmployeeIds(employeeIds);
            scheduleDTOList.add(scheduleDTO);
        }
        return scheduleDTOList;
    }

    /**
     * Get Schedules of Employee
     * @param employeeId
     * @return list of ScheduleDTOs
     */
    public List<ScheduleDTO> getScheduleForEmployee(long employeeId) {
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        Optional<Employee> findEmployee = this.employeeRepository.findById(employeeId);
        ArrayList<Employee> employeeList = new ArrayList<>();
        if(findEmployee.isPresent()) {
            employeeList.add(findEmployee.get());
        }
        List<Schedule> scheduleList = this.scheduleRepository.findAllByEmployeesIn(employeeList);
        for(Schedule schedule: scheduleList) {
            Optional<Schedule> findSchedule = scheduleRepository.findById(schedule.getId());
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            if(findEmployee.isPresent()) {
                BeanUtils.copyProperties(findSchedule.get(), scheduleDTO);
            }
            ArrayList employeeIds = new ArrayList<>();
            ArrayList petIds = new ArrayList<>();
            findSchedule.get().getEmployees().forEach(employee -> {
                employeeIds.add(employee.getId());
            });
            Collections.sort(employeeIds);
            findSchedule.get().getPets().forEach(pet -> {
                petIds.add(pet.getId());
            });
            Collections.sort(petIds);
            scheduleDTO.setPetIds(petIds);
            scheduleDTO.setEmployeeIds(employeeIds);
            scheduleDTOList.add(scheduleDTO);
        }
        return scheduleDTOList;
    }

    /**
     * Get schedules of customer
     * @param customerId
     * @return list of ScheduleDTOs
     */
    public List<ScheduleDTO> getScheduleForCustomer(long customerId) {
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        List<Schedule> scheduleList = jdbcTemplate.query(
                GET_SCHEDULE_BY_CUSTOMER,
                new MapSqlParameterSource().addValue("customerId", customerId),
                new BeanPropertyRowMapper<>(Schedule.class));
        for(Schedule schedule: scheduleList) {
            Optional<Schedule> findSchedule = scheduleRepository.findById(schedule.getId());
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            if(findSchedule.isPresent()) {
                BeanUtils.copyProperties(findSchedule.get(), scheduleDTO);
            }

            ArrayList employeeIds = new ArrayList<>();
            ArrayList petIds = new ArrayList<>();
            findSchedule.get().getEmployees().forEach(employee -> {
                employeeIds.add(employee.getId());
            });
            Collections.sort(employeeIds);
            findSchedule.get().getPets().forEach(pet -> {
                petIds.add(pet.getId());
            });
            Collections.sort(petIds);
            scheduleDTO.setPetIds(petIds);
            scheduleDTO.setEmployeeIds(employeeIds);
            scheduleDTOList.add(scheduleDTO);
        }
        return scheduleDTOList;
    }
}
