import java.time.LocalDate;
import java.util.ArrayList;

public class Account{
    private double balance;
    private AccountLog transactions;
    private AccountLog portfolio;
    
    public Account(){
        this.balance=0;
        this.transactions = new AccountLog();
        this.portfolio = new AccountLog();
    }    

    public Account(double balance){
        this.balance = roundDouble(balance);
        this.transactions = new AccountLog();
        this.portfolio = new AccountLog();
    }

    public void deposit(double amount){
        this.balance += roundDouble(amount);
    }    

    public void withdraw(double amount){
        this.balance -= roundDouble(amount);
    }

    // Rounds doubles to 2 decimal places
    //
    public double roundDouble(double amount) {
        return Math.round(amount*100.0)/100.0;
    } // END roundDouble

    // Place a new order, log it under transactions and add to client portfolio
    //
    public void placeOrder(FinancialInstrument security, String type, double amount, double cost) {
        Transaction order = new Transaction(security.getSymbol(), type, roundDouble(amount), roundDouble(cost), security.getDate());
        portfolio.logTransaction(order);
        transactions.logTransaction(order);
    }

    // Close an order and log it under transaction history
    //
    public double closeOrder(int index) {
        // update value before closing position
        Transaction initialOrder = portfolio.getTransactionByIndex(index);
        initialOrder.updateOpenTransaction();

        // calculate the profit of the position and update the balance of the account accordingly
        double profit = initialOrder.getPositionValue();
        this.balance += initialOrder.getCost() + profit;

        // create a new transaction for the closed order and log it under transaction history
        Transaction closedOrder = new Transaction(initialOrder.getTicker(), initialOrder.getType(), initialOrder.getAmount(), initialOrder.getCost(), initialOrder.getOpenDate(), initialOrder.getGrowth(), initialOrder.getPositionValue(), "closed", LocalDate.now());
        this.transactions.logTransaction(closedOrder);

        // remove the transaction from the account portfolio
        this.portfolio.removeTransaction(index);
        return profit;
    }

    // Print the account transaction history
    //
    public void printTransactionHistory() {
        if (this.transactions.getSize() <= 0) {
            System.out.println("Account history unavailable\n");
        }
        else {
            System.out.println("--- TRANSACTION HISTORY ---");
            System.out.println("OpenDate, CloseDate, Ticker, Type, Status, Amount, Cost, Value, Growth");
            this.transactions.printLog();
            System.out.println();
        }
    }

    // Print the account portfolio
    //
    public void printPortfolio() {
        if (this.portfolio.getSize() <= 0) {
            System.out.println("No positions open\n");
        }
        else {
            System.out.println("--- PORTFOLIO ---");
            this.portfolio.printLog();
            System.out.println();
        }
    }


    public int portfolioSize() {
        return portfolio.getSize();
    }

    // GETTER AND SETTER METHODS
    
    public double getBalance() {
        return roundDouble(this.balance);
    }

    public void setBalance(double amount) {
        this.balance = roundDouble(amount);
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return this.transactions.getLog();
    }

    public void setTransactionHistory(AccountLog transactions) {
        this.transactions = transactions;
    }

    public ArrayList<Transaction> getPortfolioPositions() {
        return this.portfolio.getLog();
    }

    public void setPortfolioPositions(AccountLog portfolio) {
        this.portfolio = portfolio;
    }
}