import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Manager extends User{

    private ArrayList<Client> clients;

    public Manager(String name, String password, String userType) {
        super(name, password, userType);
    }
    
    public void addClient(Client c){
	    clients.add(c);
    }

    public void removeClient(int index) {
        this.clients.remove(index);
    }

    // Delete manager account
    //
    public void deleteUser(Scanner scanner, HashMap<String, String> userbase) {
        try{
            super.deleteUser(scanner); // extend deleteUser method from parent class (confirms account deletion)
        }
        catch (UserException u) {
            return;
        }
        deleteFile(System.getProperty("user.dir") + "\\data\\managers\\" + this.getName() + ".csv"); // delete manager clients details
        deleteFile(System.getProperty("user.dir") + "\\data\\users\\" + this.getName() + ".csv"); // delete manager details
        removeFromUserbase(userbase); // remove credentials from userbase
        System.out.println("Deleted manager account\n");


    }

    // Print information about mangers clients to the screen
    //
    public void printClientInfo() {
        // if manager has no clients assigned, print error 
        if (getNumberOfClients() <= 0) {
            System.out.println("No clients assigned\n");
        }
        // if manager has clients assigned, print client information
        else {
            System.out.println("--- CLIENT PORTFOLIO ---");
            System.out.println("Username, Balance, OpenPositions");

            Iterator<Client> it = clients.iterator();
            int count = 1;

            while (it.hasNext()){
                Client client = it.next();
                System.out.println(count + ". " + client.getName() + ", " + client.getFunds() + ", " + client.getNoOpenPositions());
                count++;
            }

            System.out.println();
        }
    }


    public Client getManagerClientByIndex(int index) {
        return this.clients.get(index);
    }

    // SETTER AND GETTER METHODS

    public int getNumberOfClients(){
	    return this.clients.size();
    }

    public ArrayList<Client> getClients() {
        return this.clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }
}
