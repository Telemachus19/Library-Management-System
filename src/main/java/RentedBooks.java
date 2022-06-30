import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class RentedBooks extends Books {
    private final String BORROWED_LOCATION = "borrowedBooks.csv";
    private LocalDate issuedDate;

    private LocalDate expiryDate;


    private String userId;

    public String getUserId() {
        return userId;
    }

    public RentedBooks() {
        super();
    }

    public RentedBooks(String code, String name, String author, String userId) {
        super(code, name, author);
        this.issuedDate = LocalDate.now();
        this.expiryDate = LocalDate.now();
        this.expiryDate = expiryDate.plusDays(12);
        this.userId = userId;
    }

    public RentedBooks(String code, String name, String author, LocalDate issuedDate, LocalDate expiryDate, String userId) {
        super(code, name, author);
        this.issuedDate = issuedDate;
        this.expiryDate = expiryDate;
        this.userId = userId;
    }

    public boolean isOverDue() {
        int diff = expiryDate.compareTo(LocalDate.now());
        return diff < 0;
    }

    public double calculateFine() {
        if (isOverDue()) {
            Period due = expiryDate.until(LocalDate.now());
            return due.getDays() * 40;
        } else {
            return 0;
        }
    }

    @Override
    public String csvFormat() {
        return String.format("%s,%s,%s,%s,%s,%s", this.getCode(), this.getName(), this.getAuthor(), this.getIssuedDate(), this.getExpiryDate(), this.getUserId());
    }

    @Override
    public String toString() {
        return "Code : " + this.getCode() + "\t" +
                "Book name : " + super.getName() + "\t" +
                "Author : " + super.getAuthor() + "\t" +
                "Issued on : " + this.getIssuedDate() + "\t" +
                "Expiry's on : " + this.getExpiryDate() + "\t" +
                ((isOverDue()) ? "Fine : " + this.calculateFine() : "") + "\n";
    }

    @Override
    public boolean addToCSV() throws IOException {
        boolean found = false;
        BufferedReader reader = new BufferedReader(new FileReader(BORROWED_LOCATION));
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] objectData = line.split(",");
            if (objectData[0].equals(this.getCode()) && objectData[5].equals(this.userId)) {
                found = true;
//                System.out.println(this.csvFormat());
                break;
            }
        }
        reader.close();
        if (!found) {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(BORROWED_LOCATION, true));
//            System.out.println(this.csvFormat());
            bufferedWriter.write(this.csvFormat());
            bufferedWriter.newLine();
            bufferedWriter.close();
            return true;
        } else {
            System.out.println("You have already borrowed this book");
            return false;
        }
    }

    @Override
    public void removeFromCSV() throws IOException {
        List<RentedBooks> rentedBooksList = new ArrayList<>();
        String csv = this.csvFormat();
        BufferedReader reader = new BufferedReader(new FileReader(BORROWED_LOCATION));
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            if (!line.equalsIgnoreCase(csv)) {
                String[] objectData = line.split(",");
                RentedBooks b = new RentedBooks(objectData[0],
                        objectData[1],
                        objectData[2],
                        LocalDate.parse(objectData[3]),
                        LocalDate.parse(objectData[4]),
                        objectData[5]);
                rentedBooksList.add(b);
//                System.out.println(b.csvFormat());
            }
        }
        reader.close();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(BORROWED_LOCATION));
        bufferedWriter.write("code,name,author,issuedDate,expiryDate,userId\n");
        for (RentedBooks books : rentedBooksList) {
            bufferedWriter.write(books.csvFormat());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    public void update() throws IOException {
        List<RentedBooks> rentedBooksList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(BORROWED_LOCATION));
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] objectData = line.split(",");
            if (objectData[0].equals(this.getCode())) {
                rentedBooksList.add(this);
//                System.out.println(this.csvFormat());
            } else {
                RentedBooks b = new RentedBooks(objectData[0],
                        objectData[1],
                        objectData[2],
                        LocalDate.parse(objectData[3]),
                        LocalDate.parse(objectData[4]),
                        objectData[5]);
                rentedBooksList.add(b);
//                System.out.println(b.csvFormat());
            }
        }
        reader.close();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(BORROWED_LOCATION));
        bufferedWriter.write("code,name,author,issuedDate,expiryDate,userId\n");
        for (RentedBooks books : rentedBooksList) {
            bufferedWriter.write(books.csvFormat());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public static void main(String[] args) throws IOException {
        RentedBooks issuedBook = new RentedBooks("00002", "ayeaye", "nyenye", "123");
        issuedBook.addToCSV();
//        issuedBook.removeFromCSV();

//        issuedBook.setAuthor("saysay");
//        issuedBook.update();
    }
}
