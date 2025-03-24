package com.lumiring.minimacs.entity.user;
import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "roles", schema = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название роли, например "ROLE_ADMIN"
      */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Override
    public String getAuthority() {
        return name;
    }

}

