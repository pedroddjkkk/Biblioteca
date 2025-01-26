package com.teste.elotech.mapper;

import com.teste.elotech.dto.CreateUserDTO;
import com.teste.elotech.dto.UpdateUserDTO;
import com.teste.elotech.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(CreateUserDTO createUserDTO);
    User toEntity(UpdateUserDTO updateUserDTO);
}