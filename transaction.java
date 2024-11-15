public class transaction {
    private int transID;
    private int totalAmount;
    private  String date;
    private Buyer buyer;
    private Seller seller;
    private Book book;

    public transaction(int transID, int totalAmount, String date, Buyer buyer, Seller seller, Book book) {
        this.transID = transID;
        this.totalAmount = totalAmount;
        this.date = date;
        this.buyer = buyer;
        this.seller = seller;
        this.book = book;
    }

    // Getters and setters
    public int getTransID() {
        return transID.get();
    }

    public void setTransID(int transID) {
        this.transID.set(transID);
    }

    public int transIDProperty() {
        return transID;
    }

    public int getTotalAmount() {
        return totalAmount.get();
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public int totalAmountProperty() {
        return totalAmount;
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String dateProperty() {
        return date;
    }

    public Buyer getBuyer() {
        return buyer.get();
    }

    public void setBuyer(Buyer buyer) {
        this.buyer.set(buyer);
    }

    public Buyer buyerProperty() {
        return buyer;
    }

    public Seller getSeller() {
        return seller.get();
    }

    public void setSeller(Seller seller) {
        this.seller.set(seller);
    }

    public Seller sellerProperty() {
        return seller;
    }

    public Book getBook() {
        return book.get();
    }

    public void setBook(Book book) {
        this.book.set(book);
    }

    public Book bookProperty() {
        return book;
    }

    // Add transaction to database
    public void addTransactionToDatabase() {
    }
}

