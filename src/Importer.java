import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Importer {
    // placeholder for defaul constructor method
    //
    public Importer() {

    }

    // Import userbase from csv 
    //
    public HashMap<String, String> importUserbase() throws IOException{
        BufferedReader inputStream = new BufferedReader(new FileReader("userbase.csv"));
        
        // create hashmap to map user:pass combos
        HashMap<String, String> users = new HashMap<String, String>();
        
        String line;
        inputStream.readLine();
        while ((line = inputStream.readLine()) != null)
        {
            // add new combo to userbase
            String[] values = line.split(", ");
            users.put(values[0], values[1]);
        }
        
        inputStream.close();
        return users;
    }


    // Import manager 
    //
    public Manager importManager(String name, String password) throws IOException, ManagerNotFoundException {
        Manager manager = new Manager(name, password, "manager");

        try {
            // import manager clients from csv and set clients araylist for manager
            manager.setClients(importClients(System.getProperty("user.dir") + "\\data\\managers\\" + name + ".csv"));
        }
        catch (Exception e) {
            // should not execute unless required csv has been manually deleted or moved
            System.err.println(e);
            throw new ManagerNotFoundException("Unable to import manager");
        }

        return manager;
    };

    // Import manager clients 
    //
    public ArrayList<Client> importClients(String path) throws IOException, ClientNotFoundException {
        // System.out.println(path);
        BufferedReader inputStream = new BufferedReader(new FileReader(path));
        
        ArrayList<Client> clients = new ArrayList<Client>();

        String line;
        inputStream.readLine();
        // import each client listed under managers csv
        while ((line = inputStream.readLine()) != null) 
        {
            String[] values = line.split(", ");
            Client client = importClient(values[0], values[1]);
            // add client to clients array list
            clients.add(client);
        }

        inputStream.close();
        return clients;
    }

    // Import client
    //
    public Client importClient(String name, String password) throws ClientNotFoundException {
        Client client = new Client(name, password, "client");
        try {
            client.setFunds(importClientBalance(System.getProperty("user.dir") + "\\data\\users\\" + name + ".csv")); // import client balance
            client.setPortfolioPositions(importTransactions(System.getProperty("user.dir") + "\\data\\portfolio\\" + name + ".csv")); // import client portfolio
            client.setHistory(importTransactions(System.getProperty("user.dir") + "\\data\\transactions\\" + name + ".csv")); // import client transaction history
        }
        catch (Exception e) {
            throw new ClientNotFoundException("Unable to import client"); // throw error if csv files are inaccessible
        }

        return client;
    }

    // Import client balance
    //
    public double importClientBalance(String path) throws IOException{
        BufferedReader inputStream = new BufferedReader(new FileReader(path));
        double balance = 0;

        inputStream.readLine();
        String line = inputStream.readLine();
        String[] values = line.split(", ");
        // index 3 of file holds client balance
        balance = Double.parseDouble(values[3]);

        inputStream.close();
        return balance;
    }

    // Import client transactions
    //
    public AccountLog importTransactions(String path) throws IOException{
        BufferedReader inputStream = new BufferedReader(new FileReader(path));
        
        AccountLog transactions = new AccountLog();

        String line;
        inputStream.readLine();
        while ((line = inputStream.readLine()) != null)
        {
            String[] values = line.split(", ");
            // create a new transaction and populate all attributes
            Transaction t = new Transaction(values[2], values[3], Double.parseDouble(values[5]), Double.parseDouble(values[6]), parseDateString(values[0]), Double.parseDouble(values[8]), Double.parseDouble(values[7]), values[4], parseDateString(values[1]));
            // add transaction to account log
            transactions.logTransaction(t);
        }

        inputStream.close();
        return transactions;
    }

    // Import user type
    //
    public String importUserType(String name, String password) throws IOException{
        BufferedReader inputStream = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\data\\users\\" + name + ".csv"));

        inputStream.readLine();
        String line = inputStream.readLine();
        String[] values = line.split(", ");
        inputStream.close();

        // accountType is at index 2
        return values[2];
    }

    // Parse date string into LocalDate object
    //
    public LocalDate parseDateString(String date) {

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            return parsedDate;
        }
        catch (Exception exception) {
            return null;
        }


    }
}
