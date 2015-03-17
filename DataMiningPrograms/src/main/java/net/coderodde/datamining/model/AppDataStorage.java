package net.coderodde.datamining.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static net.coderodde.datamining.model.Course.COURSE_FAILED_GRADE;
import static net.coderodde.datamining.utils.Utils.containsAll;
import static net.coderodde.datamining.utils.Utils.intersect;
import static net.coderodde.datamining.utils.ValidationUtilities.checkNotMore;

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
     * The structure mapping <tt>(student, course)</tt> tuple to the list of
     * course attendances.
     */
    private final Map<Student, Map<Course, List<CourseAttendanceEntry>>> 
            matrix;
    
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
        this.matrix = new HashMap<>(studentList.size());
        
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
        
        for (final Student student : studentList) {
            final Map<Course, List<CourseAttendanceEntry>> map 
                    = new HashMap<>();
            
            matrix.put(student, map);
            
            final List<CourseAttendanceEntry> entryListA = 
                    studentMap.get(student);
            
            for (final CourseAttendanceEntry entry : entryListA) {
                final Course course = entry.getCourse();
                
                if (map.containsKey(course)) {
                    map.get(course).add(entry);
                } else {
                    final List<CourseAttendanceEntry> entryListB = 
                            new ArrayList<>();
                    entryListB.add(entry);
                    map.put(course, entryListB);
                }
            }
        }
    }
    
    public int getStudentAmount() {
        return studentMap.size();
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
     * Returns a list of all attendances a student <code>student</code> had on
     * behalf the course <code>course</code>.
     * 
     * @param student the target student.
     * @param course  the target course.
     * @return        the list of attendance entries.
     */
    public List<CourseAttendanceEntry> getEntryList(final Student student,
                                                    final Course course) {
        return Collections.<CourseAttendanceEntry>
                unmodifiableList(matrix.get(student).get(course));
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
    
    public boolean passed(final Student student, final Course course) {
        final List<CourseAttendanceEntry> entryList = 
                matrix.get(student).get(course);
        
        if (entryList == null || entryList.isEmpty()) {
            return false;
        }
        
        for (final CourseAttendanceEntry entry : entryList) {
            if (entry.getGrade() != COURSE_FAILED_GRADE) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Returns the grade of student <code>student</code> for the course
     * <code>course</code>. If the student attempted the course more than once,
     * the best grade is returned.
     * 
     * @param student the target student.
     * @param course  the target course.
     * @return the best grade of the student for the course.
     */
    public int grade(final Student student, final Course course) {
        final List<CourseAttendanceEntry> entryList = 
                matrix.get(student).get(course);
                
        if (entryList == null || entryList.isEmpty()) {
            return Course.NON_EXISTENT_GRADE;
        }
        
        int best = Course.NON_EXISTENT_GRADE;
        
        for (final CourseAttendanceEntry entry : entryList) {
            if (best < entry.getGrade()) {
                best = entry.getGrade();
            }
        }
        
        return best;
    }
    
    public boolean hasGrade(final Student student, 
                            final Course course,
                            final int minGrade,
                            final int maxGrade) {
        final int localMinGrade = Math.min(minGrade, maxGrade);
        final int localMaxGrade = Math.max(minGrade, maxGrade);
        
        final List<CourseAttendanceEntry> entryList = 
                matrix.get(student).get(course);
        
        if (entryList == null || entryList.isEmpty()) {
            return localMinGrade >= Course.NON_EXISTENT_GRADE;
        }
        
        for (final CourseAttendanceEntry entry : entryList) {
            final int grade = entry.getGrade();
            
            if (grade >= localMinGrade && grade <= localMaxGrade) {
                return true;
            }
        }
        
        return false;
    }
    
    public List<Student> queryStudents(final Set<Course> courseSet) {
        final List<Student>[] lists = new ArrayList[courseSet.size()];
        
        int i = 0;
        
        for (final Course course : courseSet) {
            lists[i++] = getStudentsByCourseName(course.getName());
        }
        
        final List<Student> ret = new ArrayList<>();
        ret.addAll(intersect(lists));
        return ret;
    }
    
    public List<Student> queryStudents(final Set<Course> courseSet,
                                       final int minGrade,
                                       final int maxGrade) {
        checkNotMore(minGrade, 
                     maxGrade, 
                     "The minimum and maximum grades ass-backwards.");
        final List<Student>[] lists = new ArrayList[courseSet.size()];
        
        int i = 0;
        
        for (final Course course : courseSet) {
            lists[i++] = getStudentsByCourseName(course.getName());
            
            for (int j = lists[i].size() - 1; j >= 0; --j) {
                if (!hasGrade(lists[i].get(j), course, minGrade, maxGrade)) {
                    lists[i].remove(j);
                }
            }
        }
        
        final List<Student> ret = new ArrayList<>();
        ret.addAll(intersect(lists));
        return ret;
    }
    
    public List<Student> queryStudents(final Course course, 
                                       final int minGrade,
                                       final int maxGrade) {
        checkNotMore(minGrade, 
                     maxGrade, 
                     "Mininum and maximum grades ass-backwards.");
        
        final List<Student> studentList = 
                getStudentsByCourseName(course.getName());
        
        final List<Student> ret = new ArrayList<>(studentList.size());
        
        for (final Student student : studentList) {
            if (hasGrade(student, course, minGrade, maxGrade)) {
                ret.add(student);
            }
        }
        
        return ret;
    }
    
    public double support(final Set<Course> setx, final Set<Course> sety) {
        checkIsAssociationRule(setx, sety);
        final Set<Course> work = new HashSet<>(setx);
        work.addAll(sety);
        
        int count = 0;
        
        for (final Student student : studentMap.keySet()) {
            final Set<Course> courseSet = getStudentsAllCourses(student);
            
            if (containsAll(work, courseSet)) {
                ++count;
            }
        }
        
        return 1.0 * count / getStudentAmount();
    }
    
    public double confidence(final Set<Course> setx, final Set<Course> sety) {
        checkIsAssociationRule(setx, sety);
        final Set<Course> work = new HashSet<>(setx);
        work.addAll(sety);
        
        int countXY = 0;
        int countX = 0;
        
        for (final Student student : studentMap.keySet()) {
            final Set<Course> courseSet = getStudentsAllCourses(student);
            
            if (containsAll(setx, courseSet)) {
                ++countX;
                
                if (containsAll(work, courseSet)) {
                    ++countXY;
                }
            }
        }
        
        return 1.0 * countXY / countX;
    }
    
    public Set<Course> getStudentsAllCourses(final Student student) {
        final List<CourseAttendanceEntry> entryList = studentMap.get(student);
        final Set<Course> ret = new HashSet<>();
        
        for (final CourseAttendanceEntry entry : entryList) {
            ret.add(entry.getCourse());
        }
        
        return ret;
    }
    
    private List<CourseAttendanceEntry> 
        initEntryList(final CourseAttendanceEntry entry) {
        final List<CourseAttendanceEntry> list = new ArrayList<>();
        list.add(entry);
        return list;
    }
        
    private static <T> void checkIsAssociationRule(final Set<T> set1, 
                                                   final Set<T> set2) {
        for (final T element : set1) {
            if (set2.contains(element)) {
                throw new IllegalArgumentException(
                        "Not an association rule. Both sets contain " +
                        element.toString() + ".");
            }
        }
    }
}
