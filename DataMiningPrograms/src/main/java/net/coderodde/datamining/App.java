package net.coderodde.datamining;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.coderodde.datamining.loader.support.DataLoaderv1;
import net.coderodde.datamining.model.AppDataStorage;
import net.coderodde.datamining.model.Course;
import net.coderodde.datamining.model.Student;
import static net.coderodde.datamining.utils.Utils.intersect;

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
//        app.printCourseNames();
//        app.printProgrammingCourses();
//        app.printAmountOfIntroProgrammingStudents();
//        app.printAmountOfIntroProgrammingStudentsThatPassed();
//        app.printAmountOfAdvancedProgrammingStudentsThatPassed();
//        app.printAmountOfDatasturcuteStudentsThatPassedWith45();
//        app.printAmountOfStudentsThatPassedBasicProgrammingCourses();
//        app.printAmountOfStudentsWithThreeCourses();
//        app.printAmountOfStudentsWithThreeCoursesWithCoolGrades();
//        app.printSupportForProgrammingCourses();
//        app.printHowManyContinueToAdvancedProgramming();
//        app.printSupportOfBasicToAdvancedProgrammingCourse();
//        app.printProbabilityFromBasicToAdvanced();
//        app.printPercentageOfDataStructureStudents();
//        app.printConfidenceBasicToAdvancedProgrammingCourse();
//        app.printDifferentUniqueCourses();
//        app.printCourseTwoCombinations();
//        app.printCourseThreeCombinations();
//        app.printCourseFiveCombinations();
        app.printOldCourseGradePairs();
        app.printNewCourseGradePairs();
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
    
    private void printHowManyContinueToAdvancedProgramming() {
        final List<Student> basicStudentList = 
                appData.getStudentsByCourseName("Ohjelmoinnin perusteet");
        
        final Set<Student> set1 = 
                new HashSet<>(
                        appData
                        .getStudentsByCourseName("Ohjelmoinnin jatkokurssi"));
        
        final Set<Student> set2 =
                new HashSet<>(
                appData
                .getStudentsByCourseName("Java-ohjelmointi"));
        
        int count = 0;
        
        for (final Student student : basicStudentList) {
            if (set1.contains(student) || set2.contains(student)) {
                ++count;
            }
        }
        
        System.out.println(
                "The amount of students having basic programming course and " +
                "continuing to advanced course: " + count);
    }
    
    private void printSupportOfBasicToAdvancedProgrammingCourse() {
        final Course basicCourse = 
                appData.getCourseByName("Ohjelmoinnin perusteet");
        final Course advancedCourse1 = 
                appData.getCourseByName("Ohjelmoinnin jatkokurssi");
        final Course advancedCourse2 =
                appData.getCourseByName("Java-ohjelmointi");
        
        final Set<Course> x = new HashSet<>();
        x.add(basicCourse);
        
        final Set<Course> y = new HashSet<>();
        y.add(advancedCourse1);
        
        System.out.println(
                "Ohjelmoinnin perusteet -> Ohjelmoinnin jatkokurssi: " +
                appData.support(x, y));
        
        y.clear();
        y.add(advancedCourse2);
        
        System.out.println(
                "Ohjelmoinnin perusteet -> Java-ohjelmointi: " +
                appData.support(x, y));
    }
    
    private void printProbabilityFromBasicToAdvanced() {
        final List<Student> basicStudentList = 
                appData.getStudentsByCourseName("Ohjelmoinnin perusteet");
        
        final Set<Student> set1 = 
                new HashSet<>(
                        appData
                        .getStudentsByCourseName("Ohjelmoinnin jatkokurssi"));
        
        final Set<Student> set2 =
                new HashSet<>(
                appData
                .getStudentsByCourseName("Java-ohjelmointi"));
        
        int count = 0;
        
        for (final Student student : basicStudentList) {
            if (set1.contains(student) || set2.contains(student)) {
                ++count;
            }
        }
        
        System.out.println(
                "The percentage of students continuing from basic to " + 
                "advanced programming course: " + 
                        (1.0 * count / basicStudentList.size()));
        
    }
    
    private void printPercentageOfDataStructureStudents() {
        // Find all the students that took both basic and advanced programming
        // courses.
        
        final Set<Student> basicSet = 
                new HashSet<>(
                        appData
                        .getStudentsByCourseName("Ohjelmoinnin perusteet"));
        
        final Set<Student> advancedSet1 = 
                new HashSet<>(
                        appData
                        .getStudentsByCourseName("Ohjelmoinnin jatkokurssi"));
        
        final Set<Student> advancedSet2 = 
                new HashSet<>(
                        appData
                        .getStudentsByCourseName("Java-ohjelmointi"));
        
        final Iterator<Student> iterator = basicSet.iterator();
        
        while (iterator.hasNext()) {
            final Student student = iterator.next();
            
            if (!advancedSet1.contains(student) 
                    && !advancedSet2.contains(student)) {
                iterator.remove();
            }
        }
        
        // Now basicSet contains all the students that have done both basic
        // and advanced programming courses.
        final Set<Student> dsSet = 
                new HashSet<>(
                    appData
                    .getStudentsByCourseName("Tietorakenteet ja algoritmit"));
        
        int count = 0;
        
        for (final Student student : basicSet) {
            if (dsSet.contains(student)) {
                ++count;
            }
        }
        
        System.out.println(
            "Percentage of students with both programming courses that " +
            "continue to Data structures: " + (1.0 * count / basicSet.size()));
    }
    
    private void printConfidenceBasicToAdvancedProgrammingCourse() {
        final Course basicCourse = 
                appData.getCourseByName("Ohjelmoinnin perusteet");
        
        final Course advancedCourse1 = 
                appData.getCourseByName("Ohjelmoinnin jatkokurssi");
        
        final Course advancedCourse2 =
                appData.getCourseByName("Java-ohjelmointi");
        
        final Set<Course> setx = new HashSet<>();
        setx.add(basicCourse);
        
        final Set<Course> sety = new HashSet<>();
        sety.add(advancedCourse1);
        
        System.out.println(
                "Confidence of (Ohjelmoinnin perusteet -> " +
                "Ohjelmoinnin jatkokurssi): " + appData.confidence(setx, sety));
        
        sety.clear();
        sety.add(advancedCourse2);
        
        System.out.println(
                "Confidence of (Ohjelmoinnin perusteet -> " +
                "Java-ohjelmointi): " + appData.confidence(setx, sety));
    }
    
    private void printDifferentUniqueCourses() {
        final Set<Course> set = new LinkedHashSet<>(appData.getCourseList());
        
        for (final Course course : set) {
            System.out.println(course);
        }
    }
    
    private void printCourseTwoCombinations() {
        final List<Course> list = appData.getCourseList();
        
        final long ta = System.currentTimeMillis();
        
        for (int index1 = 0; index1 < list.size(); ++index1) {
            for (int index2 = index1 + 1; index2 < list.size(); ++index2) {
                
            }
        }
        
        final long tb = System.currentTimeMillis();
        
        System.out.println(
                "Duration for 2-combinations: " + (tb - ta) + " ms.");
    }
    
    private void printCourseThreeCombinations() {
        final List<Course> list = appData.getCourseList();
        
        final long ta = System.currentTimeMillis();
        
        for (int index1 = 0; index1 < list.size(); ++index1) {
            for (int index2 = index1 + 1; index2 < list.size(); ++index2) {
                for (int index3 = index2 + 1; index3 < list.size(); ++index3) {
                    
                }
            }
        }
        
        final long tb = System.currentTimeMillis();
        
        System.out.println(
                "Duration for 3-combinations: " + (tb - ta) + " ms.");
    }
    
    private void printCourseFiveCombinations() {
        final List<Course> list = appData.getCourseList();
        
        final int N = list.size();
        final long ta = System.currentTimeMillis();
        
        for (int index1 = 0; index1 < N; ++index1) {
            for (int index2 = index1 + 1; index2 < N; ++index2) {
                for (int index3 = index2 + 1; index3 < N; ++index3) {
                    for (int index4 = index3 + 1; index4 < N; ++index4) {
                        
                    }
                }
            }
        }
        
        final long tb = System.currentTimeMillis();
        
        System.out.println(
                "Duration for 4-combinations: " + (tb - ta) + " ms.");
    }

    private void printCourseNames() {
        for (final Course course : appData.getCourseList()) {
            System.out.println(course);
        }
    }
    
    private void printProgrammingCourses() {
        for (final Course course : appData.getCourseList()) {
            if (course.getName().contains("hjelmoin") 
                    || course.getName().contains("rogramming")
                    || course.getName().contains("lgoritmi")
                    || course.getName().contains("lgorithm")) {
                System.out.println(course);
            }
        }
    }
    
    /**
     * This method is responsible for printing the grade pairs of the basic and
     * advanced programming courses, both according to the old curriculum.
     */
    private void printOldCourseGradePairs() {
        System.out.println("--- Old grade pairs ---");
        
        final Course basicCourse = 
                appData.getCourseByName("Ohjelmoinnin perusteet");
        
        final Course advancedCourse =
                appData.getCourseByName("Ohjelmoinnin jatkokurssi");
        
        final Set<Student> basicStudentSet = 
                new HashSet<>(appData.getStudentsUntil(basicCourse, 2009, 12));
        
        final Set<Student> advStudentSet =
                new HashSet<>(appData.getStudentsUntil(advancedCourse, 
                                                       2009, 
                                                       12));
        
        System.out.println("Total students at basic course: " + 
                           basicStudentSet.size());
        
        System.out.println("Total students at advanced course: " +
                           advStudentSet.size());
        
        final Set<Student> intersection = intersect(basicStudentSet,
                                                    advStudentSet);
        
        System.out.println("Total students at both courses: " + 
                           intersection.size());
        
        final StringBuilder sb = new StringBuilder();
        int index = 0;
        
        for (final Student student : intersection) {
            if (index == 0) {
                sb.append("[[");
            } else {
                sb.append(" [");
            }
            
            sb.append(appData.grade(student, basicCourse))
              .append(", ")
              .append(appData.grade(student, advancedCourse));
            
            if (index++ < intersection.size() - 1) {
                sb.append("],\n");
            } else {
                sb.append("]]");
            }
        }
        
        System.out.println("Data:");
        System.out.println(sb.toString());
    }
    
    /**
     * This method is responsible for printing the grade pairs of the basic and
     * advanced programming courses, both according to the new curriculum.
     */
    private void printNewCourseGradePairs() {
        System.out.println("--- New grade pairs ---");
        
        final Course basicCourse = 
                appData.getCourseByName("Ohjelmoinnin perusteet");
        
        final Course advancedCourse =
                appData.getCourseByName("Ohjelmoinnin jatkokurssi");
        
        final Set<Student> basicStudentSet = 
                new HashSet<>(appData.getStudentsFrom(basicCourse, 2010, 1));
        
        final Set<Student> advStudentSet =
                new HashSet<>(appData.getStudentsFrom(advancedCourse, 
                                                      2010, 
                                                      1));
        
        System.out.println("Total students at basic course: " + 
                           basicStudentSet.size());
        
        System.out.println("Total students at advanced course: " +
                           advStudentSet.size());
        
        final Set<Student> intersection = intersect(basicStudentSet,
                                                    advStudentSet);
        
        System.out.println("Total students at both courses: " + 
                           intersection.size());
        
        final StringBuilder sb = new StringBuilder();
        int index = 0;
        
        for (final Student student : intersection) {
            if (index == 0) {
                sb.append("[[");
            } else {
                sb.append(" [");
            }
            
            sb.append(appData.grade(student, basicCourse))
              .append(", ")
              .append(appData.grade(student, advancedCourse));
            
            if (index++ < intersection.size() - 1) {
                sb.append("],\n");
            } else {
                sb.append("]]");
            }
        }
        
        System.out.println("Data:");
        System.out.println(sb.toString());
    }
}
