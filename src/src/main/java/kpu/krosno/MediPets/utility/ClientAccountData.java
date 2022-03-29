package kpu.krosno.MediPets.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientAccountData {
    private String personId;
    private String email;
    private String password1;
    private String password2;
}
