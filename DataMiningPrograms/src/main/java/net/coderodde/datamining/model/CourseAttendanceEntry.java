package net.coderodde.datamining.model;

import static net.coderodde.datamining.utils.ValidationUtilities.checkNotLess;
import static net.coderodde.datamining.utils.ValidationUtilities.checkNotMore;
import static net.coderodde.datamining.utils.ValidationUtilities.checkNotNull;

/**
 * This class represents an attendance of a student at a course.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class CourseAttendanceEntry 
implements Comparable<CourseAttendanceEntry>{
    
    /**
     * The student of this attendance entry.
     */
    private final Student student;
    
    /**
     * The course being attended.
     */
    private final Course course;
    
    /**
     * The year <code>course</code> was attended.
     */
    private final int year;
    
    /**
     * The month <code>course</code> was attended.
     */
    private final int month;
    
    /**
     * The grade <code>student</code> received from the course 
     * <code>course</code>.
     */
    private final int grade;
    
    /**
     * Constructs a new course attendance entry.
     * 
     * @param student the student of this entry.
     * @param course  the course being attended.
     * @param year    the year the course was attended.
     * @param month   the month the course was attended.
     * @param grade   the course grade.
     */
    public CourseAttendanceEntry(final Student student,
                                 final Course course,
                                 final int year,
                                 final int month,
                                 final int grade) {
        checkNotNull(student, "The input student is null.");
        checkNotNull(course, "The input course is null.");
        checkMonth(month);
        
        checkNotLess(grade, 
                     Course.COURSE_FAILED_GRADE, 
                     "The grade is too small. Received: " + grade + ", " +
                     "must be no less than " + Course.COURSE_FAILED_GRADE + 
                     ".");
        
        checkNotMore(grade, 
                     Course.MAXIMUM_COURSE_GRADE, 
                     "The grade is too large. Received: " + grade + ", " +
                     "must be no more than " + Course.MAXIMUM_COURSE_GRADE +
                     ".");
        
        this.student = student;
        this.course = course;
        this.year = year;
        this.month = month;
        this.grade = grade;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public int getYear() {
        return year;
    }
    
    public int getMonth() {
        return month;
    }
    
    public int getGrade() {
        return grade;
    }
    
    public boolean isEarlierThan(final CourseAttendanceEntry other) {
        if (getYear() < other.getYear()) {
            return true;
        }
        
        if (getYear() > other.getYear()) {
            return false;
        }
        
        if (getMonth() < other.getMonth()) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof CourseAttendanceEntry)) {
            return false;
        }
        
        final CourseAttendanceEntry other = (CourseAttendanceEntry) obj;
        return getStudent().equals(other.getStudent()) 
                && getCourse().equals(other.getCourse())
                && getYear() == other.getYear()
                && getMonth() == other.getMonth();
    }
    
    @Override
    public int hashCode() {
        return getStudent().hashCode() +
               getCourse().hashCode() +
               getYear() +
               getMonth();
    }
    
    public static StudentSelector createAttendanceEntry() {
        return new StudentSelector();
    }

    @Override
    public int compareTo(final CourseAttendanceEntry other) {
        if (getYear() < other.getYear()) {
            return -1;
        }
        
        if (getYear() > other.getYear()) {
            return 1;
        }
        
        // Once here, both have the same year.
        if (getMonth() < other.getMonth()) {
            return -1;
        }
        
        if (getMonth() > other.getMonth()) {
            return 1;
        }
        
        // Once here, both have the same month as well. Sort by the course 
        // names.
        return getCourse().getName().compareTo(other.getCourse().getName());
    }
    
    public static final class StudentSelector {
        
        public CourseSelector withStudent(final Student student) {
            return new CourseSelector(student);
        }
    }
    
    public static final class CourseSelector {
        
        private final Student student;
        
        public CourseSelector(final Student student) {
            this.student = student;
        }
        
        public YearSelector withCourse(final Course course) {
            return new YearSelector(student, course);
        }
    }
    
    public static final class YearSelector {
        
        private final Student student;
        private final Course course;
        
        public YearSelector(final Student student, 
                            final Course course) {
            this.student = student;
            this.course = course;
        }
        
        public MonthSelector withYear(final int year) {
            return new MonthSelector(student, course, year);
        }
    }
    
    public static final class MonthSelector {
        
        private final Student student;
        private final Course course;
        private final int year;
        
        public MonthSelector(final Student student,
                             final Course course,
                             final int year) {
            this.student = student;
            this.course = course;
            this.year = year;
        }
        
        public GradeSelector withMonth(final int month) {
            return new GradeSelector(student, course, year, month);
        }
    }
    
    public static final class GradeSelector {
        
        private final Student student;
        private final Course course;
        private final int year;
        private final int month;
        
        public GradeSelector(final Student student,
                             final Course course,
                             final int year,
                             final int month) {
            this.student = student;
            this.course = course;
            this.year = year;
            this.month = month;
        }
        
        public CourseAttendanceEntry withGrade(final int grade) {
            return new CourseAttendanceEntry(student,
                                             course,
                                             year,
                                             month,
                                             grade);
        }
    }
    
    /**
     * Validates that the month integer is within range <tt>[1, 12]</tt>.
     * 
     * @param month the month integer to check.
     */
    private static final void checkMonth(final int month) {
        // The magic numbers 1 and 12 are not likely to change so inserted
        // as is.
        checkNotLess(month, 1, 
                "The month integer too small. Received: " + month + ", the " +
                "lower bound is " + 1);
        
        checkNotMore(month, 12,
                "The month integer too small. Received: " + month + ", the " +
                "lower bound is " + 12);
    }
}
