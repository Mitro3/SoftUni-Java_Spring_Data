package softuni.exam.models.dto;

import softuni.exam.models.entity.Genre;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ImportBookDTO {

    @Size(min = 3, max = 40)
    @NotNull
    private String author;

    @NotNull
    private Boolean available;

    @Size(min = 5)
    @NotNull
    private String description;

    @NotNull
    private Genre genre;

    @Size(min = 3, max = 40)
    @NotNull
    private String title;

    @Positive
    @NotNull
    private Double rating;

    public ImportBookDTO() {
    }

    public String getAuthor() {
        return author;
    }

    public Boolean getAvailable() {
        return available;
    }

    public String getDescription() {
        return description;
    }

    public Genre getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public Double getRating() {
        return rating;
    }
}
