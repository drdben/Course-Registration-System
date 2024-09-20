import java.time.LocalDate;

public class Complaint {
    private String matter;
    private boolean resolved;
    private String solution;
    private LocalDate date;

    // Constructor called by Student to file a complaint
    public Complaint(String problem) {
        this.matter = problem;
        this.resolved = false;
        this.solution = "";
        this.date = LocalDate.now(); // Sets the current date when the complaint is filed
    }

    // Getter for date
    public LocalDate getDate() {
        return date;
    }

    // Getter for matter
    public String getMatter() {
        return matter;
    }

    // Getter for resolved status
    public boolean isResolved() {
        return resolved;
    }

    // Getter for solution
    public String getSolution() {
        return solution;
    }

    // Setter for solution
    public void setSolution(String solution) {
        this.solution = solution;
    }

    // Setter for matter
    public void setMatter(String matter) {
        this.matter = matter;
    }

    // Setter for resolved status
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}
