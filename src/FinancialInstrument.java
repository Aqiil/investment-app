import java.net.URI;
import java.net.http.*;
import org.json.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FinancialInstrument {
    private String symbol;
    private double open;
    private double high;
    private double low;
    private double price;
    private double volume;
    private LocalDate date;
    private double close; // Previous day close value
    private double change; // Change since previous day close
    private double changePercent; // Change as percentage 

    private static String apiKey;

    public FinancialInstrument() {
        FinancialInstrument.apiKey = "4G661J5M2WAKJSM0";
    }

    // Create a new financial instrument for a ticker symbol
    //
    public FinancialInstrument(String ticker) throws SymbolNotFoundException{
        FinancialInstrument.apiKey = "4G661J5M2WAKJSM0";
        try {
            startAPI(ticker);
        }
        catch (Exception e) {
            throw new SymbolNotFoundException("ERROR connecting API");
        }
    }

    // Starts the API to populate attribute values
    //
    public void startAPI(String ticker) throws SymbolNotFoundException{
        // create httprequest to retrieve API data from alpha vantage
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + ticker + "&apikey=" + apiKey))
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();

        String jsonString = "";

        try {
            // store requested api data as string
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            jsonString = response.body(); 
            // parse response as json object for manipulation
            JSONObject obj = new JSONObject(jsonString);

            // parse json data and set string for symbol field
            this.setSymbol(obj.getJSONObject("Global Quote").getString("01. symbol"));

            // parse json data, format as double, round to 2dp and set fields
            this.setOpen(roundDouble(Double.parseDouble(obj.getJSONObject("Global Quote").getString("02. open"))));
            this.setHigh(roundDouble(Double.parseDouble(obj.getJSONObject("Global Quote").getString("03. high"))));
            this.setLow(roundDouble(Double.parseDouble(obj.getJSONObject("Global Quote").getString("04. low"))));
            this.setPrice(roundDouble(Double.parseDouble(obj.getJSONObject("Global Quote").getString("05. price"))));
            this.setVolume(roundDouble(Double.parseDouble(obj.getJSONObject("Global Quote").getString("06. volume"))));
            this.setClose(roundDouble(Double.parseDouble(obj.getJSONObject("Global Quote").getString("08. previous close"))));
            this.setChange(roundDouble(Double.parseDouble(obj.getJSONObject("Global Quote").getString("09. change"))));

            // parse json data, format as date and set date for date field
            this.setDate(parseDateString(obj.getJSONObject("Global Quote").getString("07. latest trading day")));
            // calculate the change for previous day close as percentage
            this.setChangePercent(calcChangePercent());
        }
        catch (Exception e) {
            // System.err.println(e);
            throw new SymbolNotFoundException();
        }

    }

    // Refresh the API to update attribute values
    //
    public void refreshAPI() throws SymbolNotFoundException{
        startAPI(this.symbol);
    }

    // Rounds doubles to 2 decimal places
    //
    public double roundDouble(double amount) {
        return Math.round(amount*100.0)/100.0;
    }

    // Calculate change percentage using parsed API data set to required fields
    //
    public double calcChangePercent() {
        return roundDouble((this.change / this.close) * 100);
    }

    // Parse date string into LocalDate object
    //
    public LocalDate parseDateString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);

        return parsedDate;
    }
    
    // Calculate growth of position since initial order
    //
    public double calcPercentChangeNow(double initialPrice) {
        return roundDouble((this.price - initialPrice) / initialPrice * 100);
    } 


    // GETTER AND SETTER METHODS

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getOpen() {
        return this.open;
    }

    public void setOpen(double open) {
        this.open = open;
    } 

    public double getHigh() {
        return this.high;
    } 

    public void setHigh(double high) {
        this.high = high;
    } 

    public double getLow() {
        return this.low;
    }

    public void setLow(double low) {
        this.low = low;
    } 

    public double getPrice() {
        return this.price;
    } 

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume() {
        return this.volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getClose() {
        return this.close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getChange() {
        return this.change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getChangePercent() {
        return this.changePercent;
    }

    public void setChangePercent(double percentage) {
        this.changePercent = percentage;
    } 

}
