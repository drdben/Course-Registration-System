import java.util.*;

public class Student extends User {
//    Scanner sc = new Scanner(System.in);

    private String contact;
    private ArrayList<Course> currentCourses = new ArrayList<>();
    private ArrayList<Course> completedCourses = new ArrayList<>();
    private Map <Course, Double> grades = new HashMap<Course, Double>();
    private int currentSemester;
    private Double SGPA;
    private Double CGPA;
    private ArrayList<Complaint> Complaints = new ArrayList<>();

    public Student(String id, String contact){
        this.setEmail(id);
        if(this.getPassword().isEmpty()) {
            setPassword("foo");
        }
        this.contact = contact;
        this.currentSemester = 1;
    };

    public String getContact() {
        return contact;
    }

    public void viewAvailableCourses(Admin admin) {
        System.out.println("Courses available for Semester " + currentSemester + ":");
        ArrayList<Course> courses = admin.getCoursesForSemester(currentSemester);
        for (Course course : courses) {
            System.out.println(course.getTitle() + " (" +course.getCode()+") - " + course.getProf().getEmail());
            System.out.println("1. View More Details? (1/0)? ");
            int yes = sc.nextInt();
            if(yes==1){
                System.out.println("\n1. Credits:"+ course.getCredits() + "\n2. Pre-Requisites: "+course.getPreReqs()+"\n3. Timings: "+course.getCourseSchedule()+"\n4. Syllabus"+course.getSyllabus());
            }
        }
    }
    //In main method, ask User whether they wish to register, if yes, ask for course code, ask first if they would like to view all available courses
    //REGISTER: the public method is called first, then the private one inside it(encapsulation)
    public void Register(Admin admin){
        System.out.println("Enter semester: ");
        int sem = sc.nextInt();
        if(sem!=currentSemester){
            do{System.out.println("Can't access semester! Try again: ");
                sem=sc.nextInt();
            }
            while(sem!=currentSemester);
        }
        System.out.println("Enter the code of the course you wish to register in sem " +sem+": ");
        int code= sc.nextInt();
        RegisterForCourse(admin, code);
    }
    private void RegisterForCourse(Admin admin, int code) {
        Course selectedCourse = admin.getCourseByCode(code, currentSemester);
        if (selectedCourse!=null && checkPrerequisites(selectedCourse)) {
            if (getTotalCredits() + selectedCourse.getCredits() <= 20) {
                currentCourses.add(selectedCourse);
                System.out.println("Successfully registered for " + selectedCourse.getTitle());
            } else {
                System.out.println("Cannot register. Exceeds 20 credit limit.");
            }
        }else{
            System.out.println("Cannot register. Either course not found or prerequisites not met.");
        }
    }
//Again, this will be called in main under main menu Functionalities:
    public void viewSchedule() {
        System.out.println("Weekly schedule:");
        for (Course course : currentCourses) {
            System.out.println(course.getTitle() + ": " + course.getCourseSchedule() +"taught by: " +course.getProf().getEmail() +" at: "+course.getLocation());
        }
    }
    //Under a main menu option of Track academic record are options: 1. Get CGPA 2. Get SGPA
    public void ChangeSem(Admin admin){
        System.out.println("Your current semester is: "+currentSemester+"\n");
        int cr=0;
        for(Course course: currentCourses){
            ArrayList<Course> sem = admin.getCoursesForSemester(currentSemester);
            if (sem.contains(course)){
                cr+=course.getCredits();
            }
        }
        if(cr>=16){
            currentCourses= new ArrayList<>();
            this.currentSemester++;
            System.out.println("Semester has been changed to: "+currentSemester);
        }
        else{
            System.out.println("Can't change semester as less than 16 credits have been obtained in current semester.");
        }
    }

    public void trackrecord(){
        boolean rec = true;
        for(Course course: currentCourses){
            if(!grades.containsKey(course)){
                System.out.println("Can't compute CGPA/SGPA as grades not awarded for all registered courses yet.\n");
                rec=false;
                break;
            }
        }
        if(rec){
            getGPA();
        }
    }
    public void getGPA() {
        //For current sem GPA:
        Double sempoints=0.0;
        Double semcreds = 0.0;
        for (Course course: currentCourses){
            Double grade = grades.get(course);
            if(!grades.containsKey(course)){
                sempoints += grade;
                semcreds+= course.getCredits();
            }
            else{
                System.out.println("Cant calculate sg because all grades have not been assigned for current sem.");
                return;
            }
        }
        if(semcreds==0){
            System.out.println("Cant calculate sg because all grades have not been assigned for current sem.");
            return;
        }
        SGPA = sempoints/semcreds ;
        System.out.println("SGPA: " + SGPA);

        //For cumulative GPA (all completed courses)
        System.out.println("Completed Courses and Grades:");
        Double points = 0.0;
        Double credits = 0.0;
        for (int i = 0; i < completedCourses.size(); i++) {
            Course course = completedCourses.get(i);
            Double grade = grades.get(course);
            System.out.println(course.getTitle() + " - Grade: " + grade);
            points += grade * course.getCredits();
            credits += course.getCredits();
        }
        CGPA = points / credits;
        System.out.println("CGPA: " + CGPA);
    }

    //Course dropping in Main menu (return to main menu options after break.):
    public void dropCourse(int courseCode) {
        if(currentCourses.isEmpty()){
            System.out.println("Nothing to drop! Try registering first lol\n");
            return;
        }
        for (Course course : currentCourses) {
            if (course.getCode() == courseCode) {
                currentCourses.remove(course);
                System.out.println("Successfully dropped " + course.getTitle());
                return;
            }
        }
        System.out.println("Can't drop what you aint got! (Not found)");
    }
    // COMPLAINT Filing - Main menu
    public void fileComplaint(Admin admin){
        System.out.println("Enter your grievance: ");
        String issue = sc.nextLine();
        Complaint complaint = new Complaint(issue);
        admin.getComplaints().add(complaint);
        Complaints.add(complaint);
        System.out.println("Filed Successfully!\n");
    }
    //view status
    public void viewComplaintStatus(){
        if(!Complaints.isEmpty()){
            for(Complaint complaint: Complaints){
                String solved="Pending";
                if(complaint.isResolved()){
                    solved="Resolved";
                }
                System.out.println("Your complaint: "+complaint.getMatter()+"\nComplaint Status: "+solved+"\n");
            }
            return;
        }
        System.out.println("None found. Bye.\n");
    }

    public ArrayList<Course> getCurrentCourses() {
        return currentCourses;
    }
    public ArrayList<Course> getCompletedCourses() {
        return completedCourses;
    }

    private boolean checkPrerequisites(Course course) {
        for (Course prereq : course.getPreReqs()) {
            if (!completedCourses.contains(prereq)) {
                System.out.println("Missing prerequisite: " + prereq.getTitle());
                return false;
            }
        }
        return true;
    }

    public Map<Course, Double> getGrades() {
        return grades;
    }
    public void setContact(String contact1){
        this.contact=contact1;
    }
    // Calculate total registered credits
    private int getTotalCredits() {
        int total = 0;
        for (Course course : currentCourses) {
            total += course.getCredits();
        }
        return total;
    }
}
