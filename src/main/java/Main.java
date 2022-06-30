import org.apache.commons.text.WordUtils;


import java.io.*;
import java.time.LocalDate;
import java.util.*;


public class Main {
    private static final String BOOKS_LOCATION = "books.csv";
    private static final String USERS_LOCATION = "users.csv";
    private static final String BORROWS_LOCATION = "borrowedBooks.csv";
    private static final String ORDERS_LOCATION = "orders.csv";
    private static final Scanner console = new Scanner(System.in);
    private static Student current = new Student();

    //------------------------------------------------------------------------------------------------------------------
    public static void displayLibrary(List<Books> books) {
        if (!books.isEmpty()) {
            for (Books b : books) {
                System.out.println(b);
            }
        } else {
            System.out.println("The library is EMPTY!!");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public static List<Books> readBooks() throws IOException {
        List<Books> books = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(BOOKS_LOCATION));
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] objectData = line.split(",");
            Books b = new Books(objectData[0], objectData[1], objectData[2], Integer.parseInt(objectData[3]));
            books.add(b);
//            System.out.println(b.csvFormat());
        }
        reader.close();
        return books;
    }

    public static List<RentedBooks> readBorrows() throws IOException {
        List<RentedBooks> rentedBooksList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(BORROWS_LOCATION));
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] objectData = line.split(",");
            RentedBooks b = new RentedBooks(
                    objectData[0], // code
                    objectData[1], // name
                    objectData[2], // author
                    LocalDate.parse(objectData[3]), // issued date
                    LocalDate.parse(objectData[4]), // expiry date
                    objectData[5]);
            rentedBooksList.add(b);
//            System.out.println(b.csvFormat());
        }
        reader.close();
        return rentedBooksList;
    }

    public static List<Student> readUsers() throws IOException {
        List<Student> studentList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(USERS_LOCATION));
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] objectData = line.split(",");
            Student student = new Student(objectData[0], objectData[1], Integer.parseInt(objectData[2]));
            studentList.add(student);
