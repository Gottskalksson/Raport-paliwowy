package pl.raporty.daos;

public class UserDao {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users (user_name, password) VALUES (?, ?)";
    private static final String READ_USER_QUERY =
            "SELECT * FROM users WHERE user_id = ?";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET user_name = ?, password = ? WHERE user_id = ?";
    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE user_id = ?";
}
