package br.com.munnincrow.api.model;

import jakarta.persistence.*;

@Entity
public class UserDeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    private String token;
}