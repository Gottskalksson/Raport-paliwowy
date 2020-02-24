package pl.raporty.daos;

import pl.raporty.DBUtil.DbUtil;
import pl.raporty.models.Car;

import javax.ws.rs.NotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarDao {
    private static final String CREATE_CAR_QUERY =
            "INSERT INTO cars (car_number, mark, model, fuel_type, year_production, owner_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String READ_CAR_QUERY =
            "SELECT * FROM users WHERE car_id = ?";
    private static final String UPDATE_CAR_QUERY =
            "UPDATE users SET car_number = ?, mark = ?, model = ?, fuel_type = ?, year_production = ?, owner_id = ? WHERE car_id = ?";
    private static final String DELETE_CAR_QUERY =
            "DELETE FROM users WHERE car_id = ?";

    public Car create (Car car) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(CREATE_CAR_QUERY,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            setCarsParams(car, statement);
            int result = statement.executeUpdate();

            if (result != 1) {
                throw new RuntimeException("Execute update returned " + result);
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.first()) {
                    car.setId(generatedKeys.getInt(1));
                    return car;
                } else {
                    throw new RuntimeException("Generated kay was now found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Integer carId) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CAR_QUERY)) {
            statement.setInt(1, carId);
            statement.executeUpdate();

            boolean deleted = statement.execute();
            if (!deleted) {
                throw new NotFoundException("Car not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update (Car car) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(UPDATE_CAR_QUERY)) {
            statement.setInt(7, car.getId());
            setCarsParams(car, statement);

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Car read(Integer carId) {
        Car car = new Car();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_CAR_QUERY);
        ) {
            statement.setInt(1, carId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    car.setId(resultSet.getInt("car_id"));
                    car.setCarNumber(resultSet.getString("car_number"));
                    car.setMark(resultSet.getString("mark"));
                    car.setModel(resultSet.getString("model"));
                    car.setFuelType(resultSet.getString("fuel_type"));
                    car.setYearProduction(resultSet.getInt("year_production"));
                    car.setOwnerId(resultSet.getInt("owner_id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return car;
    }

    private void setCarsParams(Car car, PreparedStatement statement) throws SQLException {
        statement.setString(1, car.getCarNumber());
        statement.setString(2, car.getMark());
        statement.setString(3, car.getModel());
        statement.setString(4, car.getFuelType());
        statement.setInt(5, car.getYearProduction());
        statement.setInt(6, car.getOwnerId());
    }

}
