import java.lang.reflect.Array;
import java.util.*;

public class Course {
    private int code;
    private String title;
    private Professor prof;
    private int credits;
    private ArrayList<Course> preReqs;
    private ArrayList<Student> enrolled;
    private String syllabus;
    private String courseSchedule;
    private int cap;
    private String location;
    private int Semester;

    public Course(int no, String tt, int cr, ArrayList<Course>pre, Professor px, String syl, String sched, int limit, int semester){
        this.code=no;
        this.title=tt;
        this.credits=cr;
        this.preReqs=pre;
        this.prof=px;
        this.syllabus=syl;
        this.courseSchedule= sched;
        this.cap=limit;
        this.enrolled=new ArrayList<>();
        this.location="Dungeons\n";
        this.Semester=semester;
    }

    public ArrayList<Student> getEnrolled() {
        return enrolled;
    }
    public int getSemester(){
        return Semester;
    }
    public int getCap() {
        return cap;
    }
    public String getLocation() {
        return location;
    }
    public int getCode() {
        return code;
    }
    public int getCredits() {
        return credits;
    }
    public Professor getProf() {
        return prof;
    }
    public ArrayList<Course> getPreReqs() {
        return preReqs;
    }
    public String getTitle() {
        return title;
    }
    public String getSyllabus() {
        return syllabus;
    }
    public String getCourseSchedule() {
        return courseSchedule;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public void setCourseSchedule(String courseSchedule) {
        this.courseSchedule = courseSchedule;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }
    public void setPreReqs(ArrayList<Course> preReqs) {
        this.preReqs = preReqs;
    }
    public void setProf(Professor prof) {
        this.prof = prof;
    }
    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCap(int cap) {
        this.cap = cap;
    }
}
