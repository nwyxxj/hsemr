/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.State;
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
public class StateDAO {

  
      public static State retrieve(String stateID, String scenarioID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        State state = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("select * from state where stateID = ? and scenarioID =?");
            stmt.setString(1, stateID);
            stmt.setString(2, scenarioID);

            rs = stmt.executeQuery();
            while (rs.next()) {
                state = new State(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return state;
    }

    public static State retrieveStateInScenario(String scenarioID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        State state = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("select * from state where scenarioID =?");
            stmt.setString(1, scenarioID);

            rs = stmt.executeQuery();
            while (rs.next()) {
                state = new State(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return state;
    }

    public static void add(String stateID, String scenarioID, String stateDescription, String patientNRIC) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO state (stateID, scenarioID, stateDescription, patientNRIC) VALUES (?,?,?,?)";

        try {
            conn = ConnectionManager.getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, stateID);
            preparedStatement.setString(2, scenarioID);
            preparedStatement.setString(3, stateDescription);
            preparedStatement.setString(4, patientNRIC);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, preparedStatement, null);
        }
    }

    public static List<State> retrieveAll(String scenarioID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<State> stateList = new ArrayList<State>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM state where scenarioID = ?");
            stmt.setString(1, scenarioID);
            rs = stmt.executeQuery();

            while (rs.next()) {
                State state = new State(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                stateList.add(state);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return stateList;
    }
 
    public static void updateNRICForState0(String scenarioID, String patientNRIC) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String query = "UPDATE state SET patientNRIC =? WHERE scenarioID =? AND patientNRIC=? AND stateID=?";

        try {
            conn = ConnectionManager.getConnection();

            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, patientNRIC);
            preparedStatement.setString(2, scenarioID);
            preparedStatement.setString(3, "-");
            preparedStatement.setString(4, "ST0");
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
        String query = "DELETE FROM state WHERE scenarioID =?";

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
    public static void updateStateDesc(String stateID, String stateDescription, String scenarioID) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String query = "UPDATE state SET stateDescription=? WHERE stateID=? AND scenarioID=?";

        try {
            conn = ConnectionManager.getConnection();

            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, stateDescription);
            preparedStatement.setString(2, stateID);
            preparedStatement.setString(3, scenarioID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, preparedStatement, null);
        }
    }
    public static void updateNRIC(String patientNRIC, String retrieveNRIC) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String query = "UPDATE state SET patientNRIC=? WHERE patientNRIC=?";

        try {
            conn = ConnectionManager.getConnection();

            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, patientNRIC);
            preparedStatement.setString(2, retrieveNRIC);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, preparedStatement, null);
        }
    }
}
