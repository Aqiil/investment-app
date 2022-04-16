public class SymbolNotFoundException extends Exception {

    public SymbolNotFoundException(){
        super();
    }   

    public SymbolNotFoundException(String message){
        super(message + "\n");
    }    
}
