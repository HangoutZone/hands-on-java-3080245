package bank;

import java.util.Scanner;
import java.util.Random;

import javax.security.auth.login.LoginException;

import bank.exceptions.AmountException;
import bank.exceptions.UserExistException;

public class Menu {

  private Scanner scanner;
  private Scanner scanText;

  public static void main(String[] args) {
    System.out.println("Welcome to Globe Bank International!");

    Menu menu = new Menu();
    menu.scanner = new Scanner(System.in);
    menu.scanText = new Scanner(System.in);

    System.out.println("Press 1 to create a user or another input to login");
    if (menu.scanner.nextInt() == 1) {
      menu.createCustomer();
    }

    System.out.println("-----------Login------------");
    Customer customer = menu.authenticateUser();

    if (customer != null) {
      Account account = DataSource.getAccount(customer.getAccountId());
      menu.showMenu(customer, account);
    }

    menu.scanner.close();
  }

  private Customer authenticateUser() {
    System.out.println("Please enter your username");
    String username = scanner.next();

    System.out.println("Please enter your password");
    String password = scanner.next();

    Customer customer = null;
    try {
      customer = Authenticator.login(username, password);
    } catch (LoginException e) {
      System.out.println("There was an error: " + e.getMessage());
    }

    return customer;
  }

  private void showMenu(Customer customer, Account account) {
    int selection = 0;

    while (selection != 4 && customer.isAuthenticated()) {
      System.out.println("================================================");
      System.out.println("Please select one of the following options: ");
      System.out.println("1: Deposit");
      System.out.println("2: Withdraw");
      System.out.println("3: Check Balance");
      System.out.println("4: Exit");
      System.out.println("================================================");

      selection = scanner.nextInt();
      double amount = 0;

      switch (selection) {
        case 1:
          System.out.println("How much would you like to deposit?");
          amount = scanner.nextDouble();
          try {
            account.deposit(amount);
          } catch (AmountException amtEx) {
            System.out.println("amount is not valid. Amount to deposit has to be greater than 0");
          }
          break;

        case 2:
          System.out.println("How much would you like to withdraw?");
          amount = scanner.nextDouble();
          try {
            account.withdraw(amount);
          } catch (AmountException e) {
            System.out.println(e.getMessage() + "\nPlease try again");
          }
          break;

        case 3:
          System.out.println("Current balance: " + account.getBalance());
          break;

        case 4:
          Authenticator.logout(customer);
          System.out.println("Thanks for banking at Globe Bank International!");
          break;

        default:
          System.out.println("Invalid option. Please try again");
          break;
      }
    }
  }

  private void createCustomer() {
    System.out.println("Enter your name.");
    String name = scanText.nextLine();
    System.out.println("Enter your username.");
    String username = scanText.nextLine();
    System.out.println("Enter your password.");
    String password = scanText.nextLine();

    Random r = new Random();
    int maxC = 9999;
    int minC = 1000;
    int maxA = 99999;
    int minA = 10000;
    int v1 = r.nextInt(maxC - minC + 1) + minC;
    int v2 = r.nextInt(maxA - minA + 1) + minA;

    Customer c = new Customer(v1, name, username, password, v2);
    try {
      DataSource.createCustomer(c);
    } catch (UserExistException u) {
      u.printStackTrace();
      System.out.println("Bye");
      System.exit(0);
    }
  }
}