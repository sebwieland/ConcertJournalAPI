package com.ConcertJournalAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required.")
    private String username;
    private String password;
    //@NotBlank(message = "Email is required.")
    private String email;
    private String firstName;
    private String lastName;
    @NotNull(message = "Role is required.")
    private String role;

    @JsonIgnore
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BandEvent> bandEvents;
}
