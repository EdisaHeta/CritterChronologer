package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.DTOs.CustomerDTO;
import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PetRepository petRepository;

    public CustomerService(CustomerRepository customerRepository, PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.petRepository = petRepository;
    }

    /**
     * Create new Customer
     * @param customerDTO
     * @return CustomerDTO
     */
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer newCustomer = new Customer();
        newCustomer.setPhoneNumber(customerDTO.getPhoneNumber());
        newCustomer.setName(customerDTO.getName());
        this.customerRepository.save(newCustomer);

        BeanUtils.copyProperties(newCustomer, customerDTO);
        return customerDTO;
    }

    /**
     * Get list of Customers
     * @return list of CustomerDTOs
     */
    public List<CustomerDTO> getList() {
        List<CustomerDTO> list = new ArrayList<>();
        for (Customer customer : this.customerRepository.findAll()) {
            CustomerDTO customerDTO = new CustomerDTO();
            BeanUtils.copyProperties(customer, customerDTO);
            ArrayList petIds = new ArrayList();
            List<Pet> findPets = this.petRepository.findAllByOwner(customer);
            findPets.forEach(pet -> {
                    petIds.add(pet.getId());
                });
                customerDTO.setPetIds(petIds);
            list.add(customerDTO);
        }
        return list;
    }

    /**
     * Get owners by petId
     * @param petId
     * @return
     */
    public CustomerDTO getOwnerByPet(long petId) {
        CustomerDTO customerDTO = new CustomerDTO();
        Optional<Pet> findPet = this.petRepository.findById(petId);
        ArrayList<Pet> petList = new ArrayList<>();
        if(findPet.isPresent()) {
            petList.add(findPet.get());
        }
        Optional<Customer> findCustomer = this.customerRepository.findByPetsIn(petList);
        if(findCustomer.isPresent()) {
            BeanUtils.copyProperties(findCustomer.get(), customerDTO);
            List<Pet> pets = this.petRepository.findAllByOwner(findCustomer.get());
            ArrayList petIds = new ArrayList();
            pets.forEach(pet -> {
                petIds.add(pet.getId());
            });
            customerDTO.setPetIds(petIds);
            return customerDTO;
        }
        else {
            return null;
        }
    }
}
