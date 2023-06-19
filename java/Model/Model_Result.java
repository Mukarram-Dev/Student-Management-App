package Model;

public class Model_Result {
    public Model_Result(String marks, String grade, String course, double gpa) {
        this.marks = marks;
        this.grade = grade;
        this.course = course;
        this.gpa = (double) gpa;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    String marks,grade,course;
    double gpa;
}
