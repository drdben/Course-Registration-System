import java.lang.reflect.Array;
import java.util.ArrayList;

public class Professor extends User implements CourseModifier {

    private ArrayList<Course> clist;
    private String OfficeHour;

    public Professor(String id, ArrayList<Course> cl, String oh){
        this.setEmail(id);
        if(this.getPassword().isEmpty()) {
            setPassword("foo");
        }
        this.clist = (cl == null) ? new ArrayList<>() : cl;
        this.OfficeHour=oh;
    };

    public String getOfficeHour() {
        return OfficeHour;
    }
    public ArrayList<Course> getClist() {
        return clist;
    }
    public void setOfficeHour(String officeHour) {
        OfficeHour = officeHour;
    }
    //Should call in main after Prof login:
    public void ProfMainMenu(Admin admin){
        boolean main_menu=true;
        while (main_menu){
            System.out.println("Would you like to:\n1. View & Modify Courses \n2. Change office Hours\n 3. Award Grades 4. Logout\n");
            int choice = sc.nextInt();
            switch(choice){
                case 1:
                    for(Course course: clist){
                        System.out.println(course.getTitle()+" "+course.getCode()+": "+course.getCourseSchedule());
                        System.out.println("View More Details / Modify? (Y/n)");
                        String yes = sc.next();
                        if(yes.equals("Y")){
                            System.out.println("At: "+course.getLocation()+"Credits: "+course.getCredits()+"Syllabus: "+course.getSyllabus());
                            boolean further = true;
                            while(further){
                                System.out.println("""
                                        1. Modify Course Details
                                        2. Change Pre-Requisites
                                        3. View List of Enrolled Students
                                        4.Back to Main Menu
                                        """);
                                int view = sc.nextInt();

                                switch(view){
                                    case 1:
                                        ModifyCourse(course);
                                        break;
                                    case 2:
                                        System.out.println("Note that Pre Requisites must be chosen from the following list:\n ");
                                        int s = course.getSemester();
                                        if(s==1){
                                            System.out.println("Can not Have Pre Requisites for 1st semester courses.\n");
                                            break;
                                        }
                                        else{
                                            for (int j=1;j<s;j++){
                                                ArrayList<Course> tmp = admin.getCoursesForSemester(j);
                                                for(Course precourse: tmp){
                                                    System.out.println(course.getTitle()+" Code: "+course.getCode()+"\n");
                                                }
                                            }
                                            System.out.println("Specify number of pre requisites to keep for this course(>=0): ");
                                            int num = sc.nextInt();
                                            ArrayList<Course> pre = new ArrayList<>();
                                            if(num==0){
                                                course.setPreReqs(pre);
                                            }
                                            else{
                                                for(int k=0;k<num;k++){
                                                    System.out.println("Enter course code "+k+": ");
                                                    int code = sc.nextInt();
                                                    System.out.println("\nEnter course semester: ");
                                                    int sem = sc.nextInt();
                                                    Course prereq = admin.getCourseByCode(code,sem);
                                                    pre.add(prereq);
                                                }
                                                course.setPreReqs(pre);
                                            }
                                            break;
                                        }
                                    case 3:
                                        System.out.println("\n");
                                        ArrayList<Student>temp = course.getEnrolled();
                                        if(temp.isEmpty()){
                                            System.out.println("None registered yet!\n");
                                        }
                                        for(Student student: temp){
                                            System.out.println(student.getEmail()+", Contact: "+ student.getContact()+"\nGPA: ");
                                            student.trackrecord(); System.out.println("\n");
                                        }
                                    case 4:
                                        further=false;
                                        break;
                                };
                            }
                        }
                    }
                    break;
                case 2:
                    System.out.println("current office hours: " +getOfficeHour());
                    System.out.println("Enter new office hours: ");
                    String oh = sc.next();
                    this.setOfficeHour(oh);
                    System.out.println("new office hours set to: "+getOfficeHour());
                    break;
                case 3:

                case 4:
                    main_menu=false;
                    break;
            }
        }
        return;
    }

    public void awardGrades(Course course) {
        System.out.println("Awarding grades for course: " + course.getTitle());
        ArrayList<Student> enrolledStudents = course.getEnrolled();

        if (enrolledStudents.isEmpty()) {
            System.out.println("No students enrolled in this course.");
            return;
        }
        for (Student student : enrolledStudents) {
            System.out.println("Enter grade point (0-10): ");
            double grade = sc.nextDouble();
            student.getGrades().put(course, grade);
            student.getCompletedCourses().add(course);
        }

    }
    @Override
    public void ModifyCourse(Course course) {
        boolean modify = true;
        while(modify){
            System.out.println("""
                Which of the following fields would you like to modify?
                1. Course Syllabus
                2. Course Timings
                3. Credits
                4. Enrollment Limit
                5. Back
                """);
            int func = sc.nextInt();
            switch (func){
                case 1:
                    System.out.println("enter new syllabus:\n");
                    String new_syl = sc.nextLine();
                    course.setSyllabus(new_syl);
                    break;

                case 2:
                    System.out.println("enter new timings:\n");
                    String new_time = sc.nextLine();
                    course.setCourseSchedule(new_time);
                    break;

                case 3:
                    System.out.println("enter Credits:\n");
                    int new_cr = sc.nextInt();
                    course.setCredits(new_cr);
                    break;

                case 4:
                    System.out.println("enter enrollment limit:\n");
                    int lim = sc.nextInt();
                    course.setCap(lim);
                    break;
                case 5:
                    modify=false;
                    break;
            }
        }

    }
}