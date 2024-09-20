import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Admin admin = new Admin();

        while (true) {
            System.out.println("Welcome to IIITD ERP!");
            System.out.println("1. Login as Student");
            System.out.println("2. Login as Professor");
            System.out.println("3. Login as Admin");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
//            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    studentLogin(admin, sc);
                    break;
                case 2:
                    professorLogin(admin, sc);
                    break;
                case 3:
                    adminLogin(admin, sc);
                    break;
                case 4:
                    System.out.println("Thank you for using IIITD ERP. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void studentLogin(Admin admin, Scanner sc) {
        System.out.print("Enter student email: ");
        String email = sc.next();
        Student student = admin.getStudentByEmail(email);

        if (student != null) {
            if(student.getPassword().equals("foo")){
                System.out.println("1st login--> Please Enter new Password(!foo): ");
                String pass = sc.next();
                student.setPassword(pass);
            }
            student.login();
            if (student.getLogged()) {
                studentMenu(student, admin, sc);
            }
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void professorLogin(Admin admin, Scanner sc) {
        System.out.print("Enter professor email:\n ");
        String email = sc.next();
        Professor professor = admin.getProfessorByEmail(email);

        if (professor != null) {
            if(professor.getPassword().equals("foo")){
                System.out.println("1st login--> Please Enter new Password(!foo): ");
                String pass = sc.next();
                professor.setPassword(pass);
            }
            professor.login();
            if (professor.getLogged()) {
                professorMenu(professor, admin, sc);
            }
        } else {
            System.out.println("Professor not found.");
        }
    }

    private static void adminLogin(Admin admin, Scanner sc) {
        admin.login();
//        System.out.println(admin.getLogged());
        if (admin.getLogged()) {
            adminMenu(admin, sc);
        }
    }

    private static void studentMenu(Student student, Admin admin, Scanner sc) {
        while (true) {
            System.out.println("\nStudent Menu:");
            System.out.println("1. View Available Courses");
            System.out.println("2. Register for Courses");
            System.out.println("3. View Schedule");
            System.out.println("4. Track Academic Progress");
            System.out.println("5. Drop Course");
            System.out.println("6. Submit Complaint");
            System.out.println("7. View Complaints");
            System.out.println("8. Change Current Semester");
            System.out.println("9. Logout");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    student.viewAvailableCourses(admin);
                    break;
                case 2:
                    student.Register(admin);
                    break;
                case 3:
                    student.viewSchedule();
                    break;
                case 4:
                    student.trackrecord();
                    break;
                case 5:
                    System.out.print("Enter course code to drop: ");
                    int courseCode = sc.nextInt();
                    student.dropCourse(courseCode);
                    break;
                case 6:
                    student.fileComplaint(admin);
                    break;
                case 7:
                    student.viewComplaintStatus();
                    break;
                case 8:
                    student.ChangeSem(admin);
                    break;
                case 9:
                    student.setLogged(false);
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void professorMenu(Professor professor, Admin admin, Scanner sc) {
        boolean menu = true;
        while (menu) {
            System.out.println("\nProfessor Menu:");
            System.out.println("1. View & Modify Courses");
            System.out.println("2. Change Office Hours");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    professor.ProfMainMenu(admin);
                    break;
                case 2:
                    System.out.print("Enter new office hours: ");
                    String newOfficeHours = sc.nextLine();
                    professor.setOfficeHour(newOfficeHours);
                    System.out.println("Office hours updated successfully.");
                    break;
                case 3:
                    menu=false;
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void adminMenu(Admin admin, Scanner sc) {
        boolean menu=true;
        while (menu) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Manage Courses");
            System.out.println("2. Manage Students");
            System.out.println("3. Assign Professors");
            System.out.println("4. Access Complaints");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    admin.ManageCourses();
                    break;
                case 2:
                    admin.ManageStudents();
                    break;
                case 3:
                    admin.AssignProfessors();
                    break;
                case 4:
                    admin.AccessComplaints();
                    break;
                case 5:
                    menu=false;
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}