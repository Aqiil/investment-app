import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in); // scanner object for user io
        HashMap<String, String> userbase = importUserbase(); // hashmap to store login combos for users
        menu(scanner, userbase); // application menu

    }

    // Application menu
    //
    public static void menu(Scanner scanner, HashMap<String, String>userbase) {
        int response = -1;

        while (!(response == 1 || response == 2 || response == 0))
        {
            System.out.println("***** INVESTMENT APP *****\n"
                            + "[1] Login \n"
                            + "[2] Register\n"
                            + "[0] Quit");
            System.out.print("Choice: ");
            response = scanner.nextInt();
            scanner.nextLine();
            System.out.println();

            if (response == 1) {
                login(scanner, userbase);
                menu(scanner, userbase);
            } else if (response == 2) {
                registerUser(scanner, userbase);
                menu(scanner, userbase);
            // update userbase and quit application
            } else if (response == 0) {
                try {
                    exportUserbase(userbase);
                }
                catch (Exception e) {
                    System.out.println();
                }
                quit(scanner);
            }
        }
    } // END menu

    // Login interface for all users
    //
    public static void login(Scanner scanner, HashMap<String, String>userbase) {
        System.out.print("Username: ");
        String username = scanner.nextLine(); 

        String password = "";
        boolean found = false;

        // check user exists
        for (String i : userbase.keySet()) {
            if (username.equals(i)) {
                password = userbase.get(i);
                found = true;
            }
        }

        if (found == true) {
            System.out.print("Password: ");
            String inputPass = scanner.nextLine();
            // check password for user is correct
            if (inputPass.equals(password)) {
                try {
                    Importer importer = new Importer();
                    // import user type to determine if user is manager or client
                    String userType = importer.importUserType(username, password);
                    // import manager and go to manager dashboard
                    if (userType.equals("manager")) {
                        Manager manager = importManager(username, password);
                        System.out.println("\nMANAGER LOGIN: " + manager.getName() + "\n");
                        managerDashboard(scanner, manager, userbase);
                    }
                    // import client and go to client dashboard
                    else {
                        Client client = importClient(username, password);
                        System.out.println("\nCLIENT LOGIN: " + client.getName() + "\n");
                        clientDashboard(scanner, userbase, client);
                    }
                }
                // Setup new manager if user has just registered
                catch (ManagerNotFoundException m) {
                    // System.err.println(m);
                    Manager manager = new Manager(username, password, "manager");
                    System.out.println("\nLOGGED IN: " + manager.getName() + "\n");
                    managerDashboard(scanner, manager, userbase);
                } 
                // Setup new client if user has just registered
                catch (ClientNotFoundException c) {
                    // System.err.println(c);
                    Client client = new Client(username, password, "client");
                    System.out.println("\nLOGGED IN: " + client.getName() + "\n");
                    clientDashboard(scanner, userbase, client);
                }
                // catch any issues with file import
                catch (FileNotFoundException f) {
                    System.out.println("Error importing user\n");
                    return;
                } 
                // catch any unexpected errors
                catch (Exception e) {
                    System.err.println(e);
                    System.out.println("Error Logging in\n");
                    return;
                }
            }
            else {
                System.out.println("Incorrect password\n");
            }
        }
        else {
            System.out.println("Unable to find user\n");
            return;
        }
    } // END login

    public static Manager importManager(String name, String password) throws IOException, ManagerNotFoundException {
        Importer importer = new Importer();
        Manager m = importer.importManager(name, password);
        return m;
    }

    public static void exportManager(Manager manager) throws IOException {
        Exporter exporter = new Exporter();
        exporter.exportManager(manager);
    }

    public static Client importClient(String name, String password) throws IOException, ClientNotFoundException{
        Importer importer = new Importer();
        Client c = importer.importClient(name, password);
        return c;
    }

    public static void exportClient(Client client) throws IOException{
        Exporter exporter = new Exporter();
        exporter.exportClient(client);
    }

    public static HashMap<String, String> importUserbase() throws IOException{
        Importer importer = new Importer();
        HashMap<String, String> userbase = importer.importUserbase();
        // System.out.println("DEBUG imported userbase");
        return userbase;
    }

    public static void exportUserbase(HashMap<String, String> userbase) throws IOException {
        Exporter exporter = new Exporter();
        exporter.exportUserbase(userbase);
    }

    public static void addUser(HashMap<String, String> userbase, String username, String password) {
        userbase.put(username, password);
    }

    // Registration interface
    //
    public static void registerUser(Scanner scanner, HashMap<String, String> userbase) {

        System.out.println("--- REGISTRATION ---");
        int response = -1;
        String accountType = "";
        while (!(response == 1 || response == 2 || response == 0))
        {
            System.out.println("[1] Register Client \n"
                            + "[2] Register Manager\n"
                            + "[0] Quit");
            System.out.print("Choice: ");
            response = scanner.nextInt();
            scanner.nextLine();
            System.out.println();

            if (response == 1) {
                accountType = "client";
            } else if (response == 2) {
                accountType = "manager";
            } else if (response == 0) {
                return;
            }

        }
        System.out.print("Username: ");
        String username = scanner.nextLine(); 

        // check if username is taken
        for (String i : userbase.keySet()) {
            if (username.equals(i)) {
                System.out.println("Username unavailable\n");
                return;
            }
        }

        String password = "";
        boolean valid = false;

        // check user password strength is strong
        while (!(valid)) {
            System.out.print("Password: ");
            password = scanner.nextLine();
            System.out.println();
            valid = checkValidPassword(password);
            if (!(valid)) {
                System.out.println("Password strength is weak.");
                System.out.println("Strong passwords must be 8 - 20 characters long with at least one uppercase letter and one number.");
            }
        }

        // setup manager account
        if (accountType.equals("manager")) {
            try {
                exportManager(new Manager(username, password, "manager"));
                System.out.println("Registered new manager!\n");
            }
            catch (Exception e) {
                System.out.println(e);
                System.out.println("Manager registration failed\n");
                return;
            }
        } 
        // setup client account
        else {
            try {
                exportClient(new Client(username, password, "client"));
                System.out.println("Registered new client!\n");
            }
            catch (Exception e) {
                System.out.println(e);
                System.out.println("Client registration failed\n");
                return;
            }
        }

        // add new user to userbase
        addUser(userbase, username, password);
        System.out.println("User registration successful!\n");

    } // END registerUser

    // Check password strength is strong
    //
    public static boolean checkValidPassword(String password) {
        // regular expression to evaluate password strength
        String regex = "^(?=.*[0-9])" // check for 1 or more numbers
                       + "(?=.*[a-z])(?=.*[A-Z])" // check for 1 or more uppercase and lowercase letters
                       + "(?=\\S+$).{8,20}$"; // check for password length of 8 - 20 chars
        Pattern pattern = Pattern.compile(regex);
        
        // check for user input
        if (password == null) {
            return false;
        }
        
        // check password strength against regex
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // Managers dashboard
    //
    public static void managerDashboard(Scanner scanner, Manager manager, HashMap<String, String> userbase) {
        int response = -1;
        
        while (!(response == 1 || response == 2 || response == 3 || response == 4 || response == 0))
        {
            System.out.println("***** DAHSBOARD *****\n"
                            + "[1] Manage client account\n" // List clients and choose which one to login to
                            + "[2] Add new client\n"
                            + "[3] Remove client\n"
                            + "[4] Delete account\n"
                            + "[0] Logout");
            System.out.print("Choice: ");
            response = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
        }
        
        if (response == 1) {
            manageClientAccount(scanner, manager, userbase);
            managerDashboard(scanner, manager, userbase);
        } else if (response == 2) {
            assignNewClient(scanner, manager, userbase);
            managerDashboard(scanner, manager, userbase);
        } else if (response == 3) {
            removeAssignedClient(scanner, manager);
            managerDashboard(scanner, manager, userbase);
        } else if (response == 4) {
            deleteManager(scanner, userbase, manager);
        // if user chooses to logout
        } else if (response == 0) {
            // export manager
            try {
                exportManager(manager);
            }
            // catch any unexpected errors
            catch (Exception e){
                System.err.println(e);
            }

            return;
        }
    } // END managerDashboard

    // Delete manager
    //
    public static void deleteManager(Scanner scanner, HashMap<String, String> userbase, Manager manager) {
        manager.deleteUser(scanner, userbase);
        // return to main menu
        menu(scanner, userbase);
    }

    // Manager a client account as a manager
    //
    public static void manageClientAccount(Scanner scanner, Manager manager, HashMap<String, String> userbase) {
        // print list of managers clients
        manager.printClientInfo();
        // check manager has clients assigned
        int noPositions = manager.getNumberOfClients();
        if (noPositions <= 0) {
            return;
        }
        int pos = -1;

        while (!((pos >= 0) && (pos <= noPositions + 1)))
        {
            System.out.println("---- MANAGE CLIENT ACCOUNT ----\n"
                            + "[+] Please choose client account to login to\n"
                            + "[+] 0 to return to dashboard");
            System.out.print("Position: ");
            pos = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
        }

        if (pos == 0) {
            return;
        }
        // read index of client and remove 
        else {
            Client client = manager.getManagerClientByIndex(pos-1); // subtract as clients are indexed from 0
            System.out.println("CLIENT LOGIN: " + client.getName() + "\n");
            // return to client dashboard
            clientDashboard(scanner, userbase, client);
        }
    } // END manageClientAccount

    // Assign a new client to a manager
    public static void assignNewClient(Scanner scanner, Manager manager, HashMap<String, String> userbase) {
        System.out.print("Username: ");
        String username = scanner.nextLine(); 

        String password = "";
        boolean found = false;

        // check client exists
        for (String i : userbase.keySet()) {
            if (username.equals(i)) {
                password = userbase.get(i);
                found = true;
            }
        }

        // check manager knows client credentials before assigning client
        if (found == true) {
            System.out.print("Password: ");
            String inputPass = scanner.nextLine();
            System.out.println();
            // check manager has input correct password
            if (inputPass.equals(password)) {
                try {
                    manager.addClient(importClient(username, password));
                }
                // catch any unexpected errors
                catch (Exception e) {
                    System.err.println(e);
                    System.out.println("Error assigning client");
                    return;
                }
            }
            else {
                System.out.println("Incorrect password\n");
            }
        }
        else {
            System.out.println("Unable to find client\n");
            return;
        }
    } // END assignNewClient


    // Remove an assigned client 
    //
    public static void removeAssignedClient(Scanner scanner, Manager manager) {
        manager.printClientInfo(); // list clients
        // select client to remove
        int noPositions = manager.getNumberOfClients();
        if (noPositions <= 0) {
            return;
        }
        int pos = -1;

        while (!((pos >= 0) && (pos <= noPositions + 1)))
        {
            System.out.println("---- REMOVE CLIENT ----\n"
                            + "[+] Please choose client to remove\n"
                            + "[+] 0 to return to dashboard");
            System.out.print("Position: ");
            pos = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
        }

        if (pos == 0) {
            return;
        }
        else {
            manager.removeClient(pos-1); // Client indexed from 0
        }
    } // END removeAssignedClient

    // Output the dashboard for a client
    //
    public static void clientDashboard(Scanner scanner, HashMap<String, String> userbase, Client client) {
        int response = -1;
        
        while (!(response == 1 || response == 2 || response == 3 || response == 4 || response == 5 || response == 6 || response == 7 || response == 0))
        {
            System.out.println("***** DASHBOARD *****\n"
                            + "[1] Search symbol\n"
                            + "[2] View portfolio\n"
                            + "[3] View balance\n"
                            + "[4] Deposit funds\n"
                            + "[5] Withdraw funds\n"
                            + "[6] View account history\n"
                            + "[7] Delect account\n"
                            + "[0] Logout");
            System.out.print("Choice: ");
            response = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
        }
        
        if (response == 1) {
            searchSymbol(scanner, client);
            clientDashboard(scanner, userbase, client);
        } else if (response == 2) {
            viewPortfolio(scanner, client);
            clientDashboard(scanner, userbase, client);
        } else if (response == 3) {
            viewBalance(client);
            clientDashboard(scanner, userbase, client);
        } else if (response == 4) {
            depositFunds(scanner, client);
            clientDashboard(scanner, userbase, client);
        } else if (response == 5) {
            withdrawFunds(scanner, client);
            clientDashboard(scanner, userbase, client);
        } else if (response == 6) {
            viewAccountHistory(scanner, client);
            clientDashboard(scanner, userbase, client);
        } else if (response == 7) {
            deleteClient(scanner, userbase, client);
        } else if (response == 0) {
            try {
                exportClient(client);
            }
            catch (Exception e){
                System.err.println(e);
            }
            return;
        }

    } // END clientDashboard

    // Delete a client
    //
    public static void deleteClient(Scanner scanner, HashMap<String, String> userbase, Client client) {
        client.deleteUser(scanner, userbase);
        // return main menu
        menu(scanner, userbase);
    }
    
    // Search for security by symbol
    //
    public static void searchSymbol(Scanner scanner, Client client) {
        System.out.print("SEARCH: ");
        String response = scanner.nextLine();
        System.out.println();
        // create new financial instrument to store symbol search results
        FinancialInstrument symbol = new FinancialInstrument();
        // try to get results for user search
        try {
            symbol = new FinancialInstrument(response);
            System.out.println("--- RESULTS ---");
            System.out.println("Symbol: " + symbol.getSymbol());
            System.out.println("Open: " + symbol.getOpen());
            System.out.println("High: " + symbol.getHigh());
            System.out.println("Low: " + symbol.getLow());
            System.out.println("Price: " + symbol.getPrice());
            System.out.println("Volume: " + symbol.getVolume());
            System.out.println("Day: " + symbol.getDate());
            System.out.println("Close: " + symbol.getClose());
            System.out.println("Change: " + symbol.getChange());
            System.out.println("Percent: " + symbol.getChangePercent());
            System.out.println();
        }
        // catch API errors
        catch (SymbolNotFoundException s) {
            System.err.println(s);
            return;
        }
        // catch any other exceptions
        catch (Exception e) {
            System.out.println("Unable to find symbol\n");
            // System.err.println(e);
            return; // Returns to client dashboard if symbol does not exist
        }

        int option = -1;

        while (!(option == 1 || option == 0))
        {
            System.out.println("---- ACTIONS ----\n"
                            + "[1] Place buy order\n"
                            + "[0] Return to dashboard");
            System.out.print("Choice: ");
            option = scanner.nextInt();
            scanner.nextLine();
            System.out.println();

            if (option == 1) {
                // place a buy order
                try {
                    placeBuyOrder(scanner, client, symbol);
                }
                // catch errors with user account balance
                catch (InsufficientFundsException i) {
                    System.err.println(i);
                }
                // catch any other exceptions
                catch(Exception e) {
                    System.out.println("Unable to place buy order");
                }
            } else if (option == 0) {
                return;
            }
        }
        
        System.out.println();

    } // END searchSymbol

    // Allow the user to view their portfolio
    //
    public static void viewPortfolio(Scanner scanner, Client client) {
        int openPositions = client.getNoOpenPositions();
        // check user has open positions
        if (openPositions <= 0) {
            System.out.println("No positions open\n");
            return; // return to dashboard if user has no open positions
        }
        // print client portfolio
        client.printPortfolio();

        int option = -1;

        while (!(option == 1 || option == 0))
        {
            System.out.println("---- ACTIONS ----\n"
                            + "[1] Close order\n"
                            + "[0] Return to dashboard");
            System.out.print("Choice: ");
            option = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            // close open position
            if (option == 1) {
                closeOrder(scanner, client);
            } else if (option == 0) {
                return;
            }
        }
        
    } // END viewPortfolio

    public static void viewAccountHistory(Scanner scanner, Client client) {
        client.printTransactionHistory();
    }
    
    public static void viewBalance(Client client) {
        try {
            double amount = client.getFunds();
            System.out.println("Balance: " + amount);
        }
        catch (Exception e) {
            System.out.println("Unable to retrieve account balance");
        }
        System.out.println();
    }

    // Deposit funds into user account
    //
    public static void depositFunds(Scanner scanner, Client client) {
        System.out.print("Amount to deposit: ");
        double amount = roundDouble(scanner.nextDouble());
        // deposit into use account
        try {
            client.deposit(amount);
            System.out.println("Successful deposit of " + amount);
        }
        // catch any exception
        catch (Exception e) {
            System.out.println("Account deposit failed");
        }
        System.out.println();
    } // END depositFunds
    
    // Withdraw funds from the users account
    //
    public static void withdrawFunds(Scanner scanner, Client client) {
        // System.out.println("DEBUG Option 5\n");

        System.out.print("Amount to withdraw: ");
        double amount = roundDouble(scanner.nextDouble());
        try {
            client.withdraw(amount);
            System.out.println("Successful withdrawal of " + amount);
        }
        catch (InsufficientFundsException i) {
            System.err.println(i);
        }
        catch (Exception e) {
            System.out.println("Account withdrawal failed");
        }
        System.out.println();
    } // END withdrawFunds

    // Place a buy order on symbol
    //
    public static void placeBuyOrder(Scanner scanner, Client client, FinancialInstrument security) throws InsufficientFundsException{
        System.out.print("Order volume: ");
        int volume = scanner.nextInt();
        scanner.nextLine();
        // calculate total cost of security
        double cost = volume * security.getPrice();
        // Check if user has sufficient funds
        if (client.getFunds() >= cost) {
            try {
                security.refreshAPI(); // refresh api to get updated price 
                client.withdraw(cost); // withdraw funds from client
                client.placeOrder(security, "buy", volume, cost); // place order with funds
            }
            catch (SymbolNotFoundException s) {
                System.err.println(s);
                System.out.println("Unable to refresh API");
            }
            catch (Exception e) {
                // System.err.println(e);
                System.out.println("Buy order failed");
            }
        }
        else {
            throw new InsufficientFundsException("Account has insufficient funds");
        }
    } // END placeBuyOrder

    // Close an order
    //
    public static void closeOrder(Scanner scanner, Client client) {
        int noPositions = client.getNoOpenPositions();
        int pos = -1;

        while (!((pos >= 0) && (pos <= noPositions + 1)))
        {
            System.out.println("---- CLOSE ORDER ----\n"
                            + "[+] Please choose position to close\n"
                            + "[+] 0 to return to dashboard");
            System.out.print("Position: ");
            pos = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
        }

        if (pos == 0) {
            return;
        }
        // close order
        else {
            client.closeOrder(pos-1); // Account indexed from 0
        }

    } // END closeOrder
    
    // Rounds doubles to 2 decimal places
    //
    public static double roundDouble(double amount) {
        return Math.round(amount*100.0)/100.0;
    } // END roundDouble

    // Quit the application
    //
    public static void quit(Scanner scanner) {
        // System.out.println("DEBUG Option 6\n");
        scanner.close();
        System.exit(0);
    } // END quit


}
