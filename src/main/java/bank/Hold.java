package bank;
import java.util.Scanner;

public class Hold {
  public Hold(){
    System.out.println("Created");
}
  public void execute(){
    String x = "";
    Scanner scan = new Scanner(System.in);
    while (!x.equals("done")) {
      x = scan.nextLine();
    }

    scan.close();
  }
}
