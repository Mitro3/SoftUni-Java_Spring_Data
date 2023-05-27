package softuni.exam.models.dto;

import javax.validation.constraints.Size;

public class ImportAgentDTO {
    @Size(min = 2)
    private String firstName;

    @Size(min = 2)
    private String lastName;
    private String town;
    private String email;

    public ImportAgentDTO() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
