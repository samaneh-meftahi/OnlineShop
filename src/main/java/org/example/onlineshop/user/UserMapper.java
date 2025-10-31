package org.example.onlineshop.user;

import org.example.onlineshop.user.dto.UserRequestDTO;
import org.example.onlineshop.user.dto.UserResponseDTO;
import org.example.onlineshop.user.model.User;

public class UserMapper {
    public static UserResponseDTO toDto(User user){
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setAddress(user.getAddress());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setActive(user.isActive());
        dto.setTwoFactor(user.isTwoFactor());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public static User toEntity(UserRequestDTO dto){
        User user=new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(user.getPassword());
        user.setPhone(user.getPhone());
        user.setAddress(user.getAddress());

        return user;
    }
}
