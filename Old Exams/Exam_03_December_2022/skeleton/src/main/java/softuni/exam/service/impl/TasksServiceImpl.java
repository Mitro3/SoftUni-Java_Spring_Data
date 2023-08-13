package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TaskExportDTO;
import softuni.exam.models.dto.TaskImportDTO;
import softuni.exam.models.dto.TaskWrapperDTO;
import softuni.exam.models.entity.*;
import softuni.exam.repository.CarsRepository;
import softuni.exam.repository.MechanicsRepository;
import softuni.exam.repository.PartsRepository;
import softuni.exam.repository.TasksRepository;
import softuni.exam.service.TasksService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.models.entity.Constants.*;

@Service
public class TasksServiceImpl implements TasksService {
    private static String TASKS_FILE_PATH = "src/main/resources/files/xml/tasks.xml";

    private final TasksRepository tasksRepository;

    private final PartsRepository partsRepository;

    private MechanicsRepository mechanicsRepository;

    private CarsRepository carsRepository;

    private final ModelMapper modelMapper;

    private final ValidationUtils validationUtils;

    private final XmlParser xmlParser;

    @Autowired
    public TasksServiceImpl(TasksRepository tasksRepository, PartsRepository partsRepository, MechanicsRepository mechanicsRepository, CarsRepository carsRepository, ModelMapper modelMapper, ValidationUtils validationUtils, XmlParser xmlParser) {
        this.tasksRepository = tasksRepository;
        this.partsRepository = partsRepository;
        this.mechanicsRepository = mechanicsRepository;
        this.carsRepository = carsRepository;
        this.modelMapper = modelMapper;
        this.validationUtils = validationUtils;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.tasksRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Files.readString(Path.of(TASKS_FILE_PATH));
    }

    @Override
    public String importTasks() throws IOException, JAXBException {
        final StringBuilder stringBuilder = new StringBuilder();

        List<TaskImportDTO> tasks = this.xmlParser
                .fromFile(Path.of(TASKS_FILE_PATH).toFile(), TaskWrapperDTO.class).getTasks();

        for (TaskImportDTO task : tasks) {
            stringBuilder.append(System.lineSeparator());

            if (this.validationUtils.isValid(task)) {

                Optional<Mechanic> optionalMechanic = this.mechanicsRepository
                        .findBFirstByFirstName(task.getMechanic().getFirstName());

                Optional<Car> optionalCar = this.carsRepository.findById(task.getCar().getId());

                Optional<Part> optionalPart = this.partsRepository.findById(task.getPart().getId());

                if (optionalCar.isEmpty() || optionalMechanic.isEmpty() || optionalPart.isEmpty()) {
                    stringBuilder.append(String.format(INVALID_FORMAT, TASK));
                    continue;
                }

                Task taskToSave = this.modelMapper.map(task, Task.class);
                taskToSave.setMechanic(optionalMechanic.get());
                taskToSave.setCar(optionalCar.get());
                taskToSave.setPart(optionalPart.get());

                this.tasksRepository.save(taskToSave);

                stringBuilder.append(
                        String.format(SUCCESSFULL_FORMAT, TASK,
                                task.getPrice().setScale(2),
                                "").trim());
                continue;
            }

            stringBuilder.append(String.format(INVALID_FORMAT, TASK));
                    }

        return stringBuilder.toString().trim();
    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {

        return this.tasksRepository.findAllByCarCarTypeOrderByPriceDesc(CarType.coupe)
                .stream()
                .map(task -> this.modelMapper.map(task, TaskExportDTO.class))
                .map(TaskExportDTO::toString)
                .collect(Collectors.joining())
                .trim();
    }
}
