import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DataRequired {

    public static void main(String[] args) {
        HashSet<Teacher> courses=new HashSet<>();
        Teacher t1=new Teacher(0,"A");
        Teacher t2=new Teacher(1,"A");
        courses.add(t1);
        courses.add(t2);

    }

}

class Course{
    String course_id;
    int total_capacity;
    String course_name;
    int course_hour;
    float course_credit;
    String course_departure;

    Course(String course_id,int total_capacity,String course_name,int course_hour,float course_credit,String course_departure){
        this.course_id=course_id;
        this.total_capacity=total_capacity;
        this.course_name=course_name;
        this.course_hour=course_hour;
        this.course_credit=course_credit;
        this.course_departure=course_departure;
        this.classes=new ArrayList<>();
    }

    //引用
    List<Class> classes;
}

class Class{
    String class_id;
    String class_name;

    Class(String class_name,Course course){
        this.class_name=class_name;
        this.class_id= course.course_id+class_name;
        this.course=course;
        class_info_list=new ArrayList<>();
        teachers=new ArrayList<>();
        course.classes.add(this);
    }

    //引用
    List<ClassList> class_info_list;
    Course course;
    List<Teacher> teachers;
}

class Teacher{
    int teacher_id;
    String names;
    @Override
    public boolean equals(Object obj){
        Teacher other=(Teacher) obj;
        if(other==null)return false;
        if(/*other.teacher_id!=this.teacher_id || */!other.names.equals(this.names))return false;
        return true ;
    }
    Teacher(int teacher_id,String name){
        this.teacher_id=teacher_id;
        this.names=name;
    }
    Teacher(String name){
        this.names = name;
    }

    @Override
    public int hashCode() {
//        return teacher_id+names.hashCode();
        return names.hashCode();
    }
}

//为了能通过class找到class_list, 将 classlist的引用放在class中，而不是像数据库那样反过来
class ClassList {
    //int[] week_list;
    String week_list;
    int week_day;
    String location;
    int start_time;
    int end_time;

    ClassList(ClassListRAW clw) {
//        week_list=clw.weekList;
        week_list = "";
        int maxWeek = clw.weekList[clw.weekList.length - 1];
        int index = 0;
        for (int i = 1; i <= maxWeek; i++) {
            if (clw.weekList[index] == i) {
                week_list = week_list.concat("1");
                index++;
            } else {
                week_list = week_list.concat("0");
            }
        }
        week_day = clw.weekday;
        location = clw.location;
        String[] class_time = clw.classTime.split("-");
        start_time = Integer.parseInt(class_time[0]);
        end_time = Integer.parseInt(class_time[1]);
    }

}

class Location{
    String name;
}

//TODO: the most important and difficult content
class Prerequisite {
    Course now_course;//当前的course
    long id;//当前语句id


}


class Student {
    String name;
    String gender;
    String college;
    String student_id;
    ArrayList<String> courses_done;

    Student(String[] info) {
        name = info[0];
        gender = info[1];
        college = info[2];
        student_id = info[3];
        courses_done = new ArrayList<>();
        for (int i = 4; i < info.length; i++) {
            courses_done.add(info[i]);
        }
    }


}
