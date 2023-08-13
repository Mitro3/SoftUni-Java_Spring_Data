package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportRecordDTO;
import softuni.exam.models.dto.ImportRecordRootDTO;
import softuni.exam.models.entity.Book;
import softuni.exam.models.entity.BorrowingRecord;
import softuni.exam.models.entity.LibraryMember;
import softuni.exam.repository.BookRepository;
import softuni.exam.repository.BorrowingRecordRepository;
import softuni.exam.repository.LibraryMemberRepository;
import softuni.exam.service.BorrowingRecordsService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.models.entity.Genre.SCIENCE_FICTION;

@Service
public class BorrowingRecordsServiceImpl implements BorrowingRecordsService {

    private final BorrowingRecordRepository borrowingRecordRepository;

    private static final String RECORDS_FILE_PATH = "src/main/resources/files/xml/borrowing-records.xml";

    private final ValidationUtils validationUtils;

    private final XmlParser xmlParser;

    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;
    private final LibraryMemberRepository libraryRepository;

    @Autowired
    public BorrowingRecordsServiceImpl(BorrowingRecordRepository borrowingRecordRepository, ValidationUtils validationUtils, XmlParser xmlParser, ModelMapper modelMapper, BookRepository bookRepository, LibraryMemberRepository libraryRepository) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.validationUtils = validationUtils;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;

        this.modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                String.class, LocalDate.class);
    }

    @Override
    public boolean areImported() {
        return this.borrowingRecordRepository.count() > 0;
    }

    @Override
    public String readBorrowingRecordsFromFile() throws IOException {
        return Files.readString(Path.of(RECORDS_FILE_PATH));
    }

    @Override
    public String importBorrowingRecords() throws IOException, JAXBException {
        ImportRecordRootDTO importRecordRootDTO = this.xmlParser
                .fromFile(Path.of(RECORDS_FILE_PATH).toFile(), ImportRecordRootDTO.class);

        List<ImportRecordDTO> records = importRecordRootDTO.getRecords();

        return records.stream()
                .map(this::importRecord)
                .collect(Collectors.joining("\n"));
    }

    private String importRecord(ImportRecordDTO dto) {
        Optional<Book> optionalBook = this.bookRepository.findByTitle(dto.getBook().getTitle());
        Optional<LibraryMember> optionalLibraryMember = this.libraryRepository.findById(dto.getMember().getId());

        String message = "";

        if (optionalBook.isPresent() && optionalLibraryMember.isPresent() && this.validationUtils.isValid(dto)) {
            BorrowingRecord borrowingRecord = this.modelMapper.map(dto, BorrowingRecord.class);

            borrowingRecord.setBook(optionalBook.get());
            borrowingRecord.setLibraryMember(optionalLibraryMember.get());

            this.borrowingRecordRepository.save(borrowingRecord);

            message = String.format("Successfully imported borrowing record "
                    + borrowingRecord.getBook().getTitle() +
                    " - " + borrowingRecord.getBorrowDate());

        } else {
            message = "Invalid borrowing record";
        }
        return message;
    }

    @Override
    public String exportBorrowingRecords() {
        LocalDate beforeDate = LocalDate.of(2021, 9, 10);

        List<BorrowingRecord> records = this.borrowingRecordRepository.findAllByBorrowDateBeforeAndBookGenreOrderByBorrowDateDesc(beforeDate, SCIENCE_FICTION);

        StringBuilder sb = new StringBuilder();

        records.forEach(r -> {
            sb.append(String.format("Book title: %s%n" +
                    "*Book author: %s%n" +
                    "**Date borrowed: %s%n" +
                    "***Borrowed by: %s %s",
                    r.getBook().getTitle(),
                    r.getBook().getAuthor(),
                    r.getBorrowDate().toString(),
                    r.getLibraryMember().getFirstName(),
                    r.getLibraryMember().getLastName()));
            sb.append(System.lineSeparator());
        });

        return sb.toString().trim();
    }
}
