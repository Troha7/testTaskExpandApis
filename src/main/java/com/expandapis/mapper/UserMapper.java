package com.expandapis.mapper;

import com.expandapis.dto.request.UserRequestDto;
import com.expandapis.dto.response.UserResponseDto;
import com.expandapis.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * {@link UserMapper}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "password", constant = "********")
  UserResponseDto toResponseDto(User user);

  @Mapping(target = "id", ignore = true)
  User toEntity(UserRequestDto userRequestDto);

}
