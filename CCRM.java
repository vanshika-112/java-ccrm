import java.util.*;
import java.time.LocalDate;

public class CCRM {
    private static CCRM instance;
    private final Map<String, Student> students = new HashMap<>();
    private final Map<String, Course> courses = new HashMap<>();
    private final Map<String, Enrollment> enrollments = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);
    
    // Grade system
    public enum Grade {
        A(9.0, "A"), B(8.0, "B"), C(6.0, "C"), D(4.0, "D"), F(2.0, "F"),
        IN_PROGRESS(0.0, "In Progress");
        
        public final double points;
        public final String displayName;
        
        Grade(double points, String displayName) {
            this.points = points;
            this.displayName = displayName;
        }
    }
    
    // Semester system
    public enum Semester {
        FALL("Fall"), WINTER("Winter");
        
        public final String displayName;
        
        Semester(String displayName) {
            this.displayName = displayName;
        }
    }
    
    // Student class
    public static class Student {
        public String id;
        public String fullName;
        public String email;
        public String course;
        public LocalDate joinDate;
        
        public Student(String id, String fullName, String email, String course) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.course = course;
            this.joinDate = LocalDate.now();
        }
    }
    
    // Course class
    public static class Course {
        public String code;
        public String name;
        public String description;
        public int credits;
        public int maxStudents;
        
        public Course(String code, String name, String description, int credits, int maxStudents) {
            this.code = code;
            this.name = name;
            this.description = description;
            this.credits = credits;
            this.maxStudents = maxStudents;
        }
    }
    
    // Enrollment class (links students to courses)
    public static class Enrollment {
        public String studentId;
        public String courseCode;
        public Semester semester;
        public Grade grade;
        
        public Enrollment(String studentId, String courseCode, Semester semester) {
            this.studentId = studentId;
            this.courseCode = courseCode;
            this.semester = semester;
            this.grade = Grade.IN_PROGRESS;
        }
        
        // Create a unique key for this enrollment
        public String getKey() {
            return studentId + "-" + courseCode + "-" + semester.name();
        }
    }
    
    // Get the single instance of our manager
    public static CCRM getInstance() {
        if (instance == null) {
            instance = new CCRM();
        }
        return instance;
    }
    
    private CCRM() {
        setupSampleData();
    }
    
    // Add some sample data to start with
    private void setupSampleData() {
        // Add sample students
        addStudent(new Student("101", "Ram", "Ram@xyz.edu", "Computer Science"));
        addStudent(new Student("102", "Siya", "Siya@xyz.edu", "Mathematics"));
        addStudent(new Student("103", "Dove", "Dove@xyz.edu", "Biology"));
        
        // Add sample courses
        addCourse(new Course("CS101", "Intro to Programming", "Learn basic coding skills", 3, 30));
        addCourse(new Course("MATH201", "Calculus I", "Differential calculus", 4, 25));
        addCourse(new Course("BIO101", "Biology Fundamentals", "Introduction to biology", 3, 35));
        addCourse(new Course("ENG101", "English Composition", "Academic writing", 3, 40));
        
        // Enroll some students
        enrollStudent("201", "CS101", Semester.FALL);
        enrollStudent("201", "MATH201", Semester.FALL);
        enrollStudent("202", "BIO101", Semester.WINTER);
        enrollStudent("203", "ENG101", Semester.WINTER);
        
        // Add some grades
        recordGrade("S001", "CS101", Semester.FALL, Grade.A);
        recordGrade("S001", "MATH201", Semester.FALL, Grade.B);
    }
    
    // Basic operations
    public void addStudent(Student student) {
        students.put(student.id, student);
    }
    
    public void addCourse(Course course) {
        courses.put(course.code, course);
    }
    
    public boolean enrollStudent(String studentId, String courseCode, Semester semester) {
        if (!students.containsKey(studentId)) {
            System.out.println("Student not found!");
            return false;
        }
        if (!courses.containsKey(courseCode)) {
            System.out.println("Course not found!");
            return false;
        }
        
        Enrollment enrollment = new Enrollment(studentId, courseCode, semester);
        enrollments.put(enrollment.getKey(), enrollment);
        System.out.println("Student enrolled successfully!");
        return true;
    }
    
    public boolean recordGrade(String studentId, String courseCode, Semester semester, Grade grade) {
        String key = studentId + "-" + courseCode + "-" + semester.name();
        Enrollment enrollment = enrollments.get(key);
        
        if (enrollment == null) {
            System.out.println("Enrollment not found!");
            return false;
        }
        
        enrollment.grade = grade;
        System.out.println("Grade recorded: " + grade.displayName);
        return true;
    }
    
    // User interface methods
    public void start() {
        System.out.println("Welcome to Campus Course & Records Manager!");
        
        while (true) {
            showMainMenu();
            int choice = readNumber("Choose an option: ");
            
            switch (choice) {
                case 1 -> manageStudents();
                case 2 -> manageCourses();
                case 3 -> manageEnrollments();
                case 4 -> manageGrades();
                case 5 -> showTranscripts();
                case 6 -> showReports();
                case 0 -> {
                    System.out.println("Thank you for using Campus Manager. Goodbye!");
                    return;
                }
                default -> System.out.println("Please choose a valid option (0-7)");
            }
        }
    }
    
    private void showMainMenu() {
        System.out.println("\nMAIN MENU");
        System.out.println("1.Student Management");
        System.out.println("2.Course Management");
        System.out.println("3.Enrollment Management");
        System.out.println("4.Grade Management");
        System.out.println("5.Student Transcripts");
        System.out.println("6.File Operations");
        System.out.println("7.Reports & Statistics");
        System.out.println("0.Exit");
    }
    
    // Student management
    private void manageStudents() {
        while (true) {
            System.out.println("\n STUDENT MANAGEMENT");
            System.out.println("1. Add New Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("0. Back to Main Menu");
            
            int choice = readNumber("Choose an option: ");
            
            switch (choice) {
                case 1 -> addNewStudent();
                case 2 -> showAllStudents();
                case 3 -> searchStudents();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }
    
    private void addNewStudent() {
        System.out.println("\n ADD NEW STUDENT");
        String id = readText("Student ID: ");
        
        if (students.containsKey(id)) {
            System.out.println("This student ID already exists!");
            return;
        }
        
        String fullName = readText("Full Name: ");
        String email = readText("Email: ");
        String course = readText("Course: ");
        
        Student student = new Student(id, fullName, email, course);
        addStudent(student);
        System.out.println("Student added successfully!");
    }
    
    private void showAllStudents() {
        System.out.println("\nALL STUDENTS");
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        for (Student student : students.values()) {
            System.out.printf("%s (%s) - %s - %s%n", 
                student.fullName, student.id, student.course, student.email);
        }
    }
    
    private void searchStudents() {
        String search = readText("Enter student name or ID to search: ").toLowerCase();
        System.out.println("\n SEARCH RESULTS:");
        
        boolean found = false;
        for (Student student : students.values()) {
            if (student.fullName.toLowerCase().contains(search) || 
                student.id.toLowerCase().contains(search)) {
                System.out.printf(" %s (%s) - %s%n", 
                    student.fullName, student.id, student.course);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No students found matching your search.");
        }
    }
    
    // Course management
    private void manageCourses() {
        while (true) {
            System.out.println("\nCOURSE MANAGEMENT");
            System.out.println("1. Add New Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Search Course");
            System.out.println("0. Back to Main Menu");
            
            int choice = readNumber("Choose an option: ");
            
            switch (choice) {
                case 1 -> addNewCourse();
                case 2 -> showAllCourses();
                case 3 -> searchCourses();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }
    
    private void addNewCourse() {
        System.out.println("\nADD NEW COURSE");
        String code = readText("Course Code: ");
        
        if (courses.containsKey(code)) {
            System.out.println("This course code already exists!");
            return;
        }
        
        String name = readText("Course Name: ");
        String description = readText("Description: ");
        int credits = readNumber("Credits: ");
        int maxStudents = readNumber("Maximum Students: ");
        
        Course course = new Course(code, name, description, credits, maxStudents);
        addCourse(course);
        System.out.println("Course added successfully!");
    }
    
    private void showAllCourses() {
        System.out.println("\n ALL COURSES");
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        
        for (Course course : courses.values()) {
            System.out.printf("%s: %s (%d credits, %d seats)%n", 
                course.code, course.name, course.credits, course.maxStudents);
        }
    }
    
    private void searchCourses() {
        String search = readText("Enter course code or name to search: ").toLowerCase();
        System.out.println("\nSEARCH RESULTS:");
        
        boolean found = false;
        for (Course course : courses.values()) {
            if (course.code.toLowerCase().contains(search) || 
                course.name.toLowerCase().contains(search)) {
                System.out.printf("%s: %s - %s%n", 
                    course.code, course.name, course.description);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No courses found matching your search.");
        }
    }
    
    // Enrollment management
    private void manageEnrollments() {
        while (true) {
            System.out.println("\nENROLLMENT MANAGEMENT");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. View All Enrollments");
            System.out.println("0. Back to Main Menu");
            
            int choice = readNumber("Choose an option: ");
            
            switch (choice) {
                case 1 -> enrollStudentInteractive();
                case 2 -> showAllEnrollments();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }
    
    private void enrollStudentInteractive() {
        System.out.println("\n ENROLL STUDENT");
        showAllStudents();
        String studentId = readText("Enter Student ID: ");
        
        showAllCourses();
        String courseCode = readText("Enter Course Code: ");
        
        System.out.println("Select Semester:");
        for (int i = 0; i < Semester.values().length; i++) {
            System.out.printf("%d. %s%n", i + 1, Semester.values()[i].displayName);
        }
        
        int semesterChoice = readNumber("Choose semester: ") - 1;
        if (semesterChoice < 0 || semesterChoice >= Semester.values().length) {
            System.out.println("Invalid semester choice!");
            return;
        }
        
        Semester semester = Semester.values()[semesterChoice];
        enrollStudent(studentId, courseCode, semester);
    }
    
    private void showAllEnrollments() {
        System.out.println("\nALL ENROLLMENTS");
        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found.");
            return;
        }
        
        for (Enrollment enrollment : enrollments.values()) {
            Student student = students.get(enrollment.studentId);
            Course course = courses.get(enrollment.courseCode);
            
            if (student != null && course != null) {
                System.out.printf("%s → %s (%s) - Grade: %s%n", 
                    student.fullName, course.name, enrollment.semester.displayName, 
                    enrollment.grade.displayName);
            }
        }
    }
    
    // Grade management
    private void manageGrades() {
        System.out.println("\nGRADE MANAGEMENT");
        showAllEnrollments();
        
        String studentId = readText("Enter Student ID: ");
        String courseCode = readText("Enter Course Code: ");
        
        System.out.println("Select Semester:");
        for (int i = 0; i < Semester.values().length; i++) {
            System.out.printf("%d. %s%n", i + 1, Semester.values()[i].displayName);
        }
        
        int semesterChoice = readNumber("Choose semester: ") - 1;
        if (semesterChoice < 0 || semesterChoice >= Semester.values().length) {
            System.out.println("Invalid semester choice!");
            return;
        }
        
        Semester semester = Semester.values()[semesterChoice];
        
        System.out.println("Select Grade:");
        Grade[] validGrades = {Grade.A, Grade.B, Grade.C, Grade.D, Grade.F};
        for (int i = 0; i < validGrades.length; i++) {
            System.out.printf("%d. %s%n", i + 1, validGrades[i].displayName);
        }
        
        int gradeChoice = readNumber("Choose grade: ") - 1;
        if (gradeChoice < 0 || gradeChoice >= validGrades.length) {
            System.out.println("Invalid grade choice!");
            return;
        }
        
        recordGrade(studentId, courseCode, semester, validGrades[gradeChoice]);
    }
    
    // Transcript system
    private void showTranscripts() {
        System.out.println("\nSTUDENT TRANSCRIPT");
        showAllStudents();
        String studentId = readText("Enter Student ID: ");
        
        Student student = students.get(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        // Find all enrollments for this student
        List<Enrollment> studentEnrollments = new ArrayList<>();
        for (Enrollment enrollment : enrollments.values()) {
            if (enrollment.studentId.equals(studentId)) {
                studentEnrollments.add(enrollment);
            }
        }
        
        if (studentEnrollments.isEmpty()) {
            System.out.println("No courses found for this student.");
            return;
        }
        
        // Calculate GPA
        double totalPoints = 0;
        int totalCredits = 0;
        
        System.out.println("\nOFFICIAL TRANSCRIPT");
        System.out.println("Student: " + student.fullName);
        System.out.println("ID: " + student.id);
        System.out.println("Course: " + student.course);
        System.out.println("\nCourses:");
        
        for (Enrollment enrollment : studentEnrollments) {
            Course course = courses.get(enrollment.courseCode);
            if (course != null && enrollment.grade != Grade.IN_PROGRESS) {
                System.out.printf("%s: %s (%d credits) - %s%n", 
                    course.code, course.name, course.credits, enrollment.grade.displayName);
                
                totalPoints += enrollment.grade.points * course.credits;
                totalCredits += course.credits;
            }
        }
        
        double gpa = totalCredits > 0 ? totalPoints / totalCredits : 0.0;
        System.out.printf("GPA: %.2f%n", gpa);
    }
    
    // Reports and statistics
    private void showReports() {
        System.out.println("\nCAMPUS STATISTICS");
        System.out.println("Total Students: " + students.size());
        System.out.println("Total Courses: " + courses.size());
        System.out.println("Total Enrollments: " + enrollments.size());
        
        // Enrollment by semester
        Map<Semester, Integer> semesterCounts = new HashMap<>();
        for (Enrollment enrollment : enrollments.values()) {
            semesterCounts.put(enrollment.semester, 
                semesterCounts.getOrDefault(enrollment.semester, 0) + 1);
        }
        
        System.out.println("\nEnrollments by Semester:");
        for (Semester semester : Semester.values()) {
            int count = semesterCounts.getOrDefault(semester, 0);
            System.out.printf("  %s: %d enrollments%n", semester.displayName, count);
        }
        
        // Most popular courses
        System.out.println("\nCourse Enrollment Counts:");
        Map<String, Integer> courseCounts = new HashMap<>();
        for (Enrollment enrollment : enrollments.values()) {
            courseCounts.put(enrollment.courseCode, 
                courseCounts.getOrDefault(enrollment.courseCode, 0) + 1);
        }
        
        for (Map.Entry<String, Integer> entry : courseCounts.entrySet()) {
            Course course = courses.get(entry.getKey());
            if (course != null) {
                System.out.printf("  %s: %d students%n", course.name, entry.getValue());
            }
        }
    }
    
    // Helper methods for user input
    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int readNumber(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    // Main method to start the application
    public static void main(String[] args) {
        CCRM manager = CCRM.getInstance();
        manager.start();
    }
}