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
    
    public List<Student> getStudentsFrom(final Course course,
                                         final int year,
                                         final int month) {
        final List<CourseAttendanceEntry> entryList = courseMap.get(course);
        final Set<Student> studentSet = new HashSet<>();
        
        for (final CourseAttendanceEntry entry : entryList) {
            if (entry.getYear() >= year && entry.getMonth() >= month) {
                studentSet.add(entry.getStudent());
            }
        }
        
        return new ArrayList<>(studentSet);
    }
    
    public List<Student> getStudentsUntil(final Course course,
                                          final int year,
                                          final int month) {
        final List<CourseAttendanceEntry> entryList = courseMap.get(course);
        final Set<Student> studentSet = new HashSet<>();
        
        for (final CourseAttendanceEntry entry : entryList) {
            if (entry.getYear() <= year && entry.getMonth() <= month) {
                studentSet.add(entry.getStudent());
            }
        }
        
        return new ArrayList<>(studentSet);
    }
    
    public int getStudentAmount() {
        return studentMap.size();
    }
    
    public int getCourseAmount() {
        return courseMap.size();
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
    
    public Map<Course, Map<Course, Integer>> getSupportMatrix() {
        final int N = courseList.size();
        final Map<Course, Map<Course, Integer>> map = new HashMap<>(N);
        
        // Initialize the support matrix.
        for (int index1 = 0; index1 < N; ++index1) {
            final Course course1 = courseList.get(index1);
            final Map<Course, Integer> submap = new HashMap<>(N);
            map.put(course1, submap);
            
            for (int index2 = 0; index2 < N; ++index2) {
                final Course course2 = courseList.get(index2);
                submap.put(course2, 0);
            }
        }
        
        // Count the supports.
        for (final Student student : studentMap.keySet()) {
            final Set<Course> studentCourseSet = getStudentsAllCourses(student);
            final List<Course> studentCourseList = 
                    new ArrayList<>(studentCourseSet);
            final int LIST_SIZE  = studentCourseList.size();
            
            // Iterate over all 2-combinations and update the support matrix.
            for (int index1 = 0; index1 < LIST_SIZE; ++index1) {
                final Course course1 = studentCourseList.get(index1);
                
                for (int index2 = index1 + 1; index2 < LIST_SIZE; ++index2) {
                    final Course course2 = studentCourseList.get(index2);
                    final int currentSupport = map.get(course1).get(course2);
                    map.get(course1).put(course2, currentSupport + 1);
                    map.get(course2).put(course1, currentSupport + 1);
                }
            }
        }
        
        return map;
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
    
    public double supportStopAfter(final Set<Course> setx, 
                                   final Set<Course> sety,
                                   final int threshold) {
        checkIsAssociationRule(setx, sety);
        final Set<Course> work = new HashSet<>(setx);
        work.addAll(sety);
        
        int count = 0;
        
        for (final Student student : studentMap.keySet()) {
            final Set<Course> courseSet = getStudentsAllCourses(student);
            
            if (containsAll(work, courseSet)) {
                if (++count >= threshold) {
                    break;
                }
            }
        }
        
        return 1.0 * count / getStudentAmount();
    }
    
    public Set<Set<Course>> apriori(final double minSupport) {
        final Map<Integer, Set<Set<Course>>> map = new HashMap<>();
        final Map<Set<Course>, Integer> sigma = new HashMap<>();
        
        map.put(1, new HashSet<Set<Course>>());
        
        for (final Course course : getCourseList()) {
            final int supportCount = supportCount(course);
            final double support = 1.0 * supportCount / studentMap.size();
            
            if (support >= minSupport) {
                final Set<Course> itemSet = new HashSet<>(1);
                itemSet.add(course);
                map.get(1).add(itemSet);
                sigma.put(itemSet, supportCount);
            }
        }
        
        int k = 1;
        
        do {
            ++k;
            System.out.println("k: " + k);
            final Set<Set<Course>> candidateSet = 
                    generateCandidates(map.get(k - 1));
            
            for (final Student student : studentMap.keySet()) {
                final Set<Course> transaction = getStudentsAllCourses(student);
                final Set<Set<Course>> candidateSet2 = subset(candidateSet, 
                                                              transaction);
                
                for (final Set<Course> itemset : candidateSet2) {
                    if (!sigma.containsKey(itemset)) {
                        sigma.put(itemset, 1);
                    } else {
                        sigma.put(itemset, sigma.get(itemset) + 1);
                    }
                }
            }
            
            map.put(k, getNextItemsets(candidateSet, 
                                       sigma,
                                       (int)(minSupport * studentMap.size())));
        } while (map.get(k).size() > 0);
        
        return extractItemSets(map);
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
    
    public double support(final Course course) {
        int count = 0;
        
        for (final Student student : studentMap.keySet()) {
            final List<CourseAttendanceEntry> list = 
                    matrix.get(student).get(course);
            
            if (list != null && list.size() > 0) {
                ++count;
            }
        }
        
        return 1.0 * count / studentMap.size();
    }
    
    public int supportCount(final Course course) {
        int count = 0;
        
        for (final Student student : studentMap.keySet()) {
            final List<CourseAttendanceEntry> list = 
                    matrix.get(student).get(course);
            
            if (list != null && list.size() > 0) {
                ++count;
            }
        }
        
        return count;
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

    private Set<Set<Course>> 
        extractItemSets(final Map<Integer, Set<Set<Course>>> map) {
        final Set<Set<Course>> ret = new HashSet<>();
        
        for (final Set<Set<Course>> kItemSets : map.values()) {
            ret.addAll(kItemSets);
        }
        
        return ret;
    }

    /**
     * Returns all those itemsest from <code>set</code> that are contained by
     * <code>transaction</code>.
     * 
     * @param set         the set of itemsets.
     * @param transaction a transaction.
     * @return            the set of itemsets that are contained by 
     *                    <code>transaction</code>.
     */
    private Set<Set<Course>> subset(final Set<Set<Course>> set,
                                    final Set<Course> transaction) {
        final Set<Set<Course>> ret = new HashSet<>();
        
        for (final Set<Course> itemset : set) {
            if (containsAll(itemset, transaction)) {
                ret.add(itemset);
            }
        }
        
        return ret;
    }

    /**
     * Generates the <code>(k + 1)</code>-itemsets from the set of 
     * <code>k</code>-itemsets.
     * 
     * @param  set the set of itemsets.
     * @return a set of candidate itemsets.
     */
    private Set<Set<Course>> generateCandidates(final Set<Set<Course>> set) {
        final List<List<Course>> list = new ArrayList<>(set.size());
        
        for (final Set<Course> itemset : set) {
            final List<Course> l = new ArrayList<>(itemset);
            Collections.sort(l);
            list.add(l);
        }
        
        final int N = list.size();
        final Set<Set<Course>> ret = new HashSet<>(list.size());
        
        for (int index1 = 0; index1 < N; ++index1) {
            for (int index2 = index1 + 1; index2 < N; ++index2) {
                final Set<Course> candidate = 
                        tryMergeItemsets(list.get(index1), 
                                         list.get(index2));
                
                if (candidate != null) {
                    ret.add(candidate);
                }
            }
        }
        
        return ret;
    }
    
    private Set<Course> tryMergeItemsets(final List<Course> itemset1,
                                         final List<Course> itemset2) {
        final int length = itemset1.size();
        
        for (int i = 0; i < length - 1; ++i) {
            if (!itemset1.get(i).equals(itemset2.get(i))) {
                return null;
            }
        }
        
        if (itemset1.get(length - 1).equals(itemset2.get(length - 1))) {
            return null;
        }
        
        final Set<Course> itemset = new HashSet<>(itemset1.size() + 1);
        
        for (int i = 0; i < length - 1; ++i) {
            itemset.add(itemset1.get(i));
        }
        
        itemset.add(itemset1.get(length - 1));
        itemset.add(itemset2.get(length - 1));
        return itemset;
    }

    private Set<Set<Course>> 
        getNextItemsets(final Set<Set<Course>> candidateSet, 
                        final Map<Set<Course>, Integer> sigma,
                        final int minSupportCount) {
        final Set<Set<Course>> ret = new HashSet<>(candidateSet.size());
        
        for (final Set<Course> itemset : candidateSet) {
            if (sigma.get(itemset) != null 
                    && sigma.get(itemset) >= minSupportCount) {
                ret.add(itemset);
            }
        }
        
        return ret;
    }
}
