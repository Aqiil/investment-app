import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Client extends User{
        
    private Account account;
    
    public Client(String name, String password, String accountType){
        super(name, password, accountType);
        this.account = new Account();
    }

    public void deposit(double amount){
        this.account.deposit(amount);
    }

    // Delete client account
    //
    public void deleteUser(Scanner scanner, HashMap<String, String> userbase) {
        try{
            super.deleteUser(scanner); // extend deleteUser method from parent class (confirms account deletion)
        }
        catch (UserException u) {
            return;
        }

        deleteFile(System.getProperty("user.dir") + "\\data\\users\\" + this.getName() + ".csv"); // delete client details
        deleteFile(System.getProperty("user.dir") + "\\data\\portfolio\\" + this.getName() + ".csv"); // delete client portfolio
        deleteFile(System.getProperty("user.dir") + "\\data\\transactions\\" + this.getName() + ".csv"); // delete client transaction history
        removeFromUserbase(userbase);
        JOptionPane.showMessageDialog(null,"Client has been deleted successfully","Account Deleted", JOptionPane.WARNING_MESSAGE); // yellow icon
    }

    // Withdraw from account
    //
    public void withdraw(double amount) throws InsufficientFundsException{
        // if account has enough money, withdraw
        if (amount <= this.account.getBalance()) {
            this.account.withdraw(amount);
        }
        // if account does not have enough money, output insufficient funds
        else {
            throw new InsufficientFundsException("Account has insufficient funds");
        }
    }

    public void placeOrder(FinancialInstrument security, String type, double amount, double cost) {
        account.placeOrder(security, type, amount, cost);;
    }
    
    // Close order and output profit made on position
    //
    public void closeOrder(int index) {
        double profit = account.closeOrder(index);
        System.out.println("Order closed with profit: " + profit + "\n");
    }

    public void printTransactionHistory() {
        account.printTransactionHistory();
    }

    public void printPortfolio() {
        account.printPortfolio();
    }

    public int getNoOpenPositions() {
        return account.portfolioSize();
    }

    // GETTER AND SETTER METHODS

    public ArrayList<Transaction> getHistory() {
        return this.account.getTransactionHistory();
    }

    public void setHistory(AccountLog transactions) {
        this.account.setTransactionHistory(transactions);
    }

    public ArrayList<Transaction> getPortfolio() {
        return this.account.getPortfolioPositions();
    }

    public void setPortfolioPositions(AccountLog portfolio) {
        this.account.setPortfolioPositions(portfolio);;
    }

    public double getFunds(){
        return this.account.getBalance();
    }

    public void setFunds(double balance) {
        this.account.setBalance(balance);
    }

}
