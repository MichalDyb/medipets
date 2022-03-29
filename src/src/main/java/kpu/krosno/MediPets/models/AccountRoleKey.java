package kpu.krosno.MediPets.models;

import lombok.Data;
import java.io.Serializable;

@Data
public class AccountRoleKey implements Serializable {
    private Long accountId;
    private Long roleId;
}
