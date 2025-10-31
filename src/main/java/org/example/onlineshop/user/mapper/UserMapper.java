package org.example.onlineshop.user.mapper;

import org.example.onlineshop.user.dto.UserRequestDTO;
import org.example.onlineshop.user.dto.UserResponseDTO;
import org.example.onlineshop.user.model.User;
import org.mapstruct.Mapper;
import org.example.onlineshop.common.mapper.BaseMapper;


@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserResponseDTO> {
    User toEntity(UserRequestDTO requestDto);
    UserResponseDTO toDto(User user);
}
