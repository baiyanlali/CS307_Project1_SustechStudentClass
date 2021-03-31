import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class JwxtParser {
    public static List<CourseRAW> courses;

    static DatabaseConnnect databaseConnnect;
    static HashMap<String, Course> courseHashMap;
    static ArrayList<Class> classes;
    static HashSet<Teacher> teachers;
    static HashMap<String,Teacher> teacherHashMap;
    static ArrayList<Student> students;

    // Run as: jshell JwxtParser.java <json file>
    public static void main(String[] args) throws IOException {

        Path path = Path.of(args[0]);
//        System.out.println(path.isAbsolute());
//        System.out.println(path.getRoot());
        String content = Files.readString(path);
        content = content.replaceAll("）",")");
        content = content.replaceAll("）","(");
        Gson gson = new Gson();
        courses = gson.fromJson(content, new TypeToken<List<CourseRAW>>() {
        }.getType());




        parseCourseRAW();
//        putJWXTinData();


        parseStudent();
        putStudentIntoData();


    }

    public static void parseStudent() throws IOException {
        students=new ArrayList<>();
        File student_info = new File("src/main/java/data/select_course.csv");
        BufferedReader reader = null;
        try {
            String one_student = null;
            reader = new BufferedReader(new FileReader(student_info));
            while ((one_student = reader.readLine()) != null) {
//                System.out.println(one_student);
                String[] s_info = one_student.split(",");
                Student student = new Student(s_info);
                students.add(student);
            }
        } catch (Exception e) {

        } finally {
            reader.close();
        }
    }
    public static void parseCourseRAW() {
        courseHashMap = new HashMap<>();
        classes = new ArrayList<>();
        teachers = new HashSet<>();
        teacherHashMap=new HashMap<>();
        HashSet<Location> locations = new HashSet<>();
        for (CourseRAW course_raw : courses) {
            //Course info_去重
            if (!courseHashMap.containsKey(course_raw.courseName.trim())) {
                Course co = new Course(course_raw.courseId.trim(),
                        course_raw.totalCapacity,
                        course_raw.courseName.trim(),
                        course_raw.courseHour,
                        course_raw.courseCredit,
                        course_raw.courseDept);
                courseHashMap.put(co.course_name, co);
            }
            //Class info 不需要去重
            Class clAss = new Class(course_raw.className.trim(), courseHashMap.get(course_raw.courseName.trim()));
//            clAss.class_info_list.addAll(course_raw.classList);
            for (ClassListRAW cl : course_raw.classList) {
                ClassList classList = new ClassList(cl);
                clAss.class_info_list.add(classList);
            }
            if(course_raw.teacher!=null){

                if(course_raw.teacher.contains(",")){
                    String[] manyTeacher = course_raw.teacher.split(",");
                    for (String oneTeacher : manyTeacher) {

                        Teacher teacher = new Teacher(oneTeacher.trim());
                        teachers.add(teacher);
                        teacherHashMap.put(teacher.names,teacher);
                        clAss.teachers.add(teacher);
                    }
                }
                else{
                    Teacher teacher = new Teacher(course_raw.teacher.trim());
                    clAss.teachers.add(teacher);
                }
            }

        }
    }
    public static void putJWXTinData(){
        databaseConnnect = new DatabaseConnnect("jdbc:postgresql://localhost:5432/CS307_SustechStudentClass",
                "byll",
                "123456");

        for (Course c:courseHashMap.values()) {
            DatabaseConnnect.SendToDataBase(c);

        }

        for (Teacher teacher:teachers){
            DatabaseConnnect.SendToDataBase(teacher);
        }

        for (Class c:classes){
            DatabaseConnnect.SendToDataBase(c);
            DatabaseConnnect.SendToDataBase(c,1);
            DatabaseConnnect.SendToDataBase(c.course,c);
            for (ClassList classList:c.class_info_list){
                DatabaseConnnect.SendToDataBase(classList,c);
            }
        }




        DatabaseConnnect.CloseConnection();
    }

    static void putStudentIntoData() {
        databaseConnnect = new DatabaseConnnect("jdbc:postgresql://localhost:5432/CS307_SustechStudentClass",
                "byll",
                "123456");

        for (Student s:students){
            DatabaseConnnect.SendToDataBase(s);
        }

        DatabaseConnnect.CloseConnection();
    }

}

class CourseRAW {
    // TODO:prerequisite question
    public int totalCapacity;
    public String courseId;
    public String prerequisite;
    public String teacher;
    public ClassListRAW[] classList;
    public int courseHour;
    public float courseCredit;
    public String courseName;
    public String courseDept;
    public String className;
}

class ClassListRAW {
    public int[] weekList;
    public String location;
    public String classTime;
    public int weekday;

}


