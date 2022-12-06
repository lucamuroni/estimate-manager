package com.project.webapp.estimatemanager.repository;

import com.project.webapp.estimatemanager.models.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepoTest {
    @Autowired
    private UserRepo userRepo;

    @Test
    public void UserRepo_Save_ReturnSavedUser() {
        //Arrange
        UserEntity user = UserEntity.builder().name("test").email("test@gmail.com").password("tests").build();
        //Act
        UserEntity savedUser = userRepo.save(user);
        //Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepo_GetAll_ReturnMoreThanOneUser() {
        //Arrange
        UserEntity user1 = UserEntity.builder().name("uno").email("uno@gmail.com").password("uno").build();
        UserEntity user2 = UserEntity.builder().name("due").email("due@gmail.com").password("due").build();
        //Act
        userRepo.save(user1);
        userRepo.save(user2);
        List<UserEntity> users = userRepo.findAll();
        //Assert
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users).isNotEmpty();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void UserRepo_FindUserEntityById_ReturnUserWithThatId() {
        //Arrange
        UserEntity user = UserEntity.builder().name("test").email("test@gmail.com").password("tests").build();
        //Act
        userRepo.save(user);
        UserEntity foundUser = userRepo.findUserEntityById(user.getId()).get();
        //Assert
        Assertions.assertThat(foundUser).isNotNull();
    }

    @Test
    public void UserRepo_FindUserEntityByEmail_ReturnUserWithThatEmail() {
        //Arrange
        UserEntity user = UserEntity.builder().name("test").email("test@gmail.com").password("tests").build();
        //Act
        userRepo.save(user);
        UserEntity foundUser = userRepo.findUserEntityByEmail(user.getEmail()).get();
        //Assert
        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void UserRepo_UpdateUser_ReturnUpdatedUser() {
        //Arrange
        UserEntity user = UserEntity.builder().name("test").email("test@gmail.com").password("tests").build();
        //Act
        userRepo.save(user);
        UserEntity savedUser = userRepo.findUserEntityById(user.getId()).get();
        savedUser.setEmail("prova@gmail.com");
        UserEntity updatedUser = userRepo.save(savedUser);
        //Assert
        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo(savedUser.getEmail());
    }

    @Test
    public void UserRepo_DeleteUser_ReturnUserIsEmpty() {
        //Arrange
        UserEntity user = UserEntity.builder().name("test").email("test@gmail.com").password("tests").build();
        //Act
        userRepo.save(user);
        userRepo.deleteUserEntityById(user.getId());
        Optional<UserEntity> userReturn = userRepo.findUserEntityById(user.getId());
        //Assert
        Assertions.assertThat(userReturn).isEmpty();
    }
}
