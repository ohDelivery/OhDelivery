package com.ohdelivery.service.user.domain.model;

import com.ohdelivery.common.passport.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user")
@Entity
public class User {

    @Column(unique = true, updatable = false, nullable = false)
    String username;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RoleType role;
    @Column(nullable = false)
    String slackId;
    @Column(nullable = false)
    Boolean isDeleted;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public static User create(String username, String name, String password, RoleType roleType, String slackId) {
        return User.builder()
                .username(username)
                .name(name)
                .password(password)
                .role(roleType)
                .slackId(slackId)
                .isDeleted(false)
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }

}
