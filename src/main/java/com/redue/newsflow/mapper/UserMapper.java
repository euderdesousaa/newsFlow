package com.redue.newsflow.mapper;

import com.redue.newsflow.dto.SignUpDto;
import com.redue.newsflow.entities.User;
import com.redue.newsflow.dto.user.UserDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDTO(User entity);

    @InheritInverseConfiguration
    User toEntity(UserDto dto);

    User toInsertDto(SignUpDto dto);

    SignUpDto toEntityInsert(User dto);
}
