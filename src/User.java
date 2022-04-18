import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JOptionPane;

abstract class User{

    private String name;
    private String password;
    private String accountType;
    
    public User(String name, String password, String accountType) {
	    this.name = name;
        this.password = password;
        this.setAccountType(accountType);
    }

    // Delete user account
    //
    public void deleteUser(Scanner scanner) throws UserException {
        // int response = -1;
        // // confirm account deletion
        // while (!(response == 1 || response == 0))
        // {
        //     System.out.println("***** ACCOUNT DELETION *****\n"
        //                     + "[1] Confirm\n"
        //                     + "[0] Cancel");
        //     System.out.print("Choice: ");
        //     response = scanner.nextInt();
        //     scanner.nextLine();
        //     System.out.println();
        //     // account deletion confirmed
        //     if (response == 1) {
        //         System.out.println("Deleting account...\n");
        //     // account deletion cancelled
        //     } else {
        //         System.out.println("Account deletion cancelled\n");
        //         throw new UserException(); // throw exception to escape subclass methods
        //     } 
        // }

        int dialogResult = JOptionPane.showConfirmDialog (null, "Confirm account deletion","Account Deletion",JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            System.out.println("Deleting account...\n");
        }
        else {
            System.out.println("Account deletion cancelled\n");
            throw new UserException(); // throw exception to escape subclass methods
        }
    }

    // delete user file
    //
    public void deleteFile(String path) {
        File file = new File(path); 
        try {
            file.delete();
        }
        catch (Exception e) {
            System.err.println(e);
            System.out.println("Error deleting: " + path);
        }
    }

    public void removeFromUserbase(HashMap<String, String> userbase) {
        userbase.remove(this.getName());
    }

    public String getName() {
	    return this.name;
    }

    public void setName(String name) {
	    this.name=name;
    }

    public String getPass() {
        return this.password;
    }

    public void setPass(String password) {
        this.password = password;
    }
    
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return this.accountType;
    }
    
}