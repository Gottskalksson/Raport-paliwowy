package pl.raporty.daos;

import org.mindrot.jbcrypt.BCrypt;
import pl.raporty.DBUtil.DbUtil;
import pl.raporty.models.User;

import javax.ws.rs.NotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            changeDb(conn);
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
            
            ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    user.setId(resultSet.getInt(1));
                }
                return user;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(Integer userId) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_QUERY)) {
            changeDb(connection);
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
            changeDb(conn);
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
            changeDb(connection);
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

    public User authorizeUser(String name, String plainTextPassword) {
        User user = new User();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_IF_USER_EXISTS)) {
            changeDb(connection);
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

    private void changeDb(Connection conn) throws SQLException {
        PreparedStatement changeDb = conn.prepareStatement("USE petrol");
        ResultSet setDb = changeDb.executeQuery();
    }


}
