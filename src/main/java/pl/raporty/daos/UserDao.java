package pl.raporty.daos;

import org.mindrot.jbcrypt.BCrypt;
import pl.raporty.DBUtil.DbUtil;
import pl.raporty.models.User;

import javax.ws.rs.NotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users (user_name, password) VALUES (?, ?)";
    private static final String READ_USER_QUERY =
            "SELECT * FROM users WHERE user_id = ?";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET user_name = ?, password = ? WHERE user_id = ?";
    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE user_id = ?";
    private static final String CHECK_IF_USER_EXISTS =
            "SELECT * FROM users WHERE user_name = ?";


    public User create (User user) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            int result = statement.executeUpdate();

            if (result != 1) {
                throw new RuntimeException("Execute update returned " + result);
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.first()) {
                    user.setId(generatedKeys.getInt(1));
                    return user;
                } else {
                    throw new RuntimeException("Generated kay was now found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Integer userId) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_QUERY)) {
            statement.setInt(1, userId);
            statement.executeUpdate();

            boolean deleted = statement.execute();
            if (!deleted) {
                throw new NotFoundException("Person not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update (User user) {
        try (Connection conn = DbUtil.getConnection();
        PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY)) {
            statement.setInt(3, user.getId());
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User read(Integer userId) {
        User user = new User();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_USER_QUERY);
        ) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    user.setId(resultSet.getInt("user_id"));
                    user.setName("user_name");
                    user.setPassword("password");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User authorizeAdmin(String name, String plainTextPassword) {
        User user = new User();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_IF_USER_EXISTS)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user.setName(resultSet.getString("user_name"));
                user.setPassword(resultSet.getString("password"));
                String hashedPassword = user.getPassword();
                if (BCrypt.checkpw(plainTextPassword, hashedPassword)) {
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
