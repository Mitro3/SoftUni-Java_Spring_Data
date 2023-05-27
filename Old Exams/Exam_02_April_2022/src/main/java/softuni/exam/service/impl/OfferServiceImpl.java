package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportOfferDTO;
import softuni.exam.models.dto.ImportOfferRootDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.XmlParser;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;

    private final XmlParser xmlParser;

    private final String OFFER_XML_PATH = "src/main/resources/files/xml/offers.xml";

    private final Validator validator;

    private final String INVALID_OFFER_MESSAGE = "Invalid offer";

    private final AgentRepository agentRepository;

    private final ApartmentRepository apartmentRepository;

    private final ModelMapper modelMapper;

    private final String THREE_ROOMS = "three_rooms";
    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, XmlParser xmlParser, Validator validator, AgentRepository agentRepository, ApartmentRepository apartmentRepository, ModelMapper modelMapper) {
        this.offerRepository = offerRepository;
        this.xmlParser = xmlParser;
        this.validator = validator;
        this.agentRepository = agentRepository;
        this.apartmentRepository = apartmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(Path.of(OFFER_XML_PATH)));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        ImportOfferRootDTO offers = xmlParser.fromFile(OFFER_XML_PATH, ImportOfferRootDTO.class);

        return offers.getOffers().stream()
                .map(this::importSingleOffer)
                .collect(Collectors.joining("\n"));
    }

    private String importSingleOffer(ImportOfferDTO dto) {
        Set<ConstraintViolation<ImportOfferDTO>> errors = validator.validate(dto);

        if (!errors.isEmpty()) {
            return INVALID_OFFER_MESSAGE;
        }

        Optional<Agent> optionalAgent = this.agentRepository.findByFirstName(dto.getAgent().getName());

        if (optionalAgent.isEmpty()) {
            return INVALID_OFFER_MESSAGE;
        }

        Optional<Apartment> optionalApartment = this.apartmentRepository.findById(dto.getApartment().getId());

        Offer offer = this.modelMapper.map(dto, Offer.class);
        offer.setAgent(optionalAgent.get());
        offer.setApartment(optionalApartment.get());

        this.offerRepository.save(offer);

        return String.format("Successfully imported offer %.2f", offer.getPrice());
    }

    @Override
    public String exportOffers() {
        StringBuilder sb = new StringBuilder();

        List<Offer> offerListThreeRooms = offerRepository.findAllByApartment_ApartmentTypeOrderByApartment_AreaDescPriceAsc(ApartmentType.three_rooms);

        offerListThreeRooms.forEach(o -> {
            sb.append(String.format("Agent %s %s with offer №%d:\n" +
                    "\t-Apartment area: %.2f\n" +
                    "\t--Town: %s\n" +
                    "\t---Price: %.2f$\n",
                    o.getAgent().getFirstName(),
                    o.getAgent().getLastName(),
                    o.getId(),
                    o.getApartment().getArea(),
                    o.getApartment().getTown().getTownName(),
                    o.getPrice()))
                    .append(System.lineSeparator());
        });
        return sb.toString();
    }
}
