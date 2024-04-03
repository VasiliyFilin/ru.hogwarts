package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvatarServiceTest {
    @Mock
    AvatarRepository avatarRepository;
    @InjectMocks
    AvatarService avatarService;
    Avatar avatar = new Avatar();
    Long studentId = 1L;
    String fileName = "1.png";


    @BeforeEach
    void setUp() {
        avatar.setId(1L);
        avatar.setMediaType("image/png");
    }
    @Test
    void findAvatarTest() {
        when(avatarRepository.findByStudentId(studentId)).thenReturn(Optional.ofNullable(avatar));
        Avatar actual = avatarService.findAvatar(studentId);
        assertEquals("image/png", actual.getMediaType());
    }

    @Test
    void getExtensionTest() {
        assertEquals("png", avatarService.getExtension(fileName));
    }
}