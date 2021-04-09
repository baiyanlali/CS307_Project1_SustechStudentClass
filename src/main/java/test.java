public class test {
    public static void main(String[] args) {
        String str= "(大学物理A(上) ".replaceAll("\\(|\\)|\\s+","");
        System.out.println(str);
    }
}
