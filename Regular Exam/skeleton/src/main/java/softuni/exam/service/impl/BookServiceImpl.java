package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.entity.Book;
import softuni.exam.models.dto.ImportBookDTO;
import softuni.exam.repository.BookRepository;
import softuni.exam.service.BookService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private static final String BOOKS_FILE_PATH = "src/main/resources/files/json/books.json";

    private final BookRepository bookRepository;

    private final ValidationUtils validationUtils;

    private final Gson gson;

    private final ModelMapper modelMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, ValidationUtils validationUtils, Gson gson, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.validationUtils = validationUtils;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.bookRepository.count() > 0;
    }

    @Override
    public String readBooksFromFile() throws IOException {
        return Files.readString(Path.of(BOOKS_FILE_PATH));
    }

    @Override
    public String importBooks() throws IOException {
        ImportBookDTO[] importBookDTOS = this.gson.fromJson(readBooksFromFile(), ImportBookDTO[].class);

        return Arrays.stream(importBookDTOS)
                .map(this::importBook)
                .collect(Collectors.joining());
     }

    private String importBook(ImportBookDTO dto) {
        Optional<Book> optionalBook = this.bookRepository.findByTitle(dto.getTitle());

        String message = "";

        if (optionalBook.isEmpty() && this.validationUtils.isValid(dto)) {
            Book book = this.modelMapper.map(dto, Book.class);

            this.bookRepository.save(book);

            message = String.format("Successfully imported book %s - %s%n", dto.getAuthor(), dto.getTitle());

        } else {
            message = String.format("Invalid book%n");
        }

        return message;
    }
}
