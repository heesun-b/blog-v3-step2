package shop.mtcoding.blogv3.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String profile;
    private Timestamp createdAt;
}
