package net.coderodde.datamining;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import net.coderodde.datamining.loader.support.DataLoaderv1;
import net.coderodde.datamining.model.AppDataStorage;
import net.coderodde.datamining.model.AppDataStorage.SequenceAndSupport;
import net.coderodde.datamining.model.AssociationRule;
import net.coderodde.datamining.model.Course;
import net.coderodde.datamining.model.Sequence;
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
//        app.printOldCourseGradePairs();
//        app.printNewCourseGradePairs();
//        app.printTwoCourseCombinationsWithSupportOver03();
        
        // Brute-force.
//        app.printTwoCourseCombinationsWithSupportOver03();
//        app.printThreeCourseCombinationsWithSupportOver0175();
//        app.printFourCourseCombinationsWithSupportOver01();
       
        // Apriori.
//        app.printTwoCourseCombinationsWithSupportOver03Apriori();
//        app.printThreeCourseCombinationsWithSupportOver0175Apriori();
//        app.printFourCourseCombinationsWithSupportOver01Apriori();
//        app.printFiveCourseCombinationsWithSupportOver01Apriori();
        
//        app.interactiveSupportCounter();
//        app.printLargestItemsetWithSupportOver005();
        
//        app.printTwoCourseCombinationsWithNonzeroSupport();
//        app.printNewCourseGradePairs();
//        app.printCandidateSequences();
        
         ////////////////
        //// WEEK 3 ////
       ////////////////
        
//        app.printWeek3Task16();
//        app.printWeek3Task17();
//        app.printWeek3Task18();
        
//        app.printWeek3Task17();
//        app.printWeek3Task17WithMaxspan();
        
