package bank;

import bank.exceptions.AmountException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
  private int id;
  private String type;
  private double balance;

  public void deposit(double amount)throws AmountException{
    if(amount < 1){
      throw new AmountException("The minimum deposit is 1.00");
    }
    else{
      setBalance(getBalance() + amount);
      DataSource.updateAccountBalance(id, getBalance());
    }
  }

  public void withdraw(double amount) throws AmountException{
    if(amount < 0){
      throw new AmountException("the withdrawal amount must be greater than 0.");
    }
    else if(amount > getBalance()){
      throw new AmountException("You do not have sufficient funds for this withdrawal.");
    }
    else {
      setBalance(getBalance() - amount);
      DataSource.updateAccountBalance(id, getBalance());
    }
  }

}
