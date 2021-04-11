import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileQuery {
    public static void main(String[] args) throws IOException {
        inJson();
    }

    static void q1() throws IOException {
        System.out.println("Start to search student count of 阿兹卡班");
        long startTime=System.currentTimeMillis();
        JwxtParser.parseStudent();
        long cnt=0;
        for (Student s:JwxtParser.students){
            if(s.college.equals("阿兹卡班(Azkaban)"))
                cnt++;
        }
        System.out.println("Student in Azkaban:"+cnt);
        System.out.println(String.format("Use %d ms time",System.currentTimeMillis()-startTime));
    }

    static void q2() throws IOException {
        System.out.println("Start to search the students with the same college of ZhouGongZhou");
        long startTime=System.currentTimeMillis();
        JwxtParser.parseStudent();
        String name="周工周";
        ArrayList<Student>[] students=new ArrayList[5];
        for (int i = 0; i < 5; i++) {
            students[i]=new ArrayList<>();
        }
        int index=-1;
        for (Student s:JwxtParser.students){
            if(s.name.equals(name)){
                switch (s.college){
                    case "阿兹卡班(Azkaban)":
                        index=0;
                        break;
                    case "斯莱特林(Slytherin)":
                        index=1;
                        break;
                    case"拉文克劳(Ravenclaw)":
                        index=2;
                        break;
                    case"格兰芬多(Gryffindor)":
                        index=3;
                        break;
                    case"赫奇帕奇(Hufflepuff)":
                        index=4;
                        break;
                }
            }
            switch (s.college){
                case "阿兹卡班(Azkaban)":
                    students[0].add(s);
                    break;
                case "斯莱特林(Slytherin)":
                    students[1].add(s);
                    break;
                case"拉文克劳(Ravenclaw)":
                    students[2].add(s);
                    break;
                case"格兰芬多(Gryffindor)":
                    students[3].add(s);
                    break;
                case"赫奇帕奇(Hufflepuff)":
                    students[4].add(s);
                    break;
            }
        }
        for (Student s:students[index]){
            System.out.println(s.toString());
        }
        System.out.println(String.format("Use %d ms time",System.currentTimeMillis()-startTime));
    }

    static void q3() throws IOException {
        System.out.println("Start to search the students learned database");
        long startTime=System.currentTimeMillis();
        JwxtParser.parseCourseJson();
        JwxtParser.parseCourseRAW();
        JwxtParser.parseStudent();
        String name="数据库原理";
        String course_id="";
        for (Course c:JwxtParser.courseHashMap.values()) {
            if(c.course_name.equals(name)){
                course_id=c.course_id;
                break;
            }
        }

        for (Student s:JwxtParser.students){
            for (String cc:s.courses_done){
                if(cc.equals(course_id)){
                    System.out.println(s.toString());
                }
            }
        }

        System.out.println(String.format("Use %d ms time",System.currentTimeMillis()-startTime));
    }

    static void q4() throws IOException{
        System.out.println("Start to find the class required");
        long startTime=System.currentTimeMillis();
        JwxtParser.parseCourseJson();
        JwxtParser.parseCourseRAW();
        JwxtParser.parseStudent();
        ArrayList<String> courses=new ArrayList<>();
        for (Course c:JwxtParser.courseHashMap.values()) {
            if(c.classes.size()>=3 && c.course_departure.equals("计算机科学与工程系"))
                courses.add(c.course_id);
        }
        HashMap<String,Long> course_count = new HashMap<>();
        Long max_cnt=0l;
        String max_str="";
        for (Student s:JwxtParser.students) {
            for (String c:courses) {

                Long tmp=course_count.get(c);
                if(tmp==null)tmp=0l;
                if(s.courses_done.contains(c))
                    course_count.put(c,tmp+1);
                if(tmp+1>max_cnt){
                    max_cnt=tmp+1;
                    max_str=c;
                }
            }
        }
        System.out.println(max_str);
        System.out.println(String.format("Use %d ms time",System.currentTimeMillis()-startTime));
    }

    static void inJson() throws IOException{
        System.out.println("Input json");
        long startTime=System.currentTimeMillis();
        JwxtParser.parseCourseJson();
        JwxtParser.parseCourseRAW();
        System.out.println(String.format("Use %d ms time",System.currentTimeMillis()-startTime));
        System.out.println("Input csv");
        startTime=System.currentTimeMillis();
        JwxtParser.parseStudent();
        System.out.println(String.format("Use %d ms time",System.currentTimeMillis()-startTime));

    }

}
