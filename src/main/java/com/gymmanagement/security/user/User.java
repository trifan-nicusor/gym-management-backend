package com.gymmanagement.security.user;

import com.gymmanagement.cart.Cart;
import com.gymmanagement.security.token.confirmation.ConfirmationToken;
import com.gymmanagement.security.token.jwt.JwtToken;
import com.gymmanagement.security.token.reset.ResetToken;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private Boolean isLocked;
    private Boolean isEnabled;
    private LocalDateTime updatedAt;
    private LocalDateTime confirmedAt;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<JwtToken> jwtTokenList;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<ResetToken> resetTokenList;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<ConfirmationToken> confirmationTokenList;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<Cart> carts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}