package com.example.bookShop.services.author;

import com.example.bookShop.domain.entities.Author;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AuthorService {

    void seedAuthors(List<Author> authors);

    boolean isDataSeeded();

    Author getRandomAuthorById();

    List<Author> findDistinctByBooksReleaseDateBefore(LocalDate date);


}
