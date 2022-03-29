package kpu.krosno.MediPets.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonData {
    private String personId;
    private String accountId;
    private String name;
    private String surname;
    private String pesel;
    private String telephone;
    private String address;
    private String postCode;
    private String city;
    private String isStandardAccount;
}
