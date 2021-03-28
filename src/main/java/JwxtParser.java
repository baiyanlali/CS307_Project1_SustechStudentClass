import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class JwxtParser {
    public static List<CourseRAW> courses;
    // Run as: jshell JwxtParser.java <json file>
    public static void main(String[] args) throws IOException {
//        Path path = Path.of("src/main/java/data/course_info.json");
        Path path = Path.of(args[0]);
        System.out.println(path.isAbsolute());
        System.out.println(path.getRoot());
        String content = Files.readString(path);
        Gson gson = new Gson();
        courses = gson.fromJson(content, new TypeToken<List<CourseRAW>>(){}.getType());
        parseCourseRAW();
    }

    public static void parseCourseRAW(){
        HashMap<String,Course> courseHashMap=new HashMap<>();
        ArrayList<Class> classes=new ArrayList<>();
        HashSet<Teacher> teachers=new HashSet<>();
        int teacher_index=0;
        HashSet<Location> locations=new HashSet<>();
        for (CourseRAW course_raw:courses) {
            //Course info_去重
            if(!courseHashMap.containsKey(course_raw.courseName.trim())){
                Course co=new Course(   course_raw.courseId.trim(),
                        course_raw.totalCapacity,
                        course_raw.courseName.trim(),
                        course_raw.courseHour,
                        course_raw.courseCredit,
                        course_raw.courseDept);
                courseHashMap.put(co.course_name,co);
            }
            //Class info 不需要去重
            Class clAss=new Class(course_raw.className.trim(), courseHashMap.get(course_raw.courseName.trim()));
//            clAss.class_info_list.addAll(course_raw.classList);
            for (ClassListRAW cl:course_raw.classList) {
                ClassList classList=new ClassList(cl);
                clAss.class_info_list.add(classList);
            }
            String[] manyTeacher = course_raw.teacher.split(",");
            for (String oneTeacher:manyTeacher) {

                Teacher teacher=new Teacher(oneTeacher);
                if(!teachers.contains(teacher)){
                    teachers.add(teacher);
                }
                clAss.teachers.add(teacher);
            }

        }
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


