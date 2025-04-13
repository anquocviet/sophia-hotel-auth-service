package vn.edu.iuh.authservice.mappers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import vn.edu.iuh.authservice.dtos.requests.CreateUserRequest;
import vn.edu.iuh.authservice.dtos.requests.UpdateUserRequest;
import vn.edu.iuh.authservice.dtos.responses.UserResponse;
import vn.edu.iuh.authservice.enums.Gender;
import vn.edu.iuh.authservice.enums.Role;
import vn.edu.iuh.authservice.models.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-12T13:27:10+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(CreateUserRequest userRequest) {
        if ( userRequest == null ) {
            return null;
        }

        User user = new User();

        if ( userRequest.role() != null ) {
            user.setRole( UserMapper.intToRole( Integer.parseInt( userRequest.role() ) ) );
        }
        user.setGender( UserMapper.intToGender( userRequest.gender() ) );
        user.setUsername( userRequest.username() );
        user.setFullName( userRequest.fullName() );
        user.setEmail( userRequest.email() );
        user.setPhone( userRequest.phone() );
        user.setPassword( userRequest.password() );
        user.setAddress( userRequest.address() );
        user.setAvatarUrl( userRequest.avatarUrl() );
        user.setBirthdate( userRequest.birthdate() );
        user.setUpdatedAt( userRequest.updatedAt() );
        user.setDeletedAt( userRequest.deletedAt() );

        user.setId( java.util.UUID.randomUUID() );
        user.setCreatedAt( java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()) );

        return user;
    }

    @Override
    public UserResponse toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        String username = null;
        String fullName = null;
        String email = null;
        String phone = null;
        Role role = null;
        String address = null;
        String avatarUrl = null;
        Date birthdate = null;
        Gender gender = null;
        Timestamp createdAt = null;
        Timestamp updatedAt = null;
        Timestamp deletedAt = null;

        id = user.getId();
        username = user.getUsername();
        fullName = user.getFullName();
        email = user.getEmail();
        phone = user.getPhone();
        role = user.getRole();
        address = user.getAddress();
        avatarUrl = user.getAvatarUrl();
        birthdate = user.getBirthdate();
        gender = user.getGender();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
        deletedAt = user.getDeletedAt();

        UserResponse userResponse = new UserResponse( id, username, fullName, email, phone, role, address, avatarUrl, birthdate, gender, createdAt, updatedAt, deletedAt );

        return userResponse;
    }

    @Override
    public void updateUserFromRequest(UpdateUserRequest userRequest, User user) {
        if ( userRequest == null ) {
            return;
        }

        if ( userRequest.fullName() != null ) {
            user.setFullName( userRequest.fullName() );
        }
        if ( userRequest.avatarUrl() != null ) {
            user.setAvatarUrl( userRequest.avatarUrl() );
        }
        if ( userRequest.birthdate() != null ) {
            user.setBirthdate( userRequest.birthdate() );
        }
        if ( userRequest.gender() != null ) {
            user.setGender( userRequest.gender() );
        }
        if ( userRequest.role() != null ) {
            user.setRole( userRequest.role() );
        }
        if ( userRequest.phone() != null ) {
            user.setPhone( userRequest.phone() );
        }
        if ( userRequest.email() != null ) {
            user.setEmail( userRequest.email() );
        }
        if ( userRequest.username() != null ) {
            user.setUsername( userRequest.username() );
        }
        if ( userRequest.password() != null ) {
            user.setPassword( userRequest.password() );
        }
        if ( userRequest.address() != null ) {
            user.setAddress( userRequest.address() );
        }
        if ( userRequest.id() != null ) {
            user.setId( UUID.fromString( userRequest.id() ) );
        }
        if ( userRequest.createdAt() != null ) {
            user.setCreatedAt( userRequest.createdAt() );
        }
        if ( userRequest.deletedAt() != null ) {
            user.setDeletedAt( userRequest.deletedAt() );
        }

        user.setUpdatedAt( java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()) );
    }

    @Override
    public User partialUpdate(UserResponse userResponse, User user) {
        if ( userResponse == null ) {
            return user;
        }

        if ( userResponse.id() != null ) {
            user.setId( userResponse.id() );
        }
        if ( userResponse.username() != null ) {
            user.setUsername( userResponse.username() );
        }
        if ( userResponse.fullName() != null ) {
            user.setFullName( userResponse.fullName() );
        }
        if ( userResponse.email() != null ) {
            user.setEmail( userResponse.email() );
        }
        if ( userResponse.phone() != null ) {
            user.setPhone( userResponse.phone() );
        }
        if ( userResponse.role() != null ) {
            user.setRole( userResponse.role() );
        }
        if ( userResponse.address() != null ) {
            user.setAddress( userResponse.address() );
        }
        if ( userResponse.avatarUrl() != null ) {
            user.setAvatarUrl( userResponse.avatarUrl() );
        }
        if ( userResponse.birthdate() != null ) {
            user.setBirthdate( userResponse.birthdate() );
        }
        if ( userResponse.gender() != null ) {
            user.setGender( userResponse.gender() );
        }
        if ( userResponse.createdAt() != null ) {
            user.setCreatedAt( userResponse.createdAt() );
        }
        if ( userResponse.updatedAt() != null ) {
            user.setUpdatedAt( userResponse.updatedAt() );
        }
        if ( userResponse.deletedAt() != null ) {
            user.setDeletedAt( userResponse.deletedAt() );
        }

        return user;
    }
}
