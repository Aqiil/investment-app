import java.time.LocalDate;

public class Transaction {
    private String ticker;
    private String type; // buy, sell
    private double amount; // volume of security
    private double cost;
    private LocalDate openDate;
    private double growth; // growth of position since purchase
    private double positionValue; // value of position at present
    private String status; // open, closed
    private LocalDate closeDate;

    // Constructor for new transaction
    //
    public Transaction(String ticker, String type, double amount, double cost, LocalDate openDate) {
        this.setTicker(ticker);
        this.setType(type);
        this.setAmount(amount);
        this.setCost(cost);
        this.setOpenDate(openDate);
        this.setGrowth(0);
        this.setPositionValue(cost);
        this.setStatus("open");
        this.setCloseDate(null);
    }

    // Constructor for imported transaction
    //
    public Transaction(String ticker, String type, double amount, double cost, LocalDate openDate, double growth, double positionValue, String status, LocalDate closeDate) {
        this.setTicker(ticker);
        this.setType(type);
        this.setAmount(amount);
        this.setCost(cost);
        this.setOpenDate(openDate);
        this.setGrowth(growth);
        this.setPositionValue(positionValue);
        this.setStatus(status);
        this.setCloseDate(closeDate);
    }

    // Update transaction
    //
    public void updateOpenTransaction() {
        try {
            // create new financial instrument object to caclulate growth and value of position
            FinancialInstrument f = new FinancialInstrument(this.getTicker());
            // calculate the price for a single unit of security
            double unitPrice = this.getCost()/this.getAmount();

            updateGrowth(f, unitPrice);
            updateCurrentValue(f, unitPrice);
        }
        catch (SymbolNotFoundException s) {
            System.out.println("Error updating transaction");
        }
        // Catch and print any unexpected errors
        catch (Exception e) {
            System.err.println(e);
        }
    }

    // Update the growth of security
    //
    public void updateGrowth(FinancialInstrument f, double unitPrice) {
        // Calculate the percentage growth of the position and update the growth attribute
        double updatedGrowth = f.calcPercentChangeNow(unitPrice);
        this.setGrowth(updatedGrowth); 
    }

    // Update value of security to current value
    //
    public void updateCurrentValue(FinancialInstrument f, double unitPrice) {
        // Calculate the current value by finding the price difference per share and multiplying by the number of shares
        double currentValue = (unitPrice - f.getPrice()) * this.getAmount();
        this.setPositionValue(roundDouble(currentValue));
    }

    // Rounds doubles to 2 decimal places
    //
    public double roundDouble(double amount) {
        return Math.round(amount*100.0)/100.0;
    } // END roundDouble


    // GETTER AND SETTER METHODS

    public String getTicker() {
        return this.ticker;
    } // END getTicker

    public void setTicker(String ticker) {
        this.ticker = ticker;
    } // END setTicker

    public String getType() {
        return this.type;
    } // END getType

    public void setType(String type) {
        this.type = type;
    } // END setType

    public Double getAmount() {
        return this.amount;
    } // END getAmount

    public void setAmount(double amount) {
        this.amount = amount;
    } // END setAmount

    public Double getCost() {
        return this.cost;
    } // END getCost

    public void setCost(double cost) {
        this.cost = cost;
    } // END setCost

    public LocalDate getOpenDate() {
        return this.openDate;
    } // END getOpenDate

    public void setOpenDate(LocalDate date) {
        this.openDate = date;
    } // END setOpenDate

    public LocalDate getCloseDate() {
        return this.closeDate;
    } // END getCloseDate

    public void setCloseDate(LocalDate date) {
        this.closeDate= date;
    } // END setCloseDate

    public double getGrowth() {
        return this.growth;
    } // END getGrowth

    public void setGrowth(double growth) {
        this.growth = growth;
    } // END setGrowth

    public double getPositionValue() {
        return this.positionValue;
    } // END getPositionValue

    public void setPositionValue(double positionValue) {
        this.positionValue = positionValue;
    } // END setPositionValue

    public void setStatus(String status) {
        this.status = status;
    } // END setStatus

    public String getStatus() {
        return this.status;
    } // END getStatus
}
