package com.example.bookshopV2.repositories;

import com.example.bookshopV2.domain.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByReleaseDateAfter(LocalDate releaseDate);

    List<Book> findAllBooksByAuthorIdOrderByReleaseDateDesc(int id);
}
