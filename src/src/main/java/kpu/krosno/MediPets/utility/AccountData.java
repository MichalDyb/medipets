package kpu.krosno.MediPets.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountData {
    private String accountId;
    private String personId;
    private String email;
    private String password;
    private String isEnabled;
    private String isLocked;
    private String isExpired;
}
