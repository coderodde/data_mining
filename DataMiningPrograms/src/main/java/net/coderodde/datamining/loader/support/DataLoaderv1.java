package net.coderodde.datamining.loader.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import net.coderodde.datamining.loader.DataLoader;
import net.coderodde.datamining.model.AppDataStorage;
import net.coderodde.datamining.model.Course;
import static net.coderodde.datamining.model.Course.GRADING_MODE_NORMAL_SCALE;
import static net.coderodde.datamining.model.Course.createCourse;
import net.coderodde.datamining.model.CourseAttendanceEntry;
import static net.coderodde.datamining.model.CourseAttendanceEntry.createAttendanceEntry;
import net.coderodde.datamining.model.Student;
import static net.coderodde.datamining.model.Student.createStudent;

/**
 * This class implements a data loader. This class is not thread-safe.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class DataLoaderv1 extends DataLoader {

    /**
     * The string representing course failure.
     */
    private static final String COURSE_FAILED_TOKEN = "Hyl.";
    
    /**
     * The string denoting that a student passed a course with binary
     * (passed/failed) grading mode.
     */
    private static final String COURSE_PASSED_TOKEN = "Hyv.";
    
    /**
     * The string denoting that a student was not attending the course exam.
     */
    private static final String NOT_IN_EXAM = "Eisa";
    
    /**
     * The string denoting that a student has abandoned the course.
     */
    private static final String ABANDONED = "Luop";
    
    /**
     * Laudatur grade.
     */
    private static final String LAUDATUR = "L";
    
    /**
     * Eximia grade.
     */
    private static final String EXIMIA = "ECLA";
    
    /**
     * Magna Cum Laude Approbatur grade.
     */
    private static final String MAGNA = "MCLA";
    
    /**
     * Cum Laude Approbatur grade.
     */
    private static final String CUM_LAUDE = "CL";
    
    /**
     * Non Sine Laude Approbatur grade.
     */
    private static final String NSLA = "NSLA";
    
    /**
     * Lubenter grade.
     */
    private static final String LUBENTER = "LUB";
    
    /**
     * The grade denoting satisfactory knowledge. Mapped to binary grading.
     */
    private static final String GRADE_SATISFACTORY = "TT";
    
    /**
     * The grade denoting good knowledge. Mapped to binary grading.
     */
    private static final String GRADE_GOOD = "HT";
    
    /**
     * Denotes the amount of tokens per course attendance entry.
     */
    private static final int TOKENS_PER_ENTRY = 5;
    
    /**
     * The list of courses.
     */
    private List<Course> courseList;
    
    /**
     * The list of students.
     */
    private List<Student> studentList;
    
    /**
     * The list of course attendance entries.
     */
    private List<CourseAttendanceEntry> entryList;
    
    /**
     * The student counter based ID.
     */
    private int studentId;
    
    /**
     * The course array indexed by courses.
     */
    private Map<Course, Course> courses;
    
    @Override
    public AppDataStorage load(File file) {
        checkFile(file);
        Scanner scanner = null;
        
        try {
            scanner = new Scanner(new FileReader(file));
        } catch (final FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return null;
        }
        
        return loadDataStorage(scanner);
    }
    
    private AppDataStorage loadDataStorage(final Scanner scanner) {
        courseList = new ArrayList<>();
        studentList = new ArrayList<>();
        entryList = new ArrayList<>();
        courses = new HashMap<>();
        studentId = 0;
        
        while (scanner.hasNext()) {
            parseLine(scanner.nextLine());
        }
        
        courseList.addAll(courses.keySet());
        return new AppDataStorage(studentList, courseList, entryList);
    }
    
    private void parseLine(final String line) {
        final String[] parts = handleCourseNames(line.split(" "));
        final Student student = 
                createStudent()
                        .withId(++studentId)
                        .withRegistrationYear(Integer.parseInt(parts[0]));
        
        studentList.add(student);
        
        final int totalCourses = (parts.length - 1) / TOKENS_PER_ENTRY;
        
        for (int i = 0; i < totalCourses; ++i) {
            processCourse(student, parts, 1 + i * TOKENS_PER_ENTRY);
        }
    }
    
    private void processCourse(
            final Student student,
            final String[] parts, 
            final int startIndex) {
        final String dateString = parts[startIndex];
        final String[] dateStringParts = dateString.split("-");
        final String yearString = dateStringParts[0];
        final String monthString = dateStringParts[1];
        
        final int year = Integer.parseInt(yearString);
        final int month = Integer.parseInt(monthString);
        
        final String courseCode = parts[startIndex + 1];
        final String courseNameRaw = parts[startIndex + 2].trim();
        
        final String courseName = 
                courseNameRaw.substring(1, courseNameRaw.length() - 1);
        
        final float credits = Float.parseFloat(parts[startIndex + 3]);
        final String grade = parts[startIndex + 4];
        
        Course course = 
                createCourse()
                .withName(courseName)
                .withCode(courseCode)
                .withNormalScale()
                .withCredits(credits);
        
        if (!courses.containsKey(course)) {
            courses.put(course, course);
        }
        
        if (grade.equals(COURSE_PASSED_TOKEN) 
                && course.getGradingMode() == GRADING_MODE_NORMAL_SCALE) {
            courses.remove(course);
            
            course = createCourse() 
                     .withName(courseName)
                     .withCode(courseCode)
                     .withBinaryScale()
                     .withCredits(credits);
            
            courses.put(course, course);
        }
        
        int gradeInt = 0;
        
        switch (grade) {
            case COURSE_FAILED_TOKEN:
            case NOT_IN_EXAM:
            case ABANDONED:
                gradeInt = 0;
                break;
                
            case COURSE_PASSED_TOKEN:
            case GRADE_SATISFACTORY:
            case GRADE_GOOD:
            case LAUDATUR:
            case EXIMIA:
            case MAGNA:
            case CUM_LAUDE:
            case NSLA:
            case LUBENTER:
                gradeInt = 1;
                break;
                
            default:
                gradeInt = Integer.parseInt(grade);
        }
        
        final CourseAttendanceEntry entry =
                createAttendanceEntry()
                .withStudent(student)
                .withCourse(course)
                .withYear(year)
                .withMonth(month)
                .withGrade(gradeInt);
        
        entryList.add(entry);
    }
    
    /**
     * Joins the words contributing to a name of a course consisting of several
     * words. For example, "Introduction to programming".
     * 
     * @param  parts the individual words.
     * @return the joined string.
     */
    private String[] handleCourseNames(final String[] parts) {
        final List<String> partList = new ArrayList<>(parts.length);
        
        for (int i = 0; i < parts.length; ++i) {
            partList.add(parts[i]);
            
            // An ad-hoc stuff in order to parse shit as
            // ""Am I funky enought?" - self-reflection course"
            if (parts[i].startsWith("\"\"")) {
                int quotesLeft = 2;
                
                for (;;) {
                    ++i;
                    
                    partList.set(partList.size() - 1,
                                 partList.get(partList.size() - 1) +
                                 " " + parts[i]);
                    
                    if (parts[i].endsWith("\"")) {
                        if (--quotesLeft == 0) {
                            break;
                        }
                    }
                }
            } else if (parts[i].startsWith("\"")) {
                if (parts[i].length() == 1 || !parts[i].endsWith("\"")) {
                    // Load the parts until one ends with a quote.
                    do {
                        ++i;
                        partList.set(partList.size() - 1, 
                                     partList.get(partList.size() - 1) + 
                                     " " + parts[i]);
                    } while (!parts[i].endsWith("\""));
                }
            }
        }
        
        return partList.toArray(new String[partList.size()]);
    }
}
