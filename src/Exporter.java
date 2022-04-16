import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Exporter {

    public Exporter() {
        
    }

    // Export the userbase csv file
    //
    public void exportUserbase(HashMap<String, String> userbase) throws IOException{
        PrintWriter outputStream = new PrintWriter(new FileWriter("userbase.csv"));
        outputStream.println("Username, Password");
        
        // for each key, value pair in our hashmap, output the key as the username and get the corresponding password
        for (String username : userbase.keySet()) 
        { 
            outputStream.println(username + ", " + userbase.get(username));
        }
        
        outputStream.close();
    }

    // Export manager
    //
    public void exportManager(Manager manager) throws IOException {
        exportManagerDetails(System.getProperty("user.dir") + "\\data\\users\\" + manager.getName() + ".csv", manager); // export manager details
        exportManagerClients(System.getProperty("user.dir") + "\\data\\managers\\" + manager.getName() + ".csv", manager); // export manager clients details
    };

    // Export manager details to csv
    //
    public void exportManagerDetails(String path, Manager manager) throws IOException{
        PrintWriter outputStream = new PrintWriter(new FileWriter(path));
        outputStream.println("Username, Password, AccountType");
        outputStream.println(manager.getName() + ", " + manager.getPass() + ", " + manager.getAccountType());
        outputStream.close();
    }

    // Export manager clients to csv
    //
    public void exportManagerClients(String path, Manager manager) throws IOException{
        PrintWriter outputStream = new PrintWriter(new FileWriter(path));
        outputStream.println("Username, Password");  

        // if manager is assigned clients
        if (manager.getClients() != null) {
            // export client name and password
            for (Client c : manager.getClients()) 
            { 
                outputStream.println(c.getName() + ", " + c.getPass());
            }
        }
        outputStream.close();
    }

    // Export a client
    //
    public void exportClient(Client client) throws IOException {
        exportClientDetails(System.getProperty("user.dir") + "\\data\\users\\" + client.getName() + ".csv",client); // export client details
        exportTransactions(System.getProperty("user.dir") + "\\data\\portfolio\\" + client.getName() + ".csv", client.getPortfolio()); // export client portfolio
        exportTransactions(System.getProperty("user.dir") + "\\data\\transactions\\" + client.getName() + ".csv", client.getHistory()); // export client transaction history
    }

    // Export client details to csv
    //
    public void exportClientDetails(String path, Client client) throws IOException{
        PrintWriter outputStream = new PrintWriter(new FileWriter(path));
        outputStream.println("Username, Password, AccountType, Balance");
        outputStream.println(client.getName() + ", " + client.getPass() + ", " + client.getAccountType() + ", " + client.getFunds());
        outputStream.close();
    }

    // Export client transaction history
    //
    public void exportTransactions(String path, ArrayList<Transaction> transactions) throws IOException{
        PrintWriter outputStream = new PrintWriter(new FileWriter(path));
        outputStream.println("OpenDate, CloseDate, Ticker, Type, Status, Amount, Cost, Value, Growth");
        
        // for each transaction, export all attributes to csv file
        for (Transaction t : transactions) 
        { 
            outputStream.println(t.getOpenDate() + ", " + t.getCloseDate() + ", " + t.getTicker() + ", " + t.getType() + ", " + t.getStatus() + ", " + t.getAmount() + ", " + t.getCost() + ", " + t.getPositionValue() + ", " + t.getGrowth());
        }
        
        outputStream.close();
    }
}
