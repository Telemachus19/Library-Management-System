import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Books implements CSVControls {

    private final String code;

    private String name;

    private String author;
    private int quantity;
    private final String BOOKS_LOCATION = "books.csv";
    //------------------------------------------------------------

    // Constructor
    public Books(String code, String name, String author, int quantity) {
        this.code = code;
        this.name = name;
        this.author = author;
        this.quantity = quantity;
    }

    public Books(String code, String name, String author) {
        this.code = code;
        this.name = name;
        this.author = author;
    }

    // No-arg constructor
    public Books() {
        this.code = null;
        this.name = null;
        this.author = null;
        this.quantity = 0;
    }

    //------------------------------------------------------------------------------------------------------------------
    public String csvFormat() {
        return String.format("%s,%s,%s,%d", getCode(), getName(), getAuthor(), getQuantity());
    }

    public boolean addToCSV() throws IOException {
        boolean found = false;
        BufferedReader reader = new BufferedReader(new FileReader(BOOKS_LOCATION));
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] objectData = line.split(",");
            if (objectData[0].equals(this.getCode())) {
                found = true;
//                System.out.println(this.csvFormat());
                break;
            }
        }
        reader.close();
        if (!found) {
            Writer writer = new FileWriter(BOOKS_LOCATION, true);
            String csvFormat = String.format("%s,%s,%s,%d\n", getCode(), getName(), getAuthor(), getQuantity());
            writer.write(csvFormat);
            writer.close();
            return true;
        } else {
            System.out.println("The book exists in the library");
            return false;
        }
    }

    @Override
    public void removeFromCSV() throws IOException {
        List<Books> bookList = new ArrayList<>();
        String csv = this.csvFormat();
        BufferedReader reader = new BufferedReader(new FileReader(BOOKS_LOCATION));
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            if (!line.equalsIgnoreCase(csv)) {
                String[] objectData = line.split(",");
                Books b = new Books(objectData[0], objectData[1], objectData[2], Integer.parseInt(objectData[3]));
                bookList.add(b);
//                System.out.println(b.csvFormat());
            }
        }
        reader.close();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(BOOKS_LOCATION));
        bufferedWriter.write("code,name,author,quantity\n");
        for (Books books : bookList) {
            bufferedWriter.write(books.csvFormat());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    public void update() throws IOException {
        List<Books> bookList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(BOOKS_LOCATION));
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] objectData = line.split(",");
            if (objectData[0].equals(this.getCode())) {
                bookList.add(this);
//                System.out.println(this.csvFormat());
            } else {
                Books b = new Books(objectData[0], objectData[1], objectData[2], Integer.parseInt(objectData[3]));
                bookList.add(b);
//                System.out.println(b.csvFormat());
            }
        }
        reader.close();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(BOOKS_LOCATION));
        bufferedWriter.write("code,name,author,quantity\n");
        for (Books books : bookList) {
            bufferedWriter.write(books.csvFormat());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Code : " + getCode() + "\t" +
                "Book : " + getName() + "\t" +
                "Author : " + getAuthor() + "\t" +
                "Quantity : " + getQuantity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Books book = (Books) o;
        return Objects.equals(name, book.name) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity() throws IOException {
        this.quantity++;
        update();
    }

    public void decrementQuantity() {
        this.quantity--;
        try {
            update();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    // Test client
    public static void main(String[] args) throws IOException {
        Books books = new Books("120", "Atlas Shrugged", "Ayn Rand", 12);
//        books.addToCSV();
        books.removeFromCSV();
    }
}
