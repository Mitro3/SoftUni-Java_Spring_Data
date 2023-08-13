package softuni.exam.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "borrowing_record")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportRecordDTO {
    @XmlElement(name = "borrow_date")
    @NotNull
    private String borrowDate;
    @XmlElement(name = "return_date")
    @NotNull
    private String returnDate;

    @XmlElement
    @NotNull
    private BookTitleDTO book;

    @XmlElement
    @NotNull
    private MemberIdDTO member;

    @XmlElement
    @Size(min = 3, max = 100)
    private String remarks;

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public BookTitleDTO getBook() {
        return book;
    }

    public MemberIdDTO getMember() {
        return member;
    }

    public String getRemarks() {
        return remarks;
    }
}
