package net.coderodde.datamining.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This singleton class is responsible for organizing all the data such, that 
 * it can be accessed efficiently.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class AppDataStorage {
    
    /**
     * This map maps each student to the list of course attendance entries of
     * that student.
     */
    private final Map<Student, List<CourseAttendanceEntry>> studentMap;
    
    /**
     * This map maps each course to the list of course attendance entries of
     * that course.
     */
    private final Map<Course, List<CourseAttendanceEntry>> courseMap;
    
    /**
     * This map maps the name of each course to the actual course.
     */
    private final Map<String, Course> mapNameToCourse;
    
    /**
     * The list containing all the courses.
     */
    private final List<Course> courseList;
    
    /**
     * Constructs a new application data storage and establishes the maps for 
     * faster data access.
     * 
     * @param studentList the list of students.
     * @param courseList  the list of courses.
     * @param entryList   the list of course attendance entries.
     */
    public AppDataStorage(final List<Student> studentList,
                          final List<Course> courseList,
                          final List<CourseAttendanceEntry> entryList) {
        this.studentMap = new HashMap<>(studentList.size());
        this.courseMap = new HashMap<>(courseList.size());
        this.courseList = Collections.<Course>unmodifiableList(courseList);
        this.mapNameToCourse = new HashMap<>(courseList.size());
        
        for (final CourseAttendanceEntry entry : entryList) {
            final Student student = entry.getStudent();
            final Course course = entry.getCourse();
            
            if (studentMap.containsKey(student)) {
                studentMap.get(student).add(entry);
            } else {
                studentMap.put(student, initEntryList(entry));
            }
            
            if (courseMap.containsKey(course)) {
                courseMap.get(course).add(entry);
            } else {
                courseMap.put(course, initEntryList(entry));
            }
        }
        
        for (final Course course : courseMap.keySet()) {
            mapNameToCourse.put(course.getName(), course);
        }
    }
    
    /**
     * Returns a list view containing all the courses.
     * 
     * @return a list of courses.
     */
    public List<Course> getCourseList() {
        return courseList;
    }
    
    /**
     * Returns the course by its name.
     * 
     * @param  courseName the name of the course to get.
     * @return the course with the input name, or <code>null</code> if there is
     *         no such.
     */
    public Course getCourseByName(final String courseName) {
        return mapNameToCourse.get(courseName);
    }
    
    /**
     * Returns a list of attendance entries of the <code>course</code>.
     * 
     * @param  course the course whose entries to get.
     * @return a list of attendance entries of the input course.
     */
    public List<CourseAttendanceEntry> getEntriesOfCourse(final Course course) {
        return Collections.<CourseAttendanceEntry>
                unmodifiableList(courseMap.get(course));
    }
    
    /**
     * Returns the list of students that have attained the course with name
     * <code>courseName</code>.
     * 
     * @param  courseName the name of the target course.
     * @return the list of students.
     */
    public List<Student> getStudentsByCourseName(final String courseName) {
        final Set<Student> studentSet = new HashSet<>();
        
        final List<CourseAttendanceEntry> entryList = 
                getEntriesOfCourse(getCourseByName(courseName));
        
        for (final CourseAttendanceEntry entry : entryList) {
            studentSet.add(entry.getStudent());
        }
        
        final List<Student> ret = new ArrayList<>(studentSet.size());
        ret.addAll(studentSet);
        return ret;
    }
    
    private List<CourseAttendanceEntry> 
        initEntryList(final CourseAttendanceEntry entry) {
        final List<CourseAttendanceEntry> list = new ArrayList<>();
        list.add(entry);
        return list;
    }
}
