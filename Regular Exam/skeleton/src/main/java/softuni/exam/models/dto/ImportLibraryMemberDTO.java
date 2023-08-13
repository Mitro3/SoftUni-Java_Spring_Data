package softuni.exam.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ImportLibraryMemberDTO {

    @Size(min = 2, max = 40)
    private String address;

    @Size(min = 2, max = 30)
    @NotNull
    private String firstName;

    @Size(min = 2, max = 30)
    @NotNull
    private String lastName;

    @Size(min = 2, max = 20)
    @NotNull
    private String phoneNumber;

    public ImportLibraryMemberDTO() {
    }

    public String getAddress() {
        return address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
