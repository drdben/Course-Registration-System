import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Admin extends User implements CourseModifier{
    // Class variables
    private ArrayList<Course> Sem1 = new ArrayList<>();
    private ArrayList<Course> Sem2 = new ArrayList<>();
    private ArrayList<ArrayList<Course>> Semlist = new ArrayList<>();
    private ArrayList<Course> pr1 = new ArrayList<>();
    private ArrayList<Course> pr2 = new ArrayList<>();
    private ArrayList<Course> pr3 = new ArrayList<>();
    private ArrayList<Course> pr4 = new ArrayList<>();
    private ArrayList<Course> pr5 = new ArrayList<>();
    private ArrayList<Professor> Proflist = new ArrayList<>();
    private ArrayList<Student> Students = new ArrayList<>();
    private ArrayList<Complaint> Complaints = new ArrayList<>();

    // Constructor
    public Admin() {
        super();
        this.setPassword("1234");

        initializeSemlist();
        initializeProflist();
        courseprofmap();
        initializeStudents();
        for (Course course : Sem1) {
            for (Student student : Students) {
                course.getEnrolled().add(student);
            }
        }
    }

    // Course management methods
    public ArrayList<Course> getCoursesForSemester(int semester) {
        if (semester == 1) {
            return Sem1;
        } else {
            return Sem2;
        }
    }

    public Course getCourseByCode(int courseCode, int semester) {
        ArrayList<Course> courses = getCoursesForSemester(semester);
        for (Course course : courses) {
            if (course.getCode() == courseCode) {
                return course;
            }
        }
        return null;
    }

    //Modifying Courses:
    public void ManageCourses(){
        boolean view = true;
        while(view){
            System.out.println("1. View Courses 2. Add courses 3. Delete Course 4. Back\n");
            int choice = sc.nextInt();
            switch(choice){
                case 1:
                    System.out.println("Course Catalogue of Hogwarts IIITD:\n ");
                    int cnt=1;
                    for(ArrayList<Course> sem: Semlist){
                        System.out.println("Semester "+cnt+": \n");
                        for(Course course1: sem){
                            System.out.println(course1.getTitle()+" Code: "+course1.getCode());
                        }
                        cnt++;
                    }
                    break;
                case 2:
                    System.out.println("Enter the following details: \nCourse Title: ");
                    String title = sc.next();
                    System.out.println("\nCourse Code: ");
                    int code = sc.nextInt();
                    System.out.println("\nNo of Credits: ");
                    int credits = sc.nextInt();
                    ArrayList<Course>Pre_Requisites=new ArrayList<>();
                    System.out.println("\nNo of PreRequisites: ");
                    int num = sc.nextInt();
                    if(num==0){
                        ;
                    }
                    else{
                        for(int i=0;i<num;i++){
                            System.out.println("\nEnter Prereq code followed by its semester no: ");
                            int precode=sc.nextInt();
                            int presem = sc.nextInt();
                            Course prereq = getCourseByCode(precode, presem);
                            Pre_Requisites.add(prereq);
                        }
                    }
                    System.out.println("\nProfessor ID (harry/hermione/dumbledore/ubmridge): ");
                    String prof = sc.next();
                    Professor newprofessor = null;
                    for(Professor professor: Proflist){
                        if(professor.getEmail().equals(prof)){
                            newprofessor=professor;
                        }
                    }
                    System.out.println("\nEnter Syllabus: ");
                    String syllabus = sc.next();
                    System.out.println("\nEnter schedule: ");
                    String schedule = sc.next();
                    //default limit is 200;
                    //default location is Dungeons
                    int limit =200;
                    System.out.println("Enter semester:\n ");
                    int sem = sc.nextInt();

                    Course addcourse = new Course(code, title, credits, Pre_Requisites, newprofessor, syllabus, schedule, limit, sem);
                    for(int j =0;j< Semlist.size();j++){
                        if(j==sem-1){
                            Semlist.get(j).add(addcourse);
                        }
                    }
                    newprofessor.getClist().add(addcourse);
                    break;
                case 3:
                    System.out.println("Enter the following details in order: \nCourse Code \nCourse Semester");
                    int dropcode= sc.nextInt();
                    int dropsem = sc.nextInt();
                    Course drop = getCourseByCode(dropcode,dropsem);
                    ModifyCourse(drop);
                    break;
                case 4:
                    view = false;
                    break;
            }
        }
    }
    @Override
    public void ModifyCourse(Course courseToDelete) {
        //function to DELETE COURSES (modify=delete)
            // Remove from semester lists
            Sem1.remove(courseToDelete);
            Sem2.remove(courseToDelete);

            // Remove from prerequisite lists
            pr1.remove(courseToDelete);
            pr2.remove(courseToDelete);
            pr3.remove(courseToDelete);
            pr4.remove(courseToDelete);
            pr5.remove(courseToDelete);

            // Remove from professor's course list
            Professor prof = courseToDelete.getProf();
            if (prof != null) {
                prof.getClist().remove(courseToDelete);
            }

            // Remove course from enrolled students' lists
            for (Student student : Students) {
                student.getCurrentCourses().remove(courseToDelete);
                //Won't remove from completed list.
            }

            // Update other courses that might have this course as a prerequisite
            for (Course course : Sem1) {
                if (course.getPreReqs() != null && !course.getPreReqs().isEmpty()){
                    course.getPreReqs().remove(courseToDelete);
                }
            }
            for (Course course : Sem2) {
                if (course.getPreReqs() != null && !course.getPreReqs().isEmpty()){
                    course.getPreReqs().remove(courseToDelete);
                }
            }
            System.out.println("Course " + courseToDelete.getTitle() + " has been dropped from the system. (Can't be registered anymore).");
    }

    public void ManageStudents() {
        System.out.println("The following students exist in the institute:\n");
        for(Student student: Students){
            System.out.println(student.getEmail()+" contact: "+student.getContact()+" grades: ");
            student.getGPA();
            System.out.println("\n");
        }
        boolean edit = true;
        while (edit) {
            System.out.println("1. Edit ID 2. Edit Contact Info 3. Add new Student 4. Back");
            int choice = sc.nextInt();
            sc.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    // Edit Student ID
                    System.out.println("Enter the email of the student whose ID you want to edit:");
                    String emailToEdit = sc.nextLine();
                    Student studentToEditID = findStudentByEmail(emailToEdit);

                    if (studentToEditID != null) {
                        System.out.println("Enter new ID:");
                        String newID = sc.nextLine();
                        studentToEditID.setEmail(newID);  // Assuming Student has setID() method
                        System.out.println("Student ID updated.");
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 2:
                    // Edit Contact Info
                    System.out.println("Enter the email of the student whose contact info you want to edit:");
                    String emailToEditContact = sc.nextLine();
                    Student studentToEditContact = findStudentByEmail(emailToEditContact);

                    if (studentToEditContact != null) {
                        System.out.println("Enter new contact info:");
                        String newContact = sc.nextLine();
                        studentToEditContact.setContact(newContact);  // Assuming Student has setContact() method
                        System.out.println("Student contact info updated.");
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 3:
                    // Add new Student
                    System.out.println("Enter new student details.");
                    System.out.print("\nID: ");
                    String id = sc.next();
                    System.out.print("\nContact: ");
                    String contact = sc.next();

                    Student newStudent = new Student(id, contact);
                    Students.add(newStudent);
                    System.out.println("New student added.");
                    break;

                case 4:
                    edit = false;
                    System.out.println("Exiting student management.");
                    break;
                default:
                    System.out.println("Invalid choice. Please select again.");
                    break;
            }
        }
    }

    // Helper method to find a student by email
    private Student findStudentByEmail(String email) {
        for (Student student : Students) {
            if (student.getEmail().equals(email)) {
                return student;
            }
        }
        return null;
    }

    public void AssignProfessors(){
        System.out.println("Assign Professors to a Course\n");
        System.out.println("""
                Enter the following details in order:\
                Course Code
                Course Semester
                Professor to Assign to(ID)
                \n""");
        int code = sc.nextInt();
        int sem = sc.nextInt();
        String profid = sc.next();
        Course assignee = getCourseByCode(code, sem);
        if (assignee == null) {
            System.out.println("No such course exists. Auto Exiting.\n");
            return;
        }
        Professor currentprof = assignee.getProf();
        if(currentprof!=null){
            currentprof.getClist().remove(assignee);
        }
        Professor newprof = null;
        for(Professor professor: Proflist){
            if(professor.getEmail().equals(profid)){
                newprof=professor;
                break;
            }
        }
        if (newprof==null){
            System.out.println("No such Professor exists. Auto Exiting.\n");
            return;
        }
        newprof.getClist().add(assignee);
        assignee.setProf(newprof);
        System.out.println("New Assignment completed successfully!\n");
    }

    // Complaint management methods

    public void AccessComplaints() {
        boolean view = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Define date format

        while (view) {
            System.out.println("1. All Complaints 2. Complaints filed on a Date 3. Back 4. Pending Complaints 5. Resolved Complaints\n");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Showing all Complaints filed:\n");
                    int cnt = 1;
                    for (Complaint complaint : Complaints) {
                        System.out.println(cnt + ". Date: " + complaint.getDate() + " - " + complaint.getMatter() + "\n");
                        solve(complaint);
                        cnt++;
                    }
                    break;

                case 2:
                    // Date filter implementation
                    System.out.println("Enter date (format: yyyy-MM-dd): ");
                    sc.nextLine(); // Consume the newline character after nextInt()
                    String dateInput = sc.nextLine();
                    try {
                        LocalDate inputDate = LocalDate.parse(dateInput, formatter);
                        System.out.println("Complaints filed on " + inputDate + ":\n");
                        for (Complaint complaint : Complaints) {
                            if (complaint.getDate().equals(inputDate)) {
                                System.out.println("Date: " + complaint.getDate() + " - " + complaint.getMatter() + "\n");
                                solve(complaint);
                            }
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Please enter the date in the format yyyy-MM-dd.");
                    }
                    break;

                case 3:
                    view = false;
                    break;

                case 4:
                    // Show only pending complaints
                    System.out.println("Pending Complaints:\n");
                    cnt = 1;
                    for (Complaint complaint : Complaints) {
                        if (!complaint.isResolved()) {  // Check if complaint is not resolved
                            System.out.println(cnt + ". Date: " + complaint.getDate() + " - " + complaint.getMatter() + "\n");
                            solve(complaint);
                            cnt++;
                        }
                    }
                    if (cnt == 1) {
                        System.out.println("No pending complaints.\n");
                    }
                    break;

                case 5:
                    // Show only resolved complaints
                    System.out.println("Resolved Complaints:\n");
                    cnt = 1;
                    for (Complaint complaint : Complaints) {
                        if (complaint.isResolved()) {  // Check if complaint is resolved
                            System.out.println(cnt + ". Date: " + complaint.getDate() + " - " + complaint.getMatter() + "\nSolution: " + complaint.getSolution() + "\n");
                            cnt++;
                        }
                    }
                    if (cnt == 1) {
                        System.out.println("No resolved complaints.\n");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void solve(Complaint complaint) {
        if (!complaint.isResolved()) {
            System.out.println("Do you wish to resolve complaint? (Y/n): ");
            String yes = sc.next();
            if (yes.equalsIgnoreCase("Y")) {
                System.out.println("\nEnter solution comment: ");
                sc.nextLine();  // Consume newline
                String comment = sc.nextLine();
                complaint.setSolution(comment);
                complaint.setResolved(true);
            }
        } else {
            System.out.println("Complaint already resolved: " + complaint.getSolution() + "\n");
        }
    }
    public Student getStudentByEmail(String email) {
        for (Student student : Students) {
            if (student.getEmail().equals(email)) {
                return student;
            }
        }
        return null;
    }

    public Professor getProfessorByEmail(String email) {
        for (Professor professor : Proflist) {
            if (professor.getEmail().equals(email)) {
                return professor;
            }
        }
        return null;
    }
    public ArrayList<Complaint> getComplaints() {
        return Complaints;
    }
    // Initialization methods
    private void initializeProflist() {
        Proflist.add(harry);
        Proflist.add(hermione);
        Proflist.add(dumbledore);
        Proflist.add(umbridge);
    }

    private void initializeStudents() {
        Students.add(s1);
        Students.add(s2);
        Students.add(s3);
    }

    private void initializeSemlist() {
        Semlist.add(Sem1);

        Sem1.add(la);
        Sem1.add(ip);
        Sem1.add(dc);
        Sem1.add(hci);
        Sem1.add(com);
        //pr1
        pr1.add(la);
        pr1.add(ip);
        //pr2
        pr2.add(la);
        //pr3
        pr3.add(la);
        pr3.add(dc);
        //pr45
        pr4.add(ip);
        //pr5
        pr5.add(ip);
        pr5.add(dc);
        pr5.add(com);

        Semlist.add(Sem2);
        Sem2.add(dsa);
        Sem2.add(pns);
        Sem2.add(ap);
        Sem2.add(be);
        Sem2.add(mb);
        Sem2.add(pde);
        Sem2.add(rando);
    }

    private void courseprofmap() {
        la.setProf(hermione);
        ip.setProf(harry);
        com.setProf(umbridge);
        dc.setProf(hermione);
        hci.setProf(umbridge);

        dsa.setProf(dumbledore);
        ap.setProf(harry);
        pde.setProf(hermione);
        mb.setProf(dumbledore);
        pns.setProf(harry);
        be.setProf(dumbledore);
        rando.setProf(umbridge);

        harry.getClist().add(ip);
        harry.getClist().add(ap);
        harry.getClist().add(pns);

        hermione.getClist().add(la);
        hermione.getClist().add(dc);
        hermione.getClist().add(pde);

        dumbledore.getClist().add(dsa);
        dumbledore.getClist().add(mb);
        dumbledore.getClist().add(be);

        umbridge.getClist().add(com);
        umbridge.getClist().add(hci);
        umbridge.getClist().add(rando);
    }

    // Overridden methods
    @Override
    protected String getEmail() {
        System.out.println("No email\n");
        return "";
    }
    @Override
    protected void setEmail(String id) {
        System.out.println("Not Allowed!\n");
    }


    // Hardcoded objects
    Professor harry = new Professor("harry", null, "Friday: 14.00");
    Professor hermione = new Professor("hermione", null, "Thursday: 15:00");
    Professor dumbledore = new Professor("dumbledore", null, "Wednesday: 20:00");
    Professor umbridge = new Professor("umbridge", null, "Monday: 8.30");

    Student s1 = new Student("divyanshi", "8178833188");
    Student s2 = new Student("percy", "Poseidon");
    Student s3 = new Student("anna", "Athena");
    ArrayList<Course>pre=new ArrayList<>();
    Course la = new Course(100, "LA", 4, pre, null, "Vector Spaces, Matrix Algebra, Linear Transformation, Eigenvalues, Orthogonality, SVD\n",
            "Monday: 9.30 - 11.00\nWednesday: 3.00 - 4.30\n", 300, 1);
    Course ip = new Course(101, "IP", 4, pre, null, "Python", "Monday: 4 - 6\nTuesday: 11.00 - 12.30\n", 300, 1);
    Course dc = new Course(200, "DC", 4, pre, null, "Binary Numbers\n", "Tuesday: 4.00 - 6.00\n", 300, 1);
    Course com = new Course(110, "COM", 4, pre, null, "Talking\n", "Thursday: 4.30 - 6.00\n", 300, 1);
    Course hci = new Course(205, "HCI", 4, pre, null, " Brain-rot\n", "Wednesday: 11.00 - 12.30\n", 300, 1);

    Course dsa = new Course(103, "DSA", 4, pr1, null, "Python", "Monday: 4 - 6\nTuesday: 11.00 - 12.30\n", 300, 2);
    Course pns = new Course(204, "PNS", 4, pr2, null, "Binary Numbers\n", "Tuesday: 4.00 - 6.00\n", 300, 2);
    Course be = new Course(300, "BE", 4, pr3, null, "Talking\n", "Thursday: 4.30 - 6.00\n", 300, 2);
    Course mb = new Course(120, "MB", 4, null, null, " Brainrot\n", "Wednesday: 11.00 - 12.30\n", 300, 2);
    Course ap = new Course(180, "AP", 4, pr4, null, "Python", "Monday: 4 - 6\nTuesday: 11.00 - 12.30\n", 300, 2);
    Course pde = new Course(250, "PDE", 4, null, null, "Binary Numbers\n", "Tuesday: 4.00 - 6.00\n", 300, 2);
    Course rando = new Course(160, "RANDO", 4, pr5, null, "Talking\n", "Thursday: 4.30 - 6.00\n", 300, 2);
}
