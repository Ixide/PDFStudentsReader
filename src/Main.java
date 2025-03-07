import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

class Student {
    String number;
    String ictCode;
    String fullName;
    int score;
    String universityCode;

    public Student(String number, String ictCode, String fullName, int score, String universityCode) {
        this.number = number;
        this.ictCode = ictCode;
        this.fullName = fullName;
        this.score = score;
        this.universityCode = universityCode;
    }
}

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "4eburek777";

    public static void main(String[] args) {
        String inputFilePath = "C:/Users/Ixit/IdeaProjects/DPFReader/src/grants.pdf";
        try {
            PDDocument document = Loader.loadPDF(new File(inputFilePath));
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            document.close();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Введите код специальности (или пустую строку для выхода): ");
                String specialtyCode = scanner.nextLine().trim();

                if (specialtyCode.isEmpty()) {
                    break;
                }

                List<Student> students = extractStudents(text, specialtyCode);
                students.sort(Comparator.comparing(s -> s.universityCode));

                if (students.isEmpty()) {
                    System.out.println("Студенты с кодом " + specialtyCode + " не найдены.");
                } else {
                    insertStudentsIntoDB(students);
                    System.out.println("Данные записаны в БД!");
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка с файлом: " + e.getMessage());
        }
    }

    private static List<Student> extractStudents(String text, String specialtyCode) {
        List<Student> students = new ArrayList<>();
        String[] lines = text.split("\n");
        boolean specialtyFound = false;
        Pattern studentPattern = Pattern.compile("^(\\d+) (\\d+) (.+?) (\\d+) (\\d+)$");

        String number = "", ictCode = "", fullName = "", universityCode = "";
        int score = 0;
        boolean capturingName = false;

        if (!text.contains(specialtyCode)) {
            System.out.println("Код специальности " + specialtyCode + " не найден в файле!");
            return students;
        }

        for (String line : lines) {
            line = line.trim();
            if (line.contains(specialtyCode)) {
                specialtyFound = true;
                System.out.println("Найден код специальности: " + line);
                continue;
            }
            if (specialtyFound) {
                Matcher matcher = studentPattern.matcher(line);
                if (matcher.matches()) {
                    if (!fullName.isEmpty()) {
                        students.add(new Student(number, ictCode, fullName.trim(), score, universityCode));
                    }
                    number = matcher.group(1);
                    ictCode = matcher.group(2);
                    fullName = matcher.group(3);
                    score = Integer.parseInt(matcher.group(4));
                    universityCode = matcher.group(5);
                    capturingName = true;
                } else if (capturingName) {
                    fullName += " " + line;
                } else if (line.matches("B\\d{3}.*")) {
                    break;
                }
            }
        }

        if (!fullName.isEmpty()) {
            students.add(new Student(number, ictCode, fullName.trim(), score, universityCode));
        }

        System.out.println("Обнаружено студентов: " + students.size());
        return students;
    }

    private static void insertStudentsIntoDB(List<Student> students) {
        String sql = "INSERT INTO students (number, ict_code, full_name, score, university_code) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Student student : students) {
                pstmt.setString(1, student.number);
                pstmt.setString(2, student.ictCode);
                pstmt.setString(3, student.fullName);
                pstmt.setInt(4, student.score);
                pstmt.setString(5, student.universityCode);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("Ошибка записи в БД: " + e.getMessage());
        }
    }
}



