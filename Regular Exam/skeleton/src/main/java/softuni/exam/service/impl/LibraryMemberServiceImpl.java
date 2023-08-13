package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportLibraryMemberDTO;
import softuni.exam.models.entity.LibraryMember;
import softuni.exam.repository.LibraryMemberRepository;
import softuni.exam.service.LibraryMemberService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibraryMemberServiceImpl implements LibraryMemberService {
    private final LibraryMemberRepository libraryMemberRepository;

    private static final String MEMBERS_FILE_PATH = "src/main/resources/files/json/library-members.json";

    private final ValidationUtils validationUtils;

    private final Gson gson;

    private final ModelMapper modelMapper;

    @Autowired
    public LibraryMemberServiceImpl(LibraryMemberRepository libraryMemberRepository, ValidationUtils validationUtils, Gson gson, ModelMapper modelMapper) {
        this.libraryMemberRepository = libraryMemberRepository;
        this.validationUtils = validationUtils;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.libraryMemberRepository.count() > 0;
    }

    @Override
    public String readLibraryMembersFileContent() throws IOException {
        return Files.readString(Path.of(MEMBERS_FILE_PATH));
    }

    @Override
    public String importLibraryMembers() throws IOException {
        ImportLibraryMemberDTO[] importLibraryMemberDTOS = this.gson.fromJson(readLibraryMembersFileContent(), ImportLibraryMemberDTO[].class);

        return Arrays.stream(importLibraryMemberDTOS)
                .map(dto -> importLibraryMember(dto))
                .collect(Collectors.joining("\n"));
    }

    private String importLibraryMember(ImportLibraryMemberDTO dto) {
        Optional<LibraryMember> optionalLibraryMember = this.libraryMemberRepository.findByPhoneNumber(dto.getPhoneNumber());

        String message = "";

        if (optionalLibraryMember.isEmpty() && this.validationUtils.isValid(dto)) {
            LibraryMember libraryMember = this.modelMapper.map(dto, LibraryMember.class);

            this.libraryMemberRepository.save(libraryMember);

            message = String.format("Successfully imported library member %s - %s",
                    libraryMember.getFirstName(), libraryMember.getLastName());

        } else {
            message = "Invalid library member";
        }

        return message;
    }
}
