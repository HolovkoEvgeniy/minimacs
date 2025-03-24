package com.lumiring.minimacs.dao.user;

import com.lumiring.minimacs.entity.user.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    @NotNull Optional<UserEntity> findById(@NotNull Long id);

    Optional<UserEntity> getByEmailIgnoreCase(String email);
    Optional<UserEntity> getByUsernameIgnoreCase(String username);

//    Page<User> findAllWithPagination(Pageable pageable);

    boolean existsByUsername (String username);
    boolean existsByUsernameAndIdNot (String username, Long id);


    boolean existsByEmail (String email);
    boolean existsByEmailAndIdNot (String email, Long id);

}
