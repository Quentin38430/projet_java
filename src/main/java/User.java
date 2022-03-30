import java.sql.Date;
import java.sql.Timestamp;

public class User
{
    private String numero_secu;
    private String lastname;
    private String firstname;
    private Date date_of_birth;
    private String phone_number;
    private String email;
    private Integer id_remboursement;
    private Integer code_soin;
    private String montant_remboursement;
    private Timestamp timestamp;

    public String getNumero_secu() {
        return numero_secu;
    }

    public void setNumero_secu(String numero_secu) {
        this.numero_secu = numero_secu;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId_remboursement() {
        return id_remboursement;
    }

    public void setId_remboursement(Integer id_remboursement) {
        this.id_remboursement = id_remboursement;
    }

    public Integer getCode_soin() {
        return code_soin;
    }

    public void setCode_soin(Integer code_soin) {
        this.code_soin = code_soin;
    }

    public String getMontant_remboursement() {
        return montant_remboursement;
    }

    public void setMontant_remboursement(String montant_remboursement) {
        this.montant_remboursement = montant_remboursement;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
