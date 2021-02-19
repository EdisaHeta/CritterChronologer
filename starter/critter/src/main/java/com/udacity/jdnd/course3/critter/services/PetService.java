package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.DTOs.PetDTO;
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
public class PetService {

    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Create new Pet
     * @param petDTO
     * @return PetDTO
     */
    public PetDTO savePet(PetDTO petDTO) {
        Pet newPet = new Pet();
        newPet.setName(petDTO.getName());
        newPet.setType(petDTO.getType());
        Optional<Customer> findCustomer = customerRepository.findById(petDTO.getOwnerId());
        if(findCustomer.isPresent()){
            newPet.setOwner(findCustomer.get());
        }
        newPet.setBirthDate(petDTO.getBirthDate());
        newPet.setNotes(petDTO.getNotes());
        this.petRepository.save(newPet);
        BeanUtils.copyProperties(newPet, petDTO);
        return petDTO;
    }

    /**
     * Get pet by id
     * @param petId
     * @return PetDTO
     */
    public PetDTO getPet(long petId) {
        Optional<Pet> pet = petRepository.findById(petId);
        if(pet.isPresent()) {
            PetDTO petDTO = new PetDTO();
            BeanUtils.copyProperties(pet.get(), petDTO);
            petDTO.setOwnerId(pet.get().getOwner().getId());
            return petDTO;
        } else
            return null;
    }

    /**
     * Get list of pets
     * @return list of PetDTOs
     */
    public List<PetDTO> getPets() {
        List<Pet> pets = this.petRepository.findAll();
        List<PetDTO> petDTOS = new ArrayList<>();
        for(Pet pet: pets) {
            PetDTO petDTO = new PetDTO();
            BeanUtils.copyProperties(pet, petDTO);
            petDTO.setOwnerId(pet.getOwner().getId());
            petDTOS.add(petDTO);
        }
        return petDTOS;
    }

    /**
     * Get list of pets by ownerId
     * @param ownerId
     * @return list of PetDTOs
     */
    public List<PetDTO> getPetsByOwner(long ownerId) {
        Optional<Customer> findCustomer = this.customerRepository.findById(ownerId);
        List<PetDTO> petDTOS = new ArrayList<>();
        if(findCustomer.isPresent()) {
            List<Pet> pets = this.petRepository.findAllByOwner(findCustomer.get());
            for(Pet pet: pets) {
                PetDTO petDTO = new PetDTO();
                BeanUtils.copyProperties(pet, petDTO);
                petDTO.setOwnerId(ownerId);
                petDTOS.add(petDTO);
            }
        }

        return petDTOS;
    }
}
