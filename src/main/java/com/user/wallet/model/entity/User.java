package com.user.wallet.model.entity;

import com.user.wallet.model.state.UserRole;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Size(min = 4, max = 45, message = "User must contains 3 to 45 characters")
  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @Size(min = 4, max = 255, message = "User must contains minimum 3 characters")
  @Column(nullable = false)
  private String name;

  @Column(unique = true, nullable = false)
  private String email;

  @Column
  private String location;

  @Size(min = 8, message = "Password must contains 5 characters")
  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  List<UserRole> userRoles;

  @Column(name = "created_date")
  private Date createdDate;

  @Column(name = "last_updated_date")
  private Date lastUpdatedDate;
}
