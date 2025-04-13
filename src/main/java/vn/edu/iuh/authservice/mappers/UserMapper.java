//package vn.edu.iuh.authservice.mappers;
//
//import org.mapstruct.BeanMapping;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.MappingTarget;
//import org.mapstruct.NullValuePropertyMappingStrategy;
//import vn.edu.iuh.authservice.dtos.requests.CreateUserRequest;
//import vn.edu.iuh.authservice.dtos.responses.UserResponse;
//import vn.edu.iuh.authservice.models.User;
//
//@Mapper(componentModel = "spring")
//public interface UserMapper {
//   @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
//   @Mapping(target = "createdAt", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))")
//   User toEntity(CreateUserRequest userRequest);
//
//   UserResponse toDto(User user);
//
//   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//   User partialUpdate(UserResponse userResponse, @MappingTarget User user);
//}


package vn.edu.iuh.authservice.mappers;

import org.mapstruct.*;
import vn.edu.iuh.authservice.dtos.requests.CreateUserRequest;
import vn.edu.iuh.authservice.dtos.requests.UpdateUserRequest;
import vn.edu.iuh.authservice.dtos.responses.UserResponse;
import vn.edu.iuh.authservice.enums.Gender;
import vn.edu.iuh.authservice.enums.Role;
import vn.edu.iuh.authservice.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
   @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
   @Mapping(target = "createdAt", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))")
   @Mapping(source = "role", target = "role", qualifiedByName = "intToRole")
   @Mapping(source = "gender", target = "gender", qualifiedByName = "intToGender")
   User toEntity(CreateUserRequest userRequest);

   UserResponse toDto(User user);

   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   @Mapping(source = "fullName", target = "fullName")
   @Mapping(source = "avatarUrl", target = "avatarUrl")
   @Mapping(source = "birthdate", target = "birthdate")
   @Mapping(source = "gender", target = "gender")
   @Mapping(source = "role", target = "role")
   @Mapping(source = "phone", target = "phone")
   @Mapping(source = "email", target = "email")
   @Mapping(source = "username", target = "username")
   @Mapping(source = "password", target = "password")
   @Mapping(source = "address", target = "address")
   @Mapping(target = "updatedAt", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))")
   void updateUserFromRequest(UpdateUserRequest userRequest, @MappingTarget User user);

   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   User partialUpdate(UserResponse userResponse, @MappingTarget User user);

   @Named("intToRole")
   static Role intToRole(Integer value) {
      return value != null ? Role.values()[value] : null;
   }

   @Named("intToGender")
   static Gender intToGender(Integer value) {
      return value != null ? Gender.values()[value] : null;
   }

}
