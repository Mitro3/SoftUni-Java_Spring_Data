package com.example.springintro;

import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.BookInformation;
import com.example.springintro.model.entity.EditionType;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

//        seedData();
//        printAllBooksAfterYear(2000);
//        printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
//        printAllAuthorsAndNumberOfTheirBooks();
//        printAllBooksByAuthorNameOrderByReleaseDate("George", "Powell");

        //P01
//        String restriction = scanner.nextLine();
//        printAllBookTitlesByAgeRestriction(restriction);

        //P02
//        printAllTitlesByEditionAndCopies().forEach(System.out::println);

        //P03
//        this.bookService.findAllWIthPriceNotBetween(5, 40)
//                .forEach(b -> System.out.println(b.getTitle() + " - " + b.getPrice()));

        //P04
//        int releaseYear = Integer.parseInt(scanner.nextLine());
//        this.bookService.findNotReleasedIn(releaseYear)
//                .forEach(b -> System.out.println(b.getTitle()));

        //P05
//        String date = scanner.nextLine();
//        printAllBooksReleasedBefore(date);

        //P06
//        String endsWith = scanner.nextLine();
//        printAllAuthorsWithFirstNameEndingWith(endsWith);

        //P07
//        String containing = scanner.nextLine();
//        printAllBooksWithTitleContaining(containing);

        //P08
//        String startsWith = scanner.nextLine();
//        printAllBooksWithAuthorLastNameStartingWith(startsWith);

        //P09
//        int length = Integer.parseInt(scanner.nextLine());
//        printCountBooksWithTitleLongerThan(length);

        //P10
//        printAuthorsWithTotalCopiesDescOrder();

        //P11
//        String title = scanner.nextLine();
//        printBookInformationFor(title);

        //P12
//        String date = scanner.nextLine();
//        int amount = Integer.parseInt(scanner.nextLine());
//        int booksUpdated = this.bookService.addCopiesToBooksAfter(date, amount);
//        System.out.printf("%d books are released after %s, so total of %d book copies were added%n",
//                booksUpdated, date, amount * booksUpdated);

        //P13
//        int amount = Integer.parseInt(scanner.nextLine());
//        int deletedBooks = this.bookService.deleteWithCopiesLessThan(amount);
//        System.out.println(deletedBooks);
    }

    private void printBookInformationFor(String title) {
        BookInformation information = this.bookService.getInformationForTitle(title);
        System.out.println(information.getTitle() + " " + information.getEditionType() + " "
        + information.getAgeRestriction() + " " + information.getPrice());
    }

    private void printAuthorsWithTotalCopiesDescOrder() {
        this.authorService.getWithTotalCopies()
                .forEach(a -> System.out.println(
                        a.getFirstName() + " " + a.getLastName() + " - " + a.getTotalCopies()));
    }

    private void printCountBooksWithTitleLongerThan(int length) {
        int totalBooks = this.bookService.countBooksWithTitleLongerThan(length);
        System.out.printf("There are %d books with longer title than %d symbols%n", totalBooks, length);
    }

    private void printAllBooksWithAuthorLastNameStartingWith(String startsWith) {
        this.bookService.findByAuthorLastNameStartsWith(startsWith)
                .forEach(b -> System.out.printf("%s (%s %s)%n",
                        b.getTitle(), b.getAuthor().getFirstName(), b.getAuthor().getLastName()));
    }

    private void printAllBooksWithTitleContaining(String containing) {
        this.bookService.findByContaining(containing)
                .forEach(b -> System.out.println(b.getTitle()));
    }

    private void printAllAuthorsWithFirstNameEndingWith(String endsWith) {
        this.authorService.findByFirstNameEndingWith(endsWith)
                .forEach(a -> System.out.printf("%s %s%n", a.getFirstName(), a.getLastName()));
    }

    private void printAllBooksReleasedBefore(String date) {
        this.bookService.findBooksReleasedBefore(date)
                .forEach(b -> System.out.printf("%s %s %.2f%n", b.getTitle(), b.getEditionType(), b.getPrice()));
    }

    private List<String> printAllTitlesByEditionAndCopies() {
        return this.bookService.findAllTitlesByEditionAndCopies(EditionType.GOLD, 5000);
    }

    private void printAllBookTitlesByAgeRestriction(String restriction) {
        this.bookService.findAllTitlesByAgeRestriction(restriction)
                .forEach(System.out::println);
    }

    private void printAllBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
