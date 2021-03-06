/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Scenario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ScenarioDAO {
    
      public static Scenario retrieveScenarioActivatedByLecturer(String lecturerID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Scenario scenarioByLecturer = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM scenario inner join lecturer_scenario "
                    + "ON scenario.scenarioID=lecturer_scenario.scenarioID where lecturer_scenario.lecturerID=?");
             stmt.setString(1, lecturerID);
            rs = stmt.executeQuery();

            while (rs.next()) {
               scenarioByLecturer = new Scenario(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return scenarioByLecturer;
    }
    

    public static Scenario retrieve(String scenarioID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Scenario scenario = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("select * from scenario where scenarioID = ?");
            stmt.setString(1, scenarioID);

            rs = stmt.executeQuery();
            while (rs.next()) {
                scenario = new Scenario(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return scenario;
    }
    
    public static Scenario retrieveByScenarioName(String scenarioName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Scenario scenario = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("select * from scenario where scenarioName = ?");
            stmt.setString(1, scenarioName);

            rs = stmt.executeQuery();
            while (rs.next()) {
                scenario = new Scenario(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return scenario;
    }
    
    public static void add(String scenarioID, String scenarioName, String scenarioDescription, String admissionNote, int bedNumber) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO scenario (scenarioID, scenarioName, scenarioDescription, admissionNote, bedNumber) VALUES (?,?,?,?,?)";

        try {
            conn = ConnectionManager.getConnection();
            preparedStatement = conn.prepareStatement(query);
            
            preparedStatement.setString(1, scenarioID);
            preparedStatement.setString(2, scenarioName);
            preparedStatement.setString(3, scenarioDescription);
            preparedStatement.setString(4, admissionNote);
            preparedStatement.setInt(5, bedNumber);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, preparedStatement, null);
        }
    }
    
    public static Integer retrieveMaxBedNumber() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int maxBed = 0;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("select max(bedNumber) as maxBed from scenario");

            rs = stmt.executeQuery();
            while (rs.next()) {
                maxBed = rs.getInt("maxBed");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return maxBed;
    }
    
    
    public static List<Scenario> retrieveAll() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Scenario> scenarioList = new ArrayList<Scenario>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM scenario order by bedNumber asc");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Scenario newScenario = new Scenario(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
                scenarioList.add(newScenario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return scenarioList;
    }

    public static void update(String scenarioID, String scenarioName, String scenarioDescription, int scenarioStatus, String admissionNote) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String query = "UPDATE scenario SET  scenarioName=?, scenarioDescription=?, scenarioStatus =?, admissionNote =?  WHERE scenarioID =?";

        try {
            conn = ConnectionManager.getConnection();

            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, scenarioName);
            preparedStatement.setString(2, scenarioDescription);
            preparedStatement.setInt(3, scenarioStatus);
            preparedStatement.setString(4, admissionNote);
            preparedStatement.setString(5, scenarioID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, preparedStatement, null);
        }

    }

    public static void delete(String scenarioID) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String query = "DELETE FROM scenario WHERE scenarioID =?;";

        try {
            conn = ConnectionManager.getConnection();

            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, scenarioID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, preparedStatement, null);
        }
    }
    public static List<Scenario> retrieveAndSortByBedNum() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Scenario> scenarioList = new ArrayList<Scenario>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM scenario ORDER BY bedNumber ASC");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Scenario newScenario = new Scenario(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
                scenarioList.add(newScenario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return scenarioList;
    }
   
    public static void update(String scenarioID, String scenarioName, String scenarioDescription, String admissionNote) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String query = "UPDATE scenario SET  scenarioName=?, scenarioDescription=?, admissionNote =?  WHERE scenarioID =?";

        try {
            conn = ConnectionManager.getConnection();

            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, scenarioName);
            preparedStatement.setString(2, scenarioDescription);
            preparedStatement.setString(3, admissionNote);
            preparedStatement.setString(4, scenarioID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, preparedStatement, null);
        }

    }

}
