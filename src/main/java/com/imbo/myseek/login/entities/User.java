package com.imbo.myseek.login.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * A user.
 */
@Entity
@Table(name = "USER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User implements Serializable {

    public static final int MIN_LOGIN_LENGTH = 6;

    public User() {
    }

    public User(@JsonProperty("id") Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @NotNull
    @Size(min = 6, max = 60)
    @Column(length = 60)
    private String password;

    @Size(max = 50)
    @NotNull
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Size(min = 6)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @Size(min = 6)
    @Column(name = "english_letters_of_fullname", length = 50, unique = true, nullable = false)
    private String englishLettersOfFullname;

    @Email
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean activated = false;

    // Indicates the password is still the default password
    // or has been upgraded to a random or manual set one
    @Column(name = "password_upgraded", nullable = false)
    private boolean passwordUpgraded = false;

    @Size(min = 2, max = 5)
    @Column(name = "lang_key", length = 5)
    private String langKey;

    @Size(max = 50)
    @Column(name = "activation_key", length = 50, unique = true)
    @JsonIgnore
    private String activationKey;





}
