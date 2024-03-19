package com.telerikacademy.web.virtualwallet.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.filters.UserFilterOptions;
import com.telerikacademy.web.virtualwallet.models.Role;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.repositories.RoleRepositoryImpl;
import com.telerikacademy.web.virtualwallet.repositories.contracts.ProfilePhotoRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.utils.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.telerikacademy.web.virtualwallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @InjectMocks
    UserServiceImpl mockUserService;

    @Mock
    WalletServiceImpl mockWalletService;

    @Mock
    UserRepository mockUserRepository;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private ProfilePhotoRepository mockProfilePhotoRepository;

    @Mock
    private RoleRepositoryImpl mockRoleRepository;

    @Test
    void getAll_Should_CallRepository() {
        //Arrange, Act
        User testUser = createMockUser();
        UserFilterOptions userFilterOptions = new UserFilterOptions("testUsername", "test@email.com", "1234567890");
        Mockito.when(mockUserRepository.getAllUsersFiltered(userFilterOptions)).thenReturn(new ArrayList<>());
        mockUserService.getAll(userFilterOptions, testUser);

        //Assert
        Mockito.verify(mockUserRepository, Mockito.times(1)).getAllUsersFiltered(userFilterOptions);
    }

    @Test
    void getById_Should_ReturnUser_When_MatchByIdExists() {
        //Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.getById(Mockito.anyInt()))
                .thenReturn(mockUser);
        //Act
        User result = mockUserService.getById(mockUser.getId());

        //Assert
        Assertions.assertEquals(mockUser, result);
    }

    @Test
    void getByUsername_Should_ReturnUser_When_MatchExists() {
        //Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.getByField("username", mockUser.getUsername()))
                .thenReturn(mockUser);
        //Act
        User result = mockUserService.getByUsername("MockUsername");

        //Assert
        Assertions.assertEquals(mockUser, result);
    }

    @Test
    void getByFirstName_Should_ReturnUser_When_MatchExists() {
        //Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.getByField("firstName", mockUser.getFirstName()))
                .thenReturn(mockUser);
        //Act
        User result = mockUserService.getByFirstName("MockFirstName");

        //Assert
        Assertions.assertEquals(mockUser, result);
    }

    @Test
    void getByLastName_Should_ReturnUser_When_MatchExists() {
        //Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.getByField("lastName", mockUser.getLastName()))
                .thenReturn(mockUser);
        //Act
        User result = mockUserService.getByLastName("MockLastName");

        //Assert
        Assertions.assertEquals(mockUser, result);
    }

    @Test
    void getByEmail_Should_ReturnUser_When_MatchExists() {
        //Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.getByField("email", mockUser.getEmail()))
                .thenReturn(mockUser);
        //Act
        User result = mockUserService.getByEmail("mock@user.com");

        //Assert
        Assertions.assertEquals(mockUser, result);
    }

    @Test
    void getByPhoneNumber_Should_ReturnUser_When_MatchExists() {
        //Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.getByField("phoneNumber", mockUser.getPhoneNumber()))
                .thenReturn(mockUser);
        //Act
        User result = mockUserService.getByPhoneNumber("123456789123");

        //Assert
        Assertions.assertEquals(mockUser, result);
    }

    @Test
    void create_Should_CallRepository_When_UserWithSameUsernameOrEmailDoesNotExist() {
        //Arrange
        User user = createMockUser();
        Mockito.when(mockUserRepository.getByField("username", user.getUsername())).thenThrow(EntityNotFoundException.class);
        Mockito.when(mockUserRepository.getByField("email", user.getEmail())).thenThrow(EntityNotFoundException.class);
        Mockito.when(cloudinary.uploader()).thenReturn(uploader);
        //Act
        mockUserService.create(user);

        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(1)).create(user);


    }

    @Test
    void create_Should_CallWalletService_When_UserWithSameUsernameOrEmailDoesNotExist() {
        //Arrange
        User user = createMockUser();
        Mockito.when(mockUserRepository.getByField("username", user.getUsername())).thenThrow(EntityNotFoundException.class);
        Mockito.when(mockUserRepository.getByField("email", user.getEmail())).thenThrow(EntityNotFoundException.class);
        Mockito.when(cloudinary.uploader()).thenReturn(uploader);
        //Act
        mockUserService.create(user);

        // Assert
        Mockito.verify(mockWalletService, Mockito.times(1))
                .create(mockWalletService.createDefaultWallet(user));


    }

    @Test
    void create_Should_Throw_When_UserWithSameUsernameExists() {
        //Arrange
        User user = createMockUser();
        Mockito.when(mockUserRepository.getByField("username", user.getUsername())).thenReturn(user);

        //Act, Assert
        Assertions.assertThrows(EntityDuplicateException.class, () -> mockUserService.create(user));
    }

    @Test
    void create_Should_Throw_When_UserWithSameEmailExists() {
        //Arrange
        User user = createMockUser();
        Mockito.when(mockUserRepository.getByField("username", user.getUsername())).thenThrow(EntityNotFoundException.class);
        Mockito.when(mockUserRepository.getByField("email", user.getEmail())).thenReturn(user);

        //Act, Assert
        Assertions.assertThrows(EntityDuplicateException.class, () -> mockUserService.create(user));
    }

    @Test
    void block_Should_Add_Blocked_Role() {
        //Arrange
        User user = createMockUser2();
        Role blockedRole = createMockBlockedRole();
        Mockito.when(mockRoleRepository.getByField("roleType", UserRole.blocked.toString())).thenReturn(blockedRole);
        Mockito.when(mockUserRepository.getById(Mockito.anyInt())).thenReturn(user);
        mockUserService.block(user.getId(), createMockUser());

        //Act, Assert
        Assertions.assertTrue(user.getUserRoles().contains(blockedRole));
        Mockito.verify(mockUserRepository).update(user);

    }

    @Test
    void makeRegular_Should_Add_Regular_Role() {
        //Arrange, Act
        User user = createMockUser2();
        Role regularRole = createMockRegularRole();
        Mockito.when(mockRoleRepository.getByField("roleType", UserRole.regular.toString())).thenReturn(regularRole);
        mockUserService.makeRegular(user);

        //Assert
        Assertions.assertTrue(user.getUserRoles().contains(regularRole));
        Mockito.verify(mockUserRepository).update(user);

    }

    @Test
    void unblock_Should_Remove_Blocked_Role() {
        //Arrange
        User user = createMockUser2();
        Role blockedRole = createMockBlockedRole();
        Mockito.when(mockRoleRepository.getByField("roleType", UserRole.blocked.toString())).thenReturn(blockedRole);
        Mockito.when(mockUserRepository.getById(Mockito.anyInt())).thenReturn(user);
        user.getUserRoles().add(blockedRole);

        //Act
        mockUserService.unblock(user.getId(), createMockUser());


        //Assert
        Assertions.assertFalse(user.getUserRoles().contains(blockedRole));
        Mockito.verify(mockUserRepository).update(user);

    }

    @Test
    void isBlocked_Should_Return_True_When_UserIsBlocked() {
        //Arrange
        User user = createMockUser2();
        user.getUserRoles().add(createMockBlockedRole());

        //Act, Assert
        Assertions.assertTrue(mockUserService.isBlocked(user));
    }

    @Test
    void isRegular_Should_Return_True_When_UserIsRegular() {
        //Arrange
        User user = createMockUser2();
        user.getUserRoles().add(createMockRegularRole());

        //Act, Assert
        Assertions.assertTrue(mockUserService.isRegular(user));
    }

    @Test
    void isAdmin_Should_Return_True_When_UserIsAdmin() {
        //Arrange
        User user = createMockUser();

        //Act, Assert
        Assertions.assertTrue(mockUserService.isAdmin(user));
    }


}
