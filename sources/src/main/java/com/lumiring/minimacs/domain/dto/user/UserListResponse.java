package com.lumiring.minimacs.domain.dto.user;

import com.lumiring.minimacs.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {
    List<UserEntity> userEntityList;
}
