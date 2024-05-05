package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import java.util.Collection;

@Service
public class FacultyService {
    private final static Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
        logger.info("@Bean FacultyService is created");
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("addFaculty was invoked.");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        logger.info("findFaculty was invoked.");
        return facultyRepository.findById(id)
                .orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("editFaculty was invoked.");
        return facultyRepository.findById(faculty.getId())
                .map(e -> facultyRepository.save(faculty))
                .orElse(null);
    }

    public void removeFaculty(long id) {
        logger.info("removeFaculty was invoked.");
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> findByNameOrColor(String name, String color) {
        logger.info("findByNameOrColor was invoked.");
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public Collection<Faculty> findAll() {
        logger.info("findAll was invoked.");
        return facultyRepository.findAll();
    }
}