//        app.printWeek3Task18WithMaxspan();
        
          ////////////////
         //// WEEK 4 ////
        ////////////////
        
        app.printWeek4Task14();
    }

    private void interactiveSupportCounter() {
        final Scanner scanner = new Scanner(System.in);
        
        for (;;) {
            final String command = scanner.nextLine().trim();
            
            if (command.equals("quit")) {
                return;
            }
            
            final double support = Double.parseDouble(command);
            final long ta = System.currentTimeMillis();
            final Set<Set<Course>> frequentItemsets = appData.apriori(support);
            final long tb = System.currentTimeMillis();
            
            for (final Set<Course> itemset : frequentItemsets) {
                System.out.println(itemset);
            }
            
            System.out.println("Time elapsed: " + (tb - ta) + " ms.");
        }
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
        
        System.out.println("N: " + N);
        
        for (int index1 = 0; index1 < N; ++index1) {
            for (int index2 = index1 + 1; index2 < N; ++index2) {
                for (int index3 = index2 + 1; index3 < N; ++index3) {
                    for (int index4 = index3 + 1; index4 < N; ++index4) {
                        for (int index5 = index4 + 1; index5 < N; ++index5) {
                            
                        }
                    }
                }
            }
        }
        
        final long tb = System.currentTimeMillis();
        
        System.out.println(
                "Duration for 5-combinations: " + (tb - ta) + " ms.");
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
        
        System.out.println(basicCourse);
        System.out.println(advancedCourse);
        
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
        
        final StringBuilder sb1 = new StringBuilder();
        index = 0;
        
        for (final Student student : intersection) {
            sb.append("{");
            sb.append(appData.grade(student, basicCourse))
              .append(", ")
              .append(appData.grade(student, advancedCourse));
            sb.append("},\n");
        }
        
        System.out.println("Java-data:");
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
        
        final StringBuilder sb1 = new StringBuilder();
        index = 0;
        
        for (final Student student : intersection) {
            sb.append("{");
            sb.append(appData.grade(student, basicCourse))
              .append(", ")
              .append(appData.grade(student, advancedCourse));
            sb.append("},\n");
        }
        
        System.out.println("Java-data:");
        System.out.println(sb.toString());
    }
    
    private void printTwoCourseCombinationsWithNonzeroSupport() {
        final List<Course> courseList = appData.getCourseList();
        final Set<Course> workSet = new HashSet<>(2);
        final int N = courseList.size();
        int count = 0;
        
        for (int index1 = 0; index1 < N; ++index1) {
            final Course a = courseList.get(index1);
            workSet.add(a);
            
            for (int index2 = index1 + 1; index2 < N; ++index2) {
                final Course b = courseList.get(index2);
                workSet.add(b);
                
                if (appData.support(workSet, 
                                    Collections.<Course>emptySet()) > 0.0) {
                    ++count;
                } 
                
                workSet.remove(b);
            }
            
            workSet.remove(a);
        }
        
        // Produced 15198 combinations in 2:42.
        System.out.println(
                "2-course combinations with nonzero support: " + count);
    }
    
    private void printTwoCourseCombinationsWithSupportOver03Apriori() {
        final long ta = System.currentTimeMillis();
        final Set<Set<Course>> frequentItemsets = appData.apriori(0.3);
        final List<List<Course>> outputList = new ArrayList<>();
        
        for (final Set<Course> itemset : frequentItemsets) {
            if (itemset.size() == 2) {
                outputList.add(new ArrayList<>(itemset));
            }
        }
        
        final long tb = System.currentTimeMillis();
        System.out.println("Total frequent itemsets with support >= 0.3: " + 
                           frequentItemsets.size());
        System.out.println("Found " + outputList.size() + " 2-combinations " + 
                           "in " + (tb - ta) + " ms.");
    }
    
    private void printTwoCourseCombinationsWithSupportOver03() {
        final long ta = System.currentTimeMillis();
        
        final Map<Course, Map<Course, Integer>> supportMatrix =
                appData.getSupportMatrix();
        
        final List<Course> courseList = appData.getCourseList();
        final int N = courseList.size();
        final int ROWS = appData.getStudentAmount();
        final List<List<Course>> outputList = new ArrayList<>();
        
        for (int index1 = 0; index1 < N; ++index1) {
            final Course a = courseList.get(index1);
            
            for (int index2 = index1 + 1; index2 < N; ++index2) {
                final Course b = courseList.get(index2);
                
                if ((1.0 * supportMatrix.get(a).get(b)) / ROWS > 0.3) {
                    final List<Course> combination = new ArrayList<>(2);
                    combination.add(a);
                    combination.add(b);
                    outputList.add(combination);
                }
            }
        }
        
        final long tb = System.currentTimeMillis();
        
        // Produced 3 combinations in 2:35.
        System.out.println(
                "2-course combinations with support > 0.3: " + 
                outputList.size() + " in " + (tb - ta) + " ms.");
    }
    
    private void printLargestItemsetWithSupportOver005() {
        final long ta = System.currentTimeMillis();
        final Set<Set<Course>> frequentItemsets = appData.apriori(0.05);
        final long tb = System.currentTimeMillis();
        
        System.out.println("Found in " + (tb - ta) + " ms.");
        
        final List<Set<Course>> largestItemsets = new ArrayList<>();
        int largestSize = 0;
        
        for (final Set<Course> itemset : frequentItemsets) {
            if (largestSize < itemset.size()) {
                largestSize = itemset.size();
                largestItemsets.clear();
                largestItemsets.add(itemset);
            } else if (largestSize == itemset.size()) {
                largestItemsets.add(itemset);
            }
        }
        
        for (final Set<Course> itemset : largestItemsets) {
            System.out.println(itemset);
        }
        
        System.out.println("Largest size: " + largestSize);
    }
    
    private void printFourCourseCombinationsWithSupportOver01() {
        final long ta = System.currentTimeMillis();
        
        final Map<Course, Map<Course, Map<Course, Map<Course, Integer>>>> map =
                appData.getSupport4DMatrix();
        
        final List<Course> courseList = appData.getCourseList();
        final int N = courseList.size();
        final int ROWS = appData.getCourseAmount();
        final List<List<Course>> outputList = new ArrayList<>();
        
        for (int i1 = 0; i1 < N; ++i1) {
            final Course c1 = courseList.get(i1);
            final Map<Course, Map<Course, Map<Course, Integer>>> submap 
                    = map.get(c1);
            
            for (int i2 = i1 + 1; i2 < N; ++i2) {
                final Course c2 = courseList.get(i2);
                final Map<Course, Map<Course, Integer>> submap2 
                        = submap.get(c2);
                
                for (int i3 = i2 + 1; i3 < N; ++i3) {
                    final Course c3 = courseList.get(i3);
                    final Map<Course, Integer> submap3 = submap2.get(c3);
                    
                    for (int i4 = i3 + 1; i4 < N; ++i4) {
                        final Course c4 = courseList.get(i4);
                        
                        if ((1.0 * submap3.get(c4)) / ROWS > 0.1) {
                            final List<Course> combination 
                                    = new ArrayList<>(4);
                            combination.add(c1);
                            combination.add(c2);
                            combination.add(c3);
                            combination.add(c4);
                            outputList.add(combination);
                        }
                    }
                }
            }
        }
        
        final long tb = System.currentTimeMillis();
        
        System.out.println(
                "4-combinations with support > 0.1: " + outputList.size() + 
                " in " + (tb - ta) + " ms.");
    }
    
    private void printThreeCourseCombinationsWithSupportOver0175() {
        final long ta = System.currentTimeMillis();
        
        final Map<Course, Map<Course, Map<Course, Integer>>> supportCube = 
                appData.getSupportCube();
        
        final List<Course> courseList = appData.getCourseList();
        final int N = courseList.size();
        final int ROWS = appData.getStudentAmount();
        final List<List<Course>> outputList = new ArrayList<>();
        
        for (int index1 = 0; index1 < N; ++index1) {
            final Course a = courseList.get(index1);
            
            for (int index2 = index1 + 1; index2 < N; ++index2) {
                final Course b = courseList.get(index2);
                
                for (int index3 = index2 + 1; index3 < N; ++index3) {
                    final Course c = courseList.get(index3);
                    
                    if ((1.0 * supportCube.get(a).get(b).get(c)) / ROWS > 0.175) {
                        final List<Course> combination = new ArrayList<>(3);
                        combination.add(a);
                        combination.add(b);
                        combination.add(c);
                        outputList.add(combination);
                    }
                }
            }
        }
        
        final long tb = System.currentTimeMillis();
        
        System.out.println(
                "3-course combinations with support > 0.175: " + 
                outputList.size() + " in " + (tb - ta) + " ms.");
    }
    
    
    private void printFiveCourseCombinationsWithSupportOver01Apriori() {
        final long ta = System.currentTimeMillis();
        final Set<Set<Course>> frequentItemsets = appData.apriori(0.1);
        final List<List<Course>> outputList = new ArrayList<>();
        
        for (final Set<Course> itemset : frequentItemsets) {
            if (itemset.size() == 5) {
                outputList.add(new ArrayList<>(itemset));
            }
        }
        
        final long tb = System.currentTimeMillis();
        
        System.out.println("Total frequent itemsets with support > 0.1: " +
                           frequentItemsets.size());
        System.out.println("Found " + outputList.size() + " 5-combinations " +
                           "in " + (tb - ta) + " ms.");
    }
    
    private void printFourCourseCombinationsWithSupportOver01Apriori() {
        final long ta = System.currentTimeMillis();
        final Set<Set<Course>> frequentItemsets = appData.apriori(0.1);
        final List<List<Course>> outputList = new ArrayList<>();
        
        for (final Set<Course> itemset : frequentItemsets) {
            if (itemset.size() == 4) {
                outputList.add(new ArrayList<>(itemset));
            }
        }
        
        final long tb = System.currentTimeMillis();
        
        System.out.println("Total frequent itemsets with support > 0.1: " +
                           frequentItemsets.size());
        System.out.println("Found " + outputList.size() + " 4-combinations " +
                           "in " + (tb - ta) + " ms.");
    }
    
    private void printThreeCourseCombinationsWithSupportOver0175Apriori() {
        final long ta = System.currentTimeMillis();
        final Set<Set<Course>> frequentItemsets = appData.apriori(0.175);
        final List<List<Course>> outputList = new ArrayList<>();
        
        for (final Set<Course> itemset : frequentItemsets) {
            if (itemset.size() == 3) {
                outputList.add(new ArrayList<>(itemset));
            }
        }
        
        final long tb = System.currentTimeMillis();
        System.out.println("Total frequent itemsets with support >= 0.175: " + 
                           frequentItemsets.size());
        System.out.println("Found " + outputList.size() + " 3-combinations " +
                           "in " + (tb - ta) + " ms.");
    }
    
    private void printWeek3Task16() {
        final long ta = System.currentTimeMillis();
        
        final List<SequenceAndSupport> result = 
                appData.sequentialApriori(0.0, 2);
        
        final long tb = System.currentTimeMillis();
        
        System.out.println("Time elapsed: " + (tb - ta) + " ms.");
        
        final List<SequenceAndSupport> list = choose(result, 2);
        
        System.out.println("Five hottest 2-sequences: ");
        
        for (int i = list.size() - 1, c = 0; i >= 0 && c < 5; --i, ++c) {
            System.out.println(list.get(i));
        }
    }
    
    private void printWeek3Task17() {
        final long ta = System.currentTimeMillis();
        
        final List<SequenceAndSupport> result = 
                appData.sequentialApriori(0.05, 5);
        
        final long tb = System.currentTimeMillis();
        
        System.out.println("Time elapsed: " + (tb - ta) + " ms.");
        
        final List<SequenceAndSupport> list = choose(result, 5);
        
        System.out.println("Five hottest 5-sequences: ");
        
        for (int i = list.size() - 1, c = 0; i >= 0 && c < 5; --i, ++c) {
            System.out.println(list.get(i));
        }
    }
    
    private void printWeek3Task17WithMaxspan() {
        System.out.println("printWeek3Task17WithMaxspan");
        
        final long ta = System.currentTimeMillis();
        
        final List<SequenceAndSupport> result = 
                appData.sequentialApriori(0.05, 5, 36);
        
        final long tb = System.currentTimeMillis();
        
        System.out.println("Time elapsed: " + (tb - ta) + " ms.");
        
        final List<SequenceAndSupport> list = choose(result, 5);
        
        System.out.println("Five hottest 5-sequences: ");
        
        for (int i = list.size() - 1, c = 0; i >= 0 && c < 5; --i, ++c) {
            System.out.println(list.get(i));
        }
    }
    
    private void printWeek3Task18() {
        final long ta = System.currentTimeMillis();
        
        final List<SequenceAndSupport> result = 
                appData.sequentialApriori(0.02, 8);
        
        final long tb = System.currentTimeMillis();
        
        System.out.println("Time elapsed: " + (tb - ta) + " ms.");
        
        final List<SequenceAndSupport> list = choose(result, 8);
        
        System.out.println("Five hottest 8-sequences: ");
        
        for (int i = list.size() - 1, c = 0; i >= 0 && c < 5; --i, ++c) {
            System.out.println(list.get(i));
        }
        
        System.out.println("---");
        
        for (int i = list.size() - 1, c = 0; i >= 0 && c < 5; --i, ++c) {
            final SequenceAndSupport sas = list.get(i);
            final Sequence seq = sas.getSequence();
            final double support = sas.getSupport();
            
            final List<String> courseNameList = new ArrayList<>();
            
            for (final Course course : seq) {
                courseNameList.add(course.getName());
            }
            
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            
            for (int j = 0; j < courseNameList.size(); ++j) {
                sb.append(courseNameList.get(j));
                
                if (j < courseNameList.size() - 1) {
                    sb.append(", ");
                }
            }
            
            System.out.println(sb.append("] ").append(support).toString());
        }
    }
    
    private void printWeek3Task18WithMaxspan() {
        final long ta = System.currentTimeMillis();
        
        final List<SequenceAndSupport> result = 
                appData.sequentialApriori(0.015, 8, 36);
        
        final long tb = System.currentTimeMillis();
        
        System.out.println("Time elapsed: " + (tb - ta) + " ms.");
        
        final List<SequenceAndSupport> list = choose(result, 8);
        
        System.out.println("Five hottest 8-sequences: ");
        
        for (int i = list.size() - 1, c = 0; i >= 0 && c < 5; --i, ++c) {
            System.out.println(list.get(i));
        }
        
        System.out.println("---");
        
        for (int i = list.size() - 1, c = 0; i >= 0 && c < 5; --i, ++c) {
            final SequenceAndSupport sas = list.get(i);
            final Sequence seq = sas.getSequence();
            final double support = sas.getSupport();
            
            final List<String> courseNameList = new ArrayList<>();
            
            for (final Course course : seq) {
                courseNameList.add(course.getName());
            }
            
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            
            for (int j = 0; j < courseNameList.size(); ++j) {
                sb.append(courseNameList.get(j));
                
                if (j < courseNameList.size() - 1) {
                    sb.append(", ");
                }
            }
            
            System.out.println(sb.append("] ").append(support).toString());
        }
    }
    
    private void printWeek4Task14() {
        System.out.println("--- printWeek4Task14 ---");
        
        final double minSupport = 0.15;
        final double minConfidence = 0.8;
        
        final long ta = System.currentTimeMillis();
        final List<AssociationRule> rules = appData.apriori(minSupport, 
                                                            minConfidence);
        final long tb = System.currentTimeMillis();
        
        System.out.println("Found " + rules.size() + " rules in " + 
                           (tb - ta) + " ms.");
        
        final Course introProgrammingCourse = 
                appData.getCourseByName("Ohjelmoinnin perusteet");
        
        System.out.println(introProgrammingCourse);
        
        for (final AssociationRule rule : rules) {
            if (rule.getConsequent().contains(introProgrammingCourse)) {
                System.out.println(rule);
            }
            
//            System.out.println(rule);
        }
    }
    
    private static 
        List<SequenceAndSupport> choose(final List<SequenceAndSupport> list,
                                        final int size) {
        final List<SequenceAndSupport> ret = new ArrayList<>(list.size());
        
        for (final SequenceAndSupport entry : list) {
            if (entry.getSequence().size() == size) {
                ret.add(entry);
            }
        }
        
        return ret;
    }
}
