package model;

import io.quarkus.security.jpa.RolesValue;
import jakarta.persistence.*;

import java.util.List;

@Entity(name = "Roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToMany
//   public List<User> users;

    @RolesValue
    public String role;


}
