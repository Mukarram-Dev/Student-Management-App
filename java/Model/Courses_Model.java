package Model;

public class Courses_Model {

    String Course_name;
    int Credit_Hours;

    public Courses_Model(){

    }


    public Courses_Model(String course_name, int credit_Hours) {
        Course_name = course_name;
        Credit_Hours = credit_Hours;
    }

    public String getCourse_name() {
        return Course_name;
    }

    public void setCourse_name(String course_name) {
        Course_name = course_name;
    }

    public int getCredit_Hours() {
        return Credit_Hours;
    }

    public void setCredit_Hours(int credit_Hours) {
        Credit_Hours = credit_Hours;
    }
}
