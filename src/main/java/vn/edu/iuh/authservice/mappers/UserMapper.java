package vn.edu.iuh.authservice.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.edu.iuh.authservice.dtos.requests.CreateUserRequest;
import vn.edu.iuh.authservice.dtos.responses.UserResponse;
import vn.edu.iuh.authservice.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
   @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
   @Mapping(target = "createdAt", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))")
   User toEntity(CreateUserRequest userRequest);

   UserResponse toDto(User user);

   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   User partialUpdate(UserResponse userResponse, @MappingTarget User user);
}
