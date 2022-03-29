package kpu.krosno.MediPets.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetData {
    private String petId;
    private String speciesId;
    private String breedId;
    private String personId;
    private String petName;
    private String dateOfBirth;
    private String gender;
    private String petDescription;
}
