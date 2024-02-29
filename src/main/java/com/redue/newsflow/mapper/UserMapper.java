package com.redue.newsflow.mapper;

import com.redue.newsflow.dto.SignUpDto;
import com.redue.newsflow.entities.User;
import com.redue.newsflow.dto.user.UserDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDTO(User entity);

    User toEntity(UserDto dto);

    SignUpDto toInsertDto(User dto);

    @Mapping(target = "username", source = "username")
    User toEntityInsert(SignUpDto dto);
}
