package pl.raporty.daos;

import pl.raporty.DBUtil.DbUtil;
import pl.raporty.models.Car;
import pl.raporty.models.Report;

import javax.ws.rs.NotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDao {

    private static final String CREATE_REPORTS_QUERY =
            "INSERT INTO reports (car_id, data, fuel_litters, price_per_litter) VALUES (?, ?, ?, ?)";
    private static final String READ_REPORTS_QUERY =
            "SELECT * FROM reports WHERE report_id = ?";
    private static final String UPDATE_REPORTS_QUERY =
            "UPDATE reports SET car_id = ?, data = ?, fuel_litter = ?, price_per_litter = ? WHERE report_id = ?";
    private static final String DELETE_REPORTS_QUERY =
            "DELETE FROM reports WHERE report_id = ?";

    public Report create (Report report) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(CREATE_REPORTS_QUERY,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            setReportsParams(report, statement);
            int result = statement.executeUpdate();

            if (result != 1) {
                throw new RuntimeException("Execute update returned " + result);
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.first()) {
                    report.setId(generatedKeys.getInt(1));
                    return report;
                } else {
                    throw new RuntimeException("Generated kay was now found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Integer reportId) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_REPORTS_QUERY)) {
            statement.setInt(1, reportId);
            statement.executeUpdate();

            boolean deleted = statement.execute();
            if (!deleted) {
                throw new NotFoundException("Report not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update (Report report) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(UPDATE_REPORTS_QUERY)) {
            statement.setInt(5, report.getId());
            setReportsParams(report, statement);

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Report read(Integer reportId) {
        Report report = new Report();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_REPORTS_QUERY);
        ) {
            statement.setInt(1, reportId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    report.setId(resultSet.getInt("report_id"));
                    report.setCarId(resultSet.getInt("car_id"));
                    report.setData(resultSet.getString("data"));
                    report.setFuelLitters(resultSet.getInt("fuel_litters"));
                    report.setPricePerLitter(resultSet.getInt("price_per_litter"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return report;
    }

    private void setReportsParams(Report report, PreparedStatement statement) throws SQLException {
        statement.setInt(1, report.getCarId());
        statement.setString(2, report.getData());
        statement.setDouble(3, report.getFuelLitters());
        statement.setDouble(4, report.getPricePerLitter());
    }

}
