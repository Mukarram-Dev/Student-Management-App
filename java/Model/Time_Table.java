package Model;

public class Time_Table {
    String Day,Room,Teacher,Subject,StartTime,EndTime;

    Time_Table(){

    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public Time_Table(String day, String room, String teacher, String subject, String startTime, String endTime) {
        Day = day;
        Room = room;
        Teacher = teacher;
        Subject = subject;
        StartTime = startTime;
        EndTime = endTime;
    }
}
