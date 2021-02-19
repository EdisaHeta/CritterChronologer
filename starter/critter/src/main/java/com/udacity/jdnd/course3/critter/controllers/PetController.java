package com.udacity.jdnd.course3.critter.controllers;

import com.udacity.jdnd.course3.critter.DTOs.PetDTO;
import com.udacity.jdnd.course3.critter.services.PetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    /**
     * Saves Pet
     * @param petDTO
     * @return PetDTO
     */
    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        try{
            return this.petService.savePet(petDTO);
        } catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not save Pet!");
        }
    }

    /**
     * Gets pet by id
     * @param petId
     * @return PetDTO
     */
    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        try {
            return this.petService.getPet(petId);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not get Pet!");
        }
    }

    /**
     * Gets list of pets
     * @return list of PetDTOs
     */
    @GetMapping
    public List<PetDTO> getPets(){
        try{
            return this.petService.getPets();
        } catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not get Pets!");
        }
    }

    /**
     * Get pets by owner id
     * @param ownerId
     * @return list of PetDTOs
     */
    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        try{
            return this.petService.getPetsByOwner(ownerId);
        } catch(Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("Could not get Owner's pets!");
        }
    }
}
