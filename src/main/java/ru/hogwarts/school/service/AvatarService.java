package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static org.imgscalr.Scalr.*;

@Service
@Transactional
public class AvatarService {
    private final static Logger logger = LoggerFactory.getLogger(AvatarService.class);
    @Value("avatars")
    private String avatarsDir;
    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    public AvatarService(StudentService studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
        logger.info("@Bean AvatarService is created");
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("uploadAvatar was invoked.");
        Student student = studentService.findStudent(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
            logger.info("Converting data..");
        }
        logger.info("File has been uploaded.");
        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(generateImageData(filePath));

        avatarRepository.save(avatar);
        logger.info("Avatar id {} has been saved to path {}.", avatar.getId(), filePath);
    }

    public Avatar findAvatar(Long studentId) {
        logger.info("findAvatar was invoked.");
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(new Avatar());
        logger.info("Found avatar {} for student with id {}", avatar, studentId);
        return avatar;
    }

    public String getExtension(String fileName) {
        logger.info("getExtension was invoked.");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private byte[] generateImageData(Path filePath) throws IOException {
        logger.info("generateImageData was invoked.");
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);
            BufferedImage scaledImg = resize(image,100,133);
            BufferedImage preview = new BufferedImage(scaledImg.getWidth(), scaledImg.getHeight(), image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(scaledImg, 0, 0, scaledImg.getWidth(), scaledImg.getHeight(), null);
            graphics.dispose();
            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }
    public List<Avatar> getAvatarPage(Integer pageNum, Integer pageSize) {
        logger.info("getAvatarPage was invoked.");
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }

}
