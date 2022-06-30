import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Student implements CSVControls {
    private String email, mobileNumber;
    private int numberOfRentedBooks = 0;
    private final int MAX_RENTED_BOOKS = 3;
    private final String USERS_LOCATION = "users.csv";
    //------------------------------------------------------------------------------------------------------------------
    public Student(String email,String mobileNumber){
        this.email = email;
        this.mobileNumber = mobileNumber;
    }
    public Student(String email,String mobileNumber, int numberOfRentedBooks){
        this.email=email;
        this.mobileNumber = mobileNumber;
        this.numberOfRentedBooks = numberOfRentedBooks;
    }
    public Student(){
        this.email = null;
        this.mobileNumber = null;
    }
    @Override
    public String csvFormat() {
        return String.format("%s,%s,%d", getEmail(), getMobileNumber(), getNumberOfRentedBooks());
    }

    @Override
    public boolean addToCSV() throws IOException {
        boolean found = false;
        BufferedReader reader = new BufferedReader(new FileReader(USERS_LOCATION));
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] objectData = line.split(",");
            if (objectData[0].equalsIgnoreCase(getEmail()) || objectData[1].equals(getMobileNumber())) {
                found = true;
//                System.out.println(this.csvFormat());
                break;
            }
        }
        reader.close();
        if(!found){
            Writer writer = new FileWriter(USERS_LOCATION, true);
            String csvFormat = String.format("%s,%s,%d\n", getEmail(), getMobileNumber(), getNumberOfRentedBooks());
            writer.write(csvFormat);
            writer.close();
            return true;
        }else {
            System.out.println("This email is already used");
            return false;
        }
    }

    @Override
    public void removeFromCSV() throws IOException {
        List<Student> studentList = new ArrayList<>();
        String csv = this.csvFormat();
        BufferedReader reader = new BufferedReader(new FileReader(USERS_LOCATION));
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            if (!line.equals(csv)) {
                String[] objectData = line.split(",");
                Student student = new Student(objectData[0], objectData[1],Integer.parseInt(objectData[2]));
                studentList.add(student);
//                System.out.println(student.csvFormat());
            }
        }
        reader.close();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(USERS_LOCATION));
        bufferedWriter.write("emailId,mobileNumber,numberOfRentedBooks\n");
        for (Student student : studentList) {
            bufferedWriter.write(student.csvFormat());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    @Override
    public void update() throws IOException {
        List<Student> studentList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(USERS_LOCATION));
        String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] objectData = line.split(",");
            if (objectData[0].equals(getEmail())) {
                studentList.add(this);
//                System.out.println(this.csvFormat());
            } else {
                Student student = new Student(objectData[0], objectData[1], Integer.parseInt(objectData[2]));
                studentList.add(student);
//                System.out.println(student.csvFormat());
            }
        }
        reader.close();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(USERS_LOCATION));
        bufferedWriter.write("emailId,mobileNumber,numberOfRentedBooks\n");
        for (Student student : studentList) {
            bufferedWriter.write(student.csvFormat());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();

    }

    public int getNumberOfRentedBooks() {
        return numberOfRentedBooks;
    }

    public void setNumberOfRentedBooks(int numberOfRentedBooks) {
        this.numberOfRentedBooks = numberOfRentedBooks;
    }
    public void decrementRentedBooks(){
        this.numberOfRentedBooks--;
        try {
            update();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void incrementRentedBooks(){
        this.numberOfRentedBooks++;
        try {
            update();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public int getMAX_RENTED_BOOKS() {
        return MAX_RENTED_BOOKS;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
