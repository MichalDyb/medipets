package kpu.krosno.MediPets.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterData {
    private String name;
    private String surname;
    private String pesel;
    private String telephone;
    private String address;
    private String postCode;
    private String city;
    private String email;
    private String password1;
    private String password2;
}
