import java.io.IOException;

public interface CSVControls {
    boolean addToCSV() throws IOException;

    void removeFromCSV() throws IOException;

    void update() throws IOException;

    String csvFormat();
}
