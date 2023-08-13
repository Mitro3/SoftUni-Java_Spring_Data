package softuni.exam.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "books")
public class Book extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean available;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(nullable = false)
    private Double rating;

    public Book() {
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isAvailable() {
        return available;
    }

    public Genre getGenre() {
        return genre;
    }

    public Double getRating() {
        return rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
