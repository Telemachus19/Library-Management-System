import java.util.Scanner;

public class Menu {
    Scanner console = new Scanner(System.in);
    public static final int DISPLAY_LIBRARY = 1,
            SEARCH = 2,
            ADD_BOOK = 3,
            UPDATE_BOOK = 4,
            VIEW_ORDERS_ADMIN = 5,
            EXIT_ADMIN = 6,
            ADMIN_ACCOUNT = 1,
            STUDENT_ACCOUNT = 2,
            EXIT_ACCOUNT = 3,
            REGISTER = 1,
            LOG_IN = 2,
            EXIT_FORM = 3,
            PLACE_ORDER = 3,
            VIEW_BORROWS_STUDENT = 4,
            RETURN_BOOK = 5,
            EXIT_STUDENT = 7,
            BORROW = 6;

    //-----------------------------------------------------
    public int accountType() {
        boolean inputCorrect = false;
        int choice = -1;
        do {
            System.out.println("Select the account type");
            System.out.println("1. Administrator");
            System.out.println("2. Student");
            System.out.println("3. Exit the Menu");
            System.out.print("Enter your choice [1-3]: ");
            try {
                choice = Integer.parseInt(console.next());
                console.nextLine();
                if (choice >= 1 && choice <= 3)
                    inputCorrect = true;
                else
                    System.out.println("Incorrect value");

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Enter an Integer");
            }
        } while (!inputCorrect);
        return choice;
    }

    public int StudentForm() {
        boolean inputCorrect = false;
        int choice = -1;
        do {
            System.out.println("1. Register");
            System.out.println("2. Log-in");
            System.out.println("3. Exit the Menu");
            System.out.print("Enter your choice[1-3] : ");
            try {
                choice = Integer.parseInt(console.next());
                console.nextLine();
                if (choice >= 1 && choice <= 3)
                    inputCorrect = true;
                else
                    System.out.println("Incorrect value");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Enter an integer between ");
            }
        } while (!inputCorrect);
        return choice;
    }

    public int studentMenu() {
        boolean inputCorrect = false;
        int choice = -1;
        do {
            System.out.println("1. Display Library");
            System.out.println("2. Search for a book");
            System.out.println("3. Place an order");
            System.out.println("4. View Borrowed books");
            System.out.println("5. Return a book");
            System.out.println("6. Borrow a book");
            System.out.println("7. Exit the menu");
            System.out.print("Enter your choice[1-6] : ");
            try {
                choice = Integer.parseInt(console.next());
                console.nextLine();
                if (choice >= 1 && choice <= 7)
                    inputCorrect = true;
                else
                    System.out.println("Incorrect value");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Enter an Integer");
            }
        } while (!inputCorrect);
        return choice;
    }

    public int adminMenu() {
        boolean inputCorrect = false;
        int choice = -1;
        while (!inputCorrect) {
            System.out.println("1. Display Library");
            System.out.println("2. Search for a book");
            System.out.println("3. Add a book");
            System.out.println("4. Update book");
            System.out.println("5. View Orders");
            System.out.println("6. Exit the menu");
            System.out.print("Enter your choice[1-6] : ");
            try {
                choice = Integer.parseInt(console.next());
                console.nextLine();
                if (choice >= 1 && choice <= 6)
                    inputCorrect = true;
                else
                    System.out.println("Incorrect value");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Enter an Integer");
            }
        }
        return choice;
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.adminMenu();
    }
}
