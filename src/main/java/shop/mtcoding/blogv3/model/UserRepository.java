package shop.mtcoding.blogv3.model;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {

        public int insert(@Param("username") String username, @Param("password") String password,
                        @Param("email") String email);

        public List<User> findByUserForSearch(String search);

        public User findById(int id);

        public User findByEmail(String email);

        public List<User> findAll();

        public User findByUser(String username);

        public User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

        public int update(@Param("id") int id, @Param("username") String username, @Param("password") String password,
                        @Param("email") String email, @Param("profile") String profile,
                        @Param("createdAt") Timestamp createdAt);

        public int deleteById(int id);

}