//            System.out.println(student.csvFormat());
        }
        reader.close();
        return studentList;
    }

    //------------------------------------------------------------------------------------------------------------------
    public static void mapByName(List<Books> booksList, Map<Integer, Books> booksMapByName) {
        if (!booksList.isEmpty()) {
            for (Books b : booksList) {
                booksMapByName.put(b.getName().hashCode(), b);
            }
        } else {
            System.out.println("Couldn't map the List cause it's empty");
        }
    }

    public static void mapByCode(List<Books> booksList, Map<Integer, Books> booksMapByCode) {
        if (!booksList.isEmpty()) {
            for (Books b : booksList) {
                booksMapByCode.put(b.getCode().hashCode(), b);
            }
        } else {
            System.out.println("Couldn't map the List cause it's empty");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public static void searchByName(String bookName, Map<Integer, Books> mapByName) {
        bookName = WordUtils.capitalize(bookName);
        Integer code = bookName.hashCode();
        if (mapByName.containsKey(code)) {
            System.out.println("The book was found in the library");
            System.out.println(mapByName.get(code));
        } else
            System.out.println("The book was not found in the library");
    }

    public static boolean searchByCode(String code, Map<Integer, Books> mapByCode) {
        Integer hashCode = code.hashCode();
        if (mapByCode.containsKey(hashCode)) {
            System.out.println(mapByCode.get(hashCode));
            return true;
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    public static void addBook(String bookCode, Map<Integer, Books> booksMapByCode) {
        if (!searchByCode(bookCode, booksMapByCode)) {
            System.out.print("Enter the name of the book : ");
            String bookName = WordUtils.capitalize(console.nextLine());
            System.out.print("Enter the author of the book : ");
            String bookAuthor = WordUtils.capitalize(console.nextLine());
            System.out.print("Enter the quantity of the book : ");
            int quantity = console.nextInt();

            Books brandNew = new Books(bookCode, bookName, bookAuthor, quantity);
            try {
                brandNew.addToCSV();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Increasing the quantity of the book by one");
            try {
                booksMapByCode.get(bookCode.hashCode()).incrementQuantity();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(booksMapByCode.get(bookCode.hashCode()));
        }
    }

    public static void updateBook(String bookCode, Map<Integer, Books> booksMapByCode) {
        if (searchByCode(bookCode, booksMapByCode)) {
            Integer hashCode = bookCode.hashCode();
            System.out.print("Enter the new name of the book : ");
            String bookName = console.nextLine();
            System.out.print("Enter the new author of the book : ");
            String bookAuthor = console.nextLine();
            System.out.print("Enter the new quantity of the book : ");
            int quantity = console.nextInt();

            Books newBook = booksMapByCode.get(hashCode);
            newBook.setName(bookName);
            newBook.setAuthor(bookAuthor);
            newBook.setQuantity(quantity);
            try {
                newBook.update();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Did you type the code correctly?");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public static void placeOrderByCode(String bookCode, Map<Integer, Books> booksMapByCode, String userID) {
        if (searchByCode(bookCode, booksMapByCode)) {
            Books placeHolder = booksMapByCode.get(bookCode.hashCode());
            if (placeHolder.getQuantity() > 0) {
                placeHolder.decrementQuantity();
                try {
                    placeHolder.update();
                    Writer writer = new FileWriter(ORDERS_LOCATION, true);
                    String csvFormat = String.format("%s,%s,%s,%s,%s\n",
                            placeHolder.getCode(),
                            placeHolder.getName(),
                            placeHolder.getAuthor(),
                            LocalDate.now(),
                            userID);
                    writer.write(csvFormat);
                    writer.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static boolean borrow(String bookCode, Map<Integer, Books> booksMapByCode, String userID) {
        if (searchByCode(bookCode, booksMapByCode)) {
            Books placeHolder = booksMapByCode.get(bookCode.hashCode());
            if (placeHolder.getQuantity() > 0) {
                RentedBooks b = new RentedBooks(placeHolder.getCode(), placeHolder.getName(), placeHolder.getAuthor(), userID);
                try {
                    if (b.addToCSV()) {
                        placeHolder.decrementQuantity();
                        return true;
                    }

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("This is not available at the moment");
            }
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    public static void viewOrder() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ORDERS_LOCATION));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitter = line.split(",");
                for (String s : splitter) {
                    System.out.print(s + "\t");
                }
                System.out.println();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void viewBorrowsByUser(List<RentedBooks> rentedBooksList, String userID) {
        boolean foundABook = false;
        if (!rentedBooksList.isEmpty()) {
            for (RentedBooks b : rentedBooksList) {
                if (b.getUserId().equals(userID)) {
                    foundABook = true;
                    System.out.println(b);
                }
            }
            if (!foundABook) {
                System.out.println("You have returned all the books and have borrowed none");
            }
        } else {
            System.out.println("There are no Borrowed Books");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public static boolean returnBook(List<RentedBooks> rentedBooksList, String bookCode, String userID) throws IOException {
        if (!rentedBooksList.isEmpty()) {
            for (RentedBooks b : rentedBooksList) {
                if (b.getCode().equals(bookCode) && b.getUserId().equals(userID)) {
                    b.removeFromCSV();
                    System.out.println(b);
                    return true;
                }
            }
        } else {
            System.out.println("There are no Issued books for you");
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    public static void reminder(List<RentedBooks> rentedBooksList, Student s) {
        List<RentedBooks> overdueBooks = new ArrayList<>();
        if (!rentedBooksList.isEmpty()) {
            for (RentedBooks b : rentedBooksList) {
                if (b.getUserId().equals(s.getMobileNumber()) && b.isOverDue()) {
                    overdueBooks.add(b);
                }
            }
        }
        if (!overdueBooks.isEmpty()) {
            System.out.println("It's a me Mario.");
            System.out.println("And I am here to remind you that you have overdue books");
            for (RentedBooks b : overdueBooks) {
                System.out.println(b);
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public static boolean adminLogin() {
        for (int i = 0; i < 3; i++) {
            System.out.print("Administrator : ");
            String email = console.next();
            System.out.print("Password : ");
            String password = console.next();
            if (email.equals("admin") && password.equals("admin")) {
                System.out.println("Log in Successful");
                return true;
            }
        }
        return false;
    }

    public static boolean userSignUp() {
        System.out.print("Enter your email : ");
        String email = console.next();
        System.out.print("Enter your phone number : ");
        String password = console.next();

        Student newStudent = new Student(email, password);
        try {
            return newStudent.addToCSV();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean userLogin(List<Student> students) {
        for (int i = 0; i < 3; i++) {
            System.out.print("Enter your email : ");
            String email = console.next();
            System.out.print("Enter your phone number : ");
            String password = console.next();

            for (Student s : students) {
                if (s.getEmail().equals(email) && s.getMobileNumber().equals(password)) {
                    current = s;
                    return true;
                }
            }
            System.out.println("Log-in unsuccessful");
        }
        return false;
    }
    //------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws IOException {
        boolean finished = false, endSubMenu, endSubSubMenu, loginSuccessful;
        int menuChoice, subMenuChoice;
        String bookName, bookCode;
        //--------------------------------------------------------------------------------------------------------------
        List<Books> books;
        List<Student> students;
        List<RentedBooks> rentedBooksList;
        Map<Integer, Books> booksMapByName = new HashMap<>();
        Map<Integer, Books> booksMapByCode = new HashMap<>();
        books = readBooks();
        students = readUsers();
        rentedBooksList = readBorrows();
        mapByName(books, booksMapByName);
        mapByCode(books, booksMapByCode);
        //--------------------------------------------------------------------------------------------------------------
        Menu mainMenu = new Menu();
        do {
            menuChoice = mainMenu.accountType();
            switch (menuChoice) {
                case Menu.ADMIN_ACCOUNT -> {
                    loginSuccessful = adminLogin();
                    endSubMenu = false;
                    while (!endSubMenu && loginSuccessful) {
                        subMenuChoice = mainMenu.adminMenu();
                        switch (subMenuChoice) {
                            case Menu.DISPLAY_LIBRARY -> displayLibrary(books);
                            case Menu.SEARCH -> {
                                System.out.print("Enter the book name : ");
                                bookName = console.nextLine();
                                searchByName(bookName, booksMapByName);
                            }
                            case Menu.ADD_BOOK -> {
                                System.out.print("Enter the book code : ");
                                bookCode = console.next();
                                console.nextLine();
                                addBook(bookCode, booksMapByCode);
                                books = readBooks();
                                mapByName(books, booksMapByName);
                                mapByCode(books, booksMapByCode);
                            }
                            case Menu.UPDATE_BOOK -> {
                                System.out.print("Enter the code of the book to be updated : ");
                                bookCode = console.next();
                                console.nextLine();
                                updateBook(bookCode, booksMapByCode);
                                books = readBooks();
                                mapByName(books, booksMapByName);
                                mapByCode(books, booksMapByCode);
                            }
                            case Menu.VIEW_ORDERS_ADMIN -> viewOrder();
                            case Menu.EXIT_ADMIN -> endSubMenu = true;
                        }
                    }
                }
                case Menu.STUDENT_ACCOUNT -> {
                    subMenuChoice = mainMenu.StudentForm();
                    endSubMenu = false;
                    while (!endSubMenu) {
                        switch (subMenuChoice) {
                            case Menu.REGISTER -> {
                                if (userSignUp()) students = readUsers();
                                endSubMenu = true;
                            }
                            case Menu.LOG_IN -> {
                                loginSuccessful = false;
                                endSubSubMenu = true;
                                if (!students.isEmpty()) {
                                    if (userLogin(students)) {
                                        loginSuccessful = true;
                                        endSubSubMenu = false;
                                        reminder(rentedBooksList, current);
                                    } else {
                                        System.out.println("Incorrect for three times");
                                    }
                                    while (loginSuccessful & !endSubSubMenu) {
                                        System.out.println("Current number of borrowed books : " + current.getNumberOfRentedBooks());
                                        subMenuChoice = mainMenu.studentMenu();
                                        switch (subMenuChoice) {
                                            case Menu.DISPLAY_LIBRARY -> displayLibrary(books);
                                            case Menu.SEARCH -> {
                                                System.out.print("Enter the book name : ");
                                                bookName = mainMenu.console.nextLine();
                                                searchByName(bookName, booksMapByName);
                                            }
                                            case Menu.PLACE_ORDER -> {
                                                System.out.print("Enter the code of the book : ");
                                                bookCode = mainMenu.console.next();
                                                if (searchByCode(bookCode, booksMapByCode)) {
                                                    System.out.println("Placing the order");
                                                    placeOrderByCode(bookCode, booksMapByCode, current.getEmail());
                                                }
                                            }
                                            case Menu.BORROW -> {
                                                if (current.getNumberOfRentedBooks() < current.getMAX_RENTED_BOOKS()) {
                                                    System.out.print("Enter the code of the book : ");
                                                    bookCode = mainMenu.console.next();
                                                    if (borrow(bookCode, booksMapByCode, current.getMobileNumber())) {
                                                        current.incrementRentedBooks();
                                                        rentedBooksList = readBorrows();
                                                    }
                                                } else {
                                                    System.out.println("You have exceeded the maximum number of rented books");
                                                }
                                            }
                                            case Menu.VIEW_BORROWS_STUDENT ->
                                                    viewBorrowsByUser(rentedBooksList, current.getMobileNumber());
                                            case Menu.RETURN_BOOK -> {
                                                System.out.print("Enter the code of the book : ");
                                                bookCode = mainMenu.console.next();
                                                if (returnBook(rentedBooksList, bookCode, current.getMobileNumber())) {
                                                    booksMapByCode.get(bookCode.hashCode()).incrementQuantity();
                                                    current.decrementRentedBooks();
                                                    rentedBooksList = readBorrows();
                                                } else {
                                                    System.out.println("This Book code Was not found");
                                                }
                                            }
                                            case Menu.EXIT_STUDENT -> {
                                                endSubSubMenu = true;
                                                endSubMenu = true;
                                            }
                                        }
                                    }
                                } else {
                                    System.out.println("No users exist in the System");
                                    endSubMenu = true;
                                }
                            }
                            case Menu.EXIT_FORM -> endSubMenu = true;
                        }
                    }
                }
                case Menu.EXIT_ACCOUNT -> finished = true;
            }
        } while (!finished);
        mainMenu.console.close();
        console.close();
    }
}