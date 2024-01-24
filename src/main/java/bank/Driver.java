package bank;

import java.util.Random;

public class Driver {
  public static void main(String[] lul) {
    Random r = new Random();
    int max = 99999;
    int min = 10000;
    for (int i = 0; i < 1000; i++) {
      int v = r.nextInt(max - min + 1) + min;
      System.out.println(v);
    }
  }
}
