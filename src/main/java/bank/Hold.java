package bank;
import java.util.Scanner;

public class Hold {
  public Hold(){
    System.out.println("Created");
}
  public void execute(){
    String x = "";
    int count = 0;
    Scanner scan = new Scanner(System.in);
    while (!x.equals("done")) {
      System.out.println(count);
      count++;
      if(count == 1000){
        System.out.print("\033[H\033[2J");
        System.out.flush();
      }

      if(count == 2000000000){
        count = 0;
      }
    }

    scan.close();
  }
}
