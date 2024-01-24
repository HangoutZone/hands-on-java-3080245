package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bank.exceptions.AmountException;
import bank.exceptions.UserExistException;

public class DataSource {

  public static Connection connect() {
    String db_file = "jdbc:sqlite:resources/bank.db";
    Connection connection = null;
    try {
      connection = DriverManager.getConnection(db_file);
      // System.out.println("Connected!!!");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connection;
  }

  public static Customer getCustomer(String username) {
    String sql = "select * from customers where username = ?";
    Customer customer = null;
    try (Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);
      try (ResultSet resultSet = statement.executeQuery()) {
        customer = new Customer(
            resultSet.getInt("id"),
            resultSet.getString("name"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getInt("account_id"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return customer;
  }

  public static Account getAccount(int id) {
    String sql = "select * from accounts where id = ?";
    Account account = null;
    try (Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        account = new Account(
            resultSet.getInt("id"),
            resultSet.getString("type"),
            resultSet.getDouble("balance"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return account;
  }

  public static void updateAccountBalance(int id, double amount) {
    String sql = "update accounts set balance = ? where id = ?";
    try (
        Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(sql);) {
      statement.setDouble(1, amount);
      statement.setInt(2, id);

      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void createCustomer(Customer c) throws UserExistException {
    // if (validate(c.getUsername()))
    //   throw new UserExistException("This user already exist");

    String sql = "insert into customers (id, name, username, password, account_id) values (?,?,?,?,?)";
    try (
        Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(sql);) {
      statement.setInt(1, c.getId());
      statement.setString(2, c.getName());
      statement.setString(3, c.getUsername());
      statement.setString(4, c.getPassword());
      statement.setInt(5, c.getAccountId());

      statement.executeUpdate();

      createAccount(c);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void createAccount(Customer c) throws SQLException {
    String sql = "insert into accounts (id, type, balance) values (?,?,?)";
    Connection connection = connect();
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, c.getAccountId());
    statement.setString(2, "Checking");
    statement.setInt(3, 0);

    statement.executeUpdate();
  }

  private static boolean validate(String str) {
    String sql = "select username from customers";
    try (Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(sql);) {
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          if (resultSet.getString(1).equals(str))
          System.out.println("same username.....@");
            return true;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static void main(String[] args) {
    Customer c = getCustomer("lfromonte9@de.vu");
    Account a = getAccount(c.getAccountId());
    System.out.println("Account ID: " + a.getId() + "\nBalance: " + a.getBalance());
    try {
      a.deposit(100);
    } catch (AmountException e) {
      System.out.println("why?");
    }
    updateAccountBalance(c.getId(), a.getBalance());
    System.out.println("\nBalance: " + a.getBalance());
  }
}
