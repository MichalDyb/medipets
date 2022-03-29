package kpu.krosno.MediPets.models;

import lombok.Data;
import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(AccountRoleKey.class)
@Data
public class AccountsRoles {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="accountId", nullable=false)
    private Accounts accountId;
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="roleId", nullable=false)
    private Roles roleId;

    public AccountsRoles() {
    }

    public AccountsRoles(Accounts accountId, Roles roleId) {
        this.accountId = accountId;
        this.roleId = roleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountId(), getRoleId());
    }

    @Override
    public String toString() {
        return "AccountsRoles{" +
                "accountId=" + accountId +
                ", roleId=" + roleId +
                '}';
    }
}
