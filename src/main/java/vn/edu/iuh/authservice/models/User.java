package vn.edu.iuh.authservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import vn.edu.iuh.authservice.enums.Gender;
import vn.edu.iuh.authservice.enums.Role;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@FilterDef(name = "deletedUserFilter", parameters = @ParamDef(name = "deletedAt", type = Timestamp.class))
@Filter(name = "deletedUserFilter", condition = "deleted_at IS NULL")
@SQLRestriction("deleted_at IS NULL")
public class User {
   @Id
   @Column(name = "id", nullable = false)
   private UUID id;

   @Column(unique = true)
   private String username;

   @Column(name = "full_name")
   private String fullName;

   @Column(unique = true)
   private String email;

   @Column(unique = true)
   private String phone;

   private String password;

   private Role role;

   private String address;

   @Column(name = "avatar_url")
   private String avatarUrl;

   private Date birthdate;

   private Gender gender;

   @Column(name = "created_at")
   private Timestamp createdAt;

   @Column(name = "updated_at")
   private Timestamp updatedAt;

   @Column(name = "deleted_at")
   private Timestamp deletedAt;
}