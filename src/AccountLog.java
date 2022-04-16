import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

public class AccountLog {

    private ArrayList<Transaction> transactions;

    public AccountLog() {
        this.transactions = new ArrayList<Transaction>();
    }

    public void logTransaction(Transaction t) {
        this.transactions.add(t);
    }

    public void removeTransaction(int index) {
        this.transactions.remove(index);
    }

    public Transaction getTransactionByIndex(int index) {
        return this.transactions.get(index);
    }

    // public void findTransactionByDate(LocalDate date) {
    //     Iterator<Transaction> it = transactions.iterator();
	
    //     while (it.hasNext()){
    //         Transaction t = it.next();

    //         if (t.getOpenDate().equals(date)){
    //             System.out.println();
    //         }
    //     }
    // }

    public void findTransactionBySymbol(LocalDate date) {
        // Generic 
    }
    
    // public boolean checkNull() {
    //     if (transactions.isEmpty()) {
    //         return true;
    //     }
    //     else {
    //         return false;
    //     }
    // }

    // Print all the transactions that have been logged
    //
    public void printLog() {
        Iterator<Transaction> it = transactions.iterator();
        int count = 1;

        while (it.hasNext()){
            Transaction t = it.next();

            // if transaction is not closed, do not retrieve transactions closeDate as it is null
            if (!t.getStatus().equals("closed")) {
                t.updateOpenTransaction();
                System.out.println(count + ". " + t.getOpenDate() + ", " + "----------, " + t.getTicker() + ", " + t.getType() + ", " + t.getStatus() + ", " + t.getAmount() + ", " + t.getCost() + ", " + t.getGrowth());
            }
            // if transaction is closed, print transaction closeDate
            else {
                System.out.println(count + ". " + t.getOpenDate() + ", " + t.getCloseDate() + ", " + t.getTicker() + ", " + t.getType() + ", " + t.getStatus() + ", " + t.getAmount() + ", " + t.getCost() + ", " + t.getGrowth());
            }

            count++;
        }
    }


    // GETTER AND SETTER METHODS

    public int getSize() {
        return transactions.size();
    }

    public ArrayList<Transaction> getLog() {
        return this.transactions;
    }

}
