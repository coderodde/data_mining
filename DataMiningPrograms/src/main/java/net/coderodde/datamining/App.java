package net.coderodde.datamining;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.coderodde.datamining.loader.support.DataLoaderv1;
import net.coderodde.datamining.model.AppDataStorage;
import net.coderodde.datamining.model.Course;
import net.coderodde.datamining.model.Student;

/**
 * This class defines the entry point of the program.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class App {
    
    private static final String HELP = 
            "usage: java -jar program.jar <file>\n" + 
            "  where <file> is the path to the data file.\n";
    
    private AppDataStorage appData;
    
    public App(final AppDataStorage appData) {
        this.appData = appData;
    }
    
    public static void main(final String... args) {
        if (args.length == 0) {
            System.out.println(HELP);
            System.exit(1);
        }
        
        final AppDataStorage appData = 
                new DataLoaderv1().load(new File(args[0]));
        
        final App app = new App(appData);
        
//        app.printAllCourseCodes();
        app.printAmountOfIntroProgrammingStudents();
        app.printAmountOfIntroProgrammingStudentsThatPassed();
        app.printAmountOfAdvancedProgrammingStudentsThatPassed();
        app.printAmountOfDatasturcuteStudentsThatPassedWith45();
        app.printAmountOfStudentsThatPassedBasicProgrammingCourses();
        app.printAmountOfStudentsWithThreeCourses();
        app.printAmountOfStudentsWithThreeCoursesWithCoolGrades();
        app.printSupportForProgrammingCourses();
    }

    private void printAllCourseCodes() {
        for (final Course course : appData.getCourseList()) {
            System.out.println(course.getCode());
        }
    }
    
    private void printAmountOfIntroProgrammingStudents() {
        final List<Student> studentList = 
                appData.getStudentsByCourseName("Ohjelmoinnin perusteet");
        
        System.out.println("The amount of students attaining " +
                           "\"Ohjelmoinnin perusteet\": " +
                           studentList.size());
    }
    
    private void printAmountOfIntroProgrammingStudentsThatPassed() {
        final Course course = 
                appData.getCourseByName("Ohjelmoinnin perusteet");
        
        final List<Student> studentList = 
                appData.getStudentsByCourseName(course.getName());
        
        int passed = 0;
        
        for (final Student student : studentList) {
            if (appData.passed(student, course)) {
                ++passed;
            }
        }
        
        System.out.println("The amount of students attaining \"Ohjelmoinnin " +
                           "perusteet\" that passed: " + passed);
    }
    
    private void printAmountOfAdvancedProgrammingStudentsThatPassed() {
        final Course course1 = 
                appData.getCourseByName("Ohjelmoinnin jatkokurssi");
        final Course course2 =
                appData.getCourseByName("Java-ohjelmointi");
        
        final Set<Student> set = new HashSet<>();
        set.addAll(appData.getStudentsByCourseName(course1.getName()));
        set.addAll(appData.getStudentsByCourseName(course2.getName()));
        
        int passed = 0;
        
        for (final Student student : set) {
            if (appData.passed(student, course1) 
                    || appData.passed(student, course2)) {
                ++passed;
            }
        }
        
        System.out.println("The amount of students that passed " + 
                           "\"Advanced programming\": " + passed);
    }
    
    private void printAmountOfDatasturcuteStudentsThatPassedWith45() {
        final Course course = 
                appData.getCourseByName("Tietorakenteet ja algoritmit");
        
        final List<Student> studentList = 
                appData.getStudentsByCourseName(course.getName());
        
        int amount = 0;
        
        for (final Student student : studentList) {
            if (appData.passed(student, course) 
                    && appData.grade(student, course) >= 4) {
                ++amount;
            }
        }
        
        System.out.println(
                "The amount of students attaining \"" + course.getName() +
                "\" that passed with grade 4 or 5: " + amount);
    }
    
    private void printAmountOfStudentsThatPassedBasicProgrammingCourses() {
        final Course course1 = 
                appData.getCourseByName("Ohjelmoinnin perusteet");
        
        final Course course2a = 
                appData.getCourseByName("Ohjelmoinnin jatkokurssi");
        
        final Course course2b =
                appData.getCourseByName("Java-ohjelmointi");
        
        final List<Student> studentList1 = 
                appData.queryStudents(course1, 1, 5);
                
        final List<Student> studentList2a =
                appData.queryStudents(course2a, 1, 5);
        
        final List<Student> studentList2b = 
                appData.queryStudents(course2b, 1, 5);
        
        final Set<Student> set = 
                new HashSet<>(Math.max(studentList1.size(),
                                       studentList2a.size() + 
                                       studentList2b.size()));
        
        set.addAll(studentList1);
        
        final Set<Student> set2 = new HashSet<>();
        
        set2.addAll(studentList2a);
        set2.addAll(studentList2b);
        
        final Set<Student> toRemove = new HashSet<>();
        
        for (final Student student : set) {
            if (!set2.contains(student)) {
                toRemove.add(student);
            }
        }
        
        set.removeAll(toRemove);
        
        System.out.println("The amount of students that have passed both " +
                           "basic and advanced programming courses: " + 
                           set.size());
    }
    
    /**
     * Prints the amount of students that have passed "Introduction to
     * Programming", "Advanced Programming" and "Data structures".
     */
    private void printAmountOfStudentsWithThreeCourses() {
        final Course course1 = 
                appData.getCourseByName("Ohjelmoinnin perusteet");
        
        final Course course2a = 
                appData.getCourseByName("Ohjelmoinnin jatkokurssi");
        
        final Course course2b = 
                appData.getCourseByName("Java-ohjelmointi");
        
        final Course course3 = 
                appData.getCourseByName("Tietorakenteet ja algoritmit");
        
        final List<Student> studentList1 = 
                appData.queryStudents(course1, 1, 5);
        
        final List<Student> studentList2a = 
                appData.queryStudents(course2a, 1, 5);
        
        final List<Student> studentList2b = 
                appData.queryStudents(course2b, 1, 5);
        
        final List<Student> studentList3 = 
                appData.queryStudents(course3, 1, 5);
        
        final Set<Student> studentSet1 = new HashSet<>(studentList1.size());
        
        final Set<Student> studentSet2 = new HashSet<>(studentList2a.size() +
                                                       studentList2b.size());
        
        final Set<Student> studentSet3 = new HashSet<>(studentList3.size());
        
        studentSet1.addAll(studentList1);
        studentSet2.addAll(studentList2a);
        studentSet2.addAll(studentList2b);
        studentSet3.addAll(studentList3);
        
        final Set<Student> finalSet = new HashSet<>();
        finalSet.addAll(studentSet2);
        
        final Iterator<Student> it = finalSet.iterator();
        
        while (it.hasNext()) {
            final Student s = it.next();
            
            if (!studentSet1.contains(s) || !studentSet3.contains(s)) {
                it.remove();
            }
        }
        
        System.out.println("The amount of students that passed both " +
                           "programming courses and the \"Data structures\": " + 
                           finalSet.size());
    }
    
    /**
     * Prints the amount of students that have passed "Introduction to
     * Programming", "Advanced Programming" and "Data structures", all with
     * cool grades of 4 or 5.
     */
    private void printAmountOfStudentsWithThreeCoursesWithCoolGrades() {
        final Course course1 = 
                appData.getCourseByName("Ohjelmoinnin perusteet");
        
        final Course course2a = 
                appData.getCourseByName("Ohjelmoinnin jatkokurssi");
        
        final Course course2b = 
                appData.getCourseByName("Java-ohjelmointi");
        
        final Course course3 = 
                appData.getCourseByName("Tietorakenteet ja algoritmit");
        
        final List<Student> studentList1 = 
                appData.queryStudents(course1, 4, 5);
        
        final List<Student> studentList2a = 
                appData.queryStudents(course2a, 4, 5);
        
        final List<Student> studentList2b = 
                appData.queryStudents(course2b, 4, 5);
        
        final List<Student> studentList3 = 
                appData.queryStudents(course3, 4, 5);
        
        final Set<Student> studentSet1 = new HashSet<>(studentList1.size());
        
        final Set<Student> studentSet2 = new HashSet<>(studentList2a.size() +
                                                       studentList2b.size());
        
        final Set<Student> studentSet3 = new HashSet<>(studentList3.size());
        
        studentSet1.addAll(studentList1);
        studentSet2.addAll(studentList2a);
        studentSet2.addAll(studentList2b);
        studentSet3.addAll(studentList3);
        
        final Set<Student> finalSet = new HashSet<>();
        finalSet.addAll(studentSet2);
        
        final Iterator<Student> it = finalSet.iterator();
        
        while (it.hasNext()) {
            final Student s = it.next();
            
            if (!studentSet1.contains(s) || !studentSet3.contains(s)) {
                it.remove();
            }
        }
        
        System.out.println("The amount of students that passed both " + 
                           "programming courses and the \"Data structures\" " +
                           "with grades 4 or 5: " + 
                           finalSet.size());
    }
    
    private void printSupportForProgrammingCourses() {
        final List<Student> basicStudentList = 
                appData.getStudentsByCourseName("Ohjelmoinnin perusteet");
        final List<Student> advancedStudentList1 =
                appData.getStudentsByCourseName("Ohjelmoinnin jatkokurssi");
        final List<Student> advancedStudentList2 =
                appData.getStudentsByCourseName("Java-ohjelmointi");
        
        final Set<Student> set = new HashSet<>(basicStudentList);
        final Set<Student> seta1 = new HashSet<>(advancedStudentList1);
        final Set<Student> seta2 = new HashSet<>(advancedStudentList2);
        
        final Iterator<Student> iterator = set.iterator();
        
        while (iterator.hasNext()) {
            final Student s = iterator.next();
            
            if (!seta1.contains(s) && !seta2.contains(s)) {
                iterator.remove();
            }
        }
        
        System.out.println("Support of programming courses: " +
                           (1.0f * set.size() / appData.getStudentAmount()));
    }
    
    private void countHowManyContinueToAdvancedProgramming() {
        final List<Student> basicStudentList = 
                appData.getStudentsByCourseName("Ohjelmoinnin perusteet");
        
        final Set<Course> courseSet = new HashSet<>();
        courseSet.add(appData.getCourseByName("Ohjelmoinnin jatkokurssi"));
        courseSet.add(appData.getCourseByName("Java-ohjelmointi"));
        
        final List<Student> advancedStudentList =
                appData.queryStudents(courseSet);
        
        final Set<Student> advancedStudentSet = new HashSet<>();
        advancedStudentSet.addAll(advancedStudentList);
        
        int count = 0;
        
        for (final Student student : basicStudentList) {
            if (advancedStudentSet.contains(student)) {
                ++count;
            }
        }
        
        System.out.println(
                "The amount of students having basic programming course and ");
    }
}
