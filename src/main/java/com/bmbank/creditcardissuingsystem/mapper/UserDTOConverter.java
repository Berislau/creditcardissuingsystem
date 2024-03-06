package com.bmbank.creditcardissuingsystem.mapper;

import com.bmbank.creditcardissuingsystem.constants.StatusEnum;
import com.bmbank.creditcardissuingsystem.dto.UserDTO;
import com.bmbank.creditcardissuingsystem.entity.Status;
import com.bmbank.creditcardissuingsystem.entity.User;
import com.bmbank.creditcardissuingsystem.exception.InvalidStatusException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDTOConverter {

    public static User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setOib(userDTO.getOib());

        if (!StatusEnum.existsById(userDTO.getStatusId())) {
            throw new InvalidStatusException("Invalid status ID: " + userDTO.getStatusId());
        }

        Long statusId = userDTO.getStatusId();
        Status status = new Status();
        status.setId(statusId);
        status.setName(StatusEnum.valueOfId(statusId).name());
        user.setStatus(status);
        return user;
    }
}
