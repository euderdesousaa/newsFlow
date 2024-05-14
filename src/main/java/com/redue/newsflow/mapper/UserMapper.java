package com.redue.newsflow.mapper;

import com.redue.newsflow.dto.SignUpDto;
import com.redue.newsflow.dto.user.UserDto;
import com.redue.newsflow.dto.user.UserUpdateDTO;
import com.redue.newsflow.entities.User;
import com.redue.newsflow.entities.UserLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDTO(User entity);

    User toEntity(UserDto dto);

    SignUpDto toInsertDto(User dto);

    User toEntityInsert(SignUpDto dto);

    UserUpdateDTO toUpdate(User entity);

    UserLocation toLocationEntity(SignUpDto dto);


}
