package net.coderodde.datamining.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
     * This maps each student to the list of courses of that student.
     */
    private final Map<Student, List<Course>> studentToCourseListMap;
    
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
        this.studentToCourseListMap = new HashMap<>(studentList.size());
        
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
                    Collections.sort(entryListB);
                }
            }
        }
        
        Collections.sort(courseList);
        
        for (final Student student : studentList) {
            final List<Course> sortedCourseList = new ArrayList<>();
            final Set<Course> courseSet = new HashSet<>();
            
            for (final CourseAttendanceEntry entry : studentMap.get(student)) {
                courseSet.add(entry.getCourse());
            }
            
            sortedCourseList.addAll(courseSet);
//            Collections.sort(sortedCourseList, 
//                             new CourseComparatorByEntries(courseMap));
            
            studentToCourseListMap.put(student, courseList);
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
            return false;
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
    
    public Map<Course, Map<Course, Map<Course, Map<Course, Integer>>>> 
            getSupport4DMatrix() {
        final int N = courseList.size();
        final Map<Course, Map<Course, Map<Course, Map<Course, Integer>>>> map =
                new HashMap<>(N);
        
        // Initialize the matrix.
        for (int i1 = 0; i1 < N; ++i1) {
            final Course c1 = courseList.get(i1);
            final Map<Course, Map<Course, Map<Course, Integer>>> submap1 =
                    new HashMap<>();
            map.put(c1, submap1);
            
            for (int i2 = i1 + 1; i2 < N; ++i2) {
                final Course c2 = courseList.get(i2);
                final Map<Course, Map<Course, Integer>> submap2 = 
                        new HashMap<>();
                submap1.put(c2, submap2);
                
                for (int i3 = i2 + 1; i3 < N; ++i3) {
                    final Course c3 = courseList.get(i3);
                    final Map<Course, Integer> submap3 = new HashMap<>();
                    submap2.put(c3, submap3);
                    
                    for (int i4 = i3 + 1; i4 < N; ++i4) {
                        final Course c4 = courseList.get(i4);
                        submap3.put(c4, 0);
                    }
                }
            }
        }
        
        // Count the supports.
        for (final Student student : studentMap.keySet()) {
            final Set<Course> studentCourseSet = getStudentsAllCourses(student);
            final List<Course> studentCourseList = 
                    new ArrayList<>(studentCourseSet);
            final int LIST_SIZE = studentCourseList.size();
            
            for (int i1 = 0; i1 < LIST_SIZE; ++i1) {
                final Course c1 = studentCourseList.get(i1);
                final Map<Course, Map<Course, Map<Course, Integer>>> submap = 
                        map.get(c1);
                
                for (int i2 = i1 + 1; i2 < LIST_SIZE; ++i2) {
                    final Course c2 = studentCourseList.get(i2);
                    final Map<Course, Map<Course, Integer>> submap2 = 
                            submap.get(c2);
                    
                    for (int i3 = i2 + 1; i3 < LIST_SIZE; ++i3) {
                        final Course c3 = studentCourseList.get(i3);
                        final Map<Course, Integer> submap3 = 
                                submap2.get(c3);
                        
                        for (int i4 = i3 + 1; i4 < LIST_SIZE; ++i4) {
                            final Course c4 = studentCourseList.get(i4);
                            final int currentSupport = submap3.get(c4);
                            submap3.put(c4, currentSupport + 1);
                        }
                    }
                }
            }
        }
        
        return map;
    }
    
    public Map<Course, Map<Course, Map<Course, Integer>>> getSupportCube() {
        final int N = courseList.size();
        final Map<Course, Map<Course, Map<Course, Integer>>> map = 
                new HashMap<>(N);
        
        // Initialize the support cube.
        for (int index1 = 0; index1 < N; ++index1) {
            final Course course1 = courseList.get(index1);
            final Map<Course, Map<Course, Integer>> submap = new HashMap<>(N);
            map.put(course1, submap);
            
            for (int index2 = 0; index2 < N; ++index2) {
                final Course course2 = courseList.get(index2);
                final Map<Course, Integer> subsubmap = new HashMap<>(N);
                submap.put(course2, subsubmap);
                
                for (int index3 = 0; index3 < N; ++index3) {
                    final Course course3 = courseList.get(index3);
                    subsubmap.put(course3, 0);
                }
            }
        }
        
        // Count the supports.
        for (final Student student : studentMap.keySet()) {
            final Set<Course> studentCourseSet = getStudentsAllCourses(student);
            final List<Course> studentCourseList = 
                    new ArrayList<>(studentCourseSet);
            final int LIST_SIZE  = studentCourseList.size();
            
            // Iterate over all 3-combinations and update the support matrix.
            for (int index1 = 0; index1 < LIST_SIZE; ++index1) {
                final Course course1 = studentCourseList.get(index1);
                
                for (int index2 = index1 + 1; index2 < LIST_SIZE; ++index2) {
                    final Course course2 = studentCourseList.get(index2);
                    
                    for (int index3 = index2 + 1; index3 < LIST_SIZE; ++index3) {
                        final Course course3 = studentCourseList.get(index3);
                        final int currentSupport =
                                map.get(course1).get(course2).get(course3);
                        
                        map.get(course1).get(course2).put(course3, currentSupport + 1);
                        map.get(course1).get(course3).put(course2, currentSupport + 1);
                        map.get(course2).get(course1).put(course3, currentSupport + 1);
                        map.get(course2).get(course3).put(course1, currentSupport + 1);
                        map.get(course3).get(course1).put(course2, currentSupport + 1);
                        map.get(course3).get(course2).put(course1, currentSupport + 1);
                    }
                }
            }
        }
        
        return map;
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
    
    public static class SequenceAndSupport 
    implements Comparable<SequenceAndSupport>{
        
        private final Sequence sequence;
        private final double support;
        
        public SequenceAndSupport(final Sequence sequence, 
                                  final double support) {
            this.sequence = sequence;
            this.support = support;
        }
        
        public Sequence getSequence() {
            return sequence;
        }
        
        public double getSupport() {
            return support;
        }

        @Override
        public int compareTo(SequenceAndSupport o) {
            return Double.compare(support, o.support);
        }
        
        @Override
        public String toString() {
            return "[[" + sequence + ": " + support + "]]";
        }
    }
    
    public List<SequenceAndSupport> 
        sequentialApriori(final double minSupport,
                          final int size,
                          final int maxspan) {
        final Map<Sequence, Double> seqToSupportMap = new HashMap<>();
        final Map<Sequence, Integer> sigma = new HashMap<>();
        final Map<Integer, List<Sequence>> map = new HashMap<>();
        
        //// BEGIN: Work structures.
        final List<List<Course>> workList = new ArrayList<>();
        final List<Course> elementList = new ArrayList<>();
        workList.add(elementList);
        //// END: Work structures.
        
        final int ROWS = studentMap.size();
        
        map.put(1, new ArrayList<Sequence>());
        
        // In the first iteration, find out all frequent 1-sequences.
        for (final Course course : courseList) {
            final int supportCount = supportCount(course);
            final double support = 1.0 * supportCount / ROWS;
            
            if (support >= minSupport) {
                elementList.clear();
                elementList.add(course);
                
                final Sequence sequence = new Sequence(workList);
                
                if (!sequence.fitsInSpan(maxspan)) {
                    continue; // Ignore this sequence.
                }
                
                map.get(1).add(sequence);
                sigma.put(sequence, supportCount);
                
                seqToSupportMap.put(sequence, support);
            }
        }
        
        if (size <= 1) {
            final List<SequenceAndSupport> ret = 
                    extractSequences(map, seqToSupportMap);

            Collections.sort(ret);

            return ret;
        }
        
        final Map<Student, Sequence> transactionMap = 
                new HashMap<>(studentMap.size());
        
        for (final Student student : studentMap.keySet()) {
            transactionMap.put(student, 
                               getStudentCoursesAsSequenceWithTimes(student));
        }
        
        int k = 1;
        
        do {
            ++k;
            
            System.out.println("Doing k = " + k);
            
            final List<Sequence> candidateList = 
                    generateSequenceCandidates(map.get(k - 1), maxspan);
            
            System.out.println("Candidates: " + candidateList.size());
            
            for (final Student student : studentMap.keySet()) {
                final Sequence transaction = transactionMap.get(student);
                final List<Sequence> candidateList2 = subsequence(candidateList,
                                                                  transaction,
                                                                  student,
                                                                  maxspan);
                
                for (final Sequence sequence : candidateList2) {
                    if (!sigma.containsKey(sequence)) {
                        sigma.put(sequence, 1);
                        seqToSupportMap.put(sequence, 1.0 / ROWS);
                    } else {
                        final int newSupportCount = sigma.get(sequence) + 1;
                        sigma.put(sequence, newSupportCount);
                        seqToSupportMap.put(sequence, 
                                            1.0 * newSupportCount / ROWS);
                    }
                }
            }
            
            map.put(k, getNextSequences(candidateList, sigma, minSupport));
        } while (k < size && map.get(k).size() > 0);
        
        final List<SequenceAndSupport> ret = 
                extractSequences(map, seqToSupportMap);
        
        Collections.sort(ret);
        
        return ret;
    }
    
    public List<SequenceAndSupport> 
        sequentialApriori(final double minSupport,
                          final int size) {
        final Map<Sequence, Double> seqToSupportMap = new HashMap<>();
        final Map<Sequence, Integer> sigma = new HashMap<>();
        final Map<Integer, List<Sequence>> map = new HashMap<>();
        
        //// BEGIN: Work structures.
        final List<List<Course>> workList = new ArrayList<>();
        final List<Course> elementList = new ArrayList<>();
        workList.add(elementList);
        //// END: Work structures.
        
        final int ROWS = studentMap.size();
        
        map.put(1, new ArrayList<Sequence>());
        
        // In the first iteration, find out all frequent 1-sequences.
        for (final Course course : courseList) {
            final int supportCount = supportCount(course);
            final double support = 1.0 * supportCount / studentMap.size();
            
            if (support >= minSupport) {
                elementList.clear();
                elementList.add(course);
                
                final Sequence sequence = new Sequence(workList);
                
                map.get(1).add(sequence);
                sigma.put(sequence, supportCount);
                
                seqToSupportMap.put(sequence, support);
            }
        }
        
        if (size <= 1) {
            final List<SequenceAndSupport> ret = 
                    extractSequences(map, seqToSupportMap);

            Collections.sort(ret);

            return ret;
        }
        
        System.out.println("Hfdsf: " + map.get(1).size());
        
        final Map<Student, Sequence> transactionMap = 
                new HashMap<>(studentMap.size());
        
        for (final Student student : studentMap.keySet()) {
            transactionMap.put(student, getStudentCoursesAsSequence(student));
        }
        
        int k = 1;
        
        do {
            ++k;
            
            System.out.println("Doing k = " + k);
            
            final List<Sequence> candidateList = 
                    generateSequenceCandidates(map.get(k - 1));
            
            System.out.println("Candidates: " + candidateList.size());
            
            for (final Student student : studentMap.keySet()) {
                final Sequence transaction = transactionMap.get(student);
                final List<Sequence> candidateList2 = subsequence(candidateList,
                                                                  transaction);
                
                for (final Sequence sequence : candidateList2) {
                    if (!sigma.containsKey(sequence)) {
                        sigma.put(sequence, 1);
                        seqToSupportMap.put(sequence, 1.0 / ROWS);
                    } else {
                        final int newSupportCount = sigma.get(sequence) + 1;
                        sigma.put(sequence, newSupportCount);
                        seqToSupportMap.put(sequence, 
                                            1.0 * newSupportCount / ROWS);
                    }
                }
            }
            
            map.put(k, getNextSequences(candidateList, sigma, minSupport));
        } while (k < size && map.get(k).size() > 0);
        
        final List<SequenceAndSupport> ret = 
                extractSequences(map, seqToSupportMap);
        
        Collections.sort(ret);
        
        return ret;
    }
    
    public List<SequenceAndSupport> sequentialApriori(final double minSupport) {
        final Map<Sequence, Double> seqToSupportMap = new HashMap<>();
        final Map<Sequence, Integer> sigma = new HashMap<>();
        final Map<Integer, List<Sequence>> map = new HashMap<>();
        
        //// BEGIN: Work structures.
        final List<List<Course>> workList = new ArrayList<>();
        final List<Course> elementList = new ArrayList<>();
        workList.add(elementList);
        //// END: Work structures.
        
        map.put(1, new ArrayList<Sequence>());
        
        // In the first iteration, find out all frequent 1-sequences.
        for (final Course course : courseList) {
            final int supportCount = supportCount(course);
            final double support = 1.0 * supportCount / studentMap.size();
            
            if (support >= minSupport) {
                elementList.clear();
                elementList.add(course);
                
                final Sequence sequence = new Sequence(workList);
                
                map.get(1).add(sequence);
                sigma.put(sequence, supportCount);
                
                seqToSupportMap.put(sequence, support);
            }
        }
        
        int k = 1;
        
        do {
            ++k;
            
            final List<Sequence> candidateList = 
                    generateSequenceCandidates(map.get(k - 1));
            
            for (final Student student : studentMap.keySet()) {
                final Sequence transaction 
                        = getStudentCoursesAsSequence(student);
                final List<Sequence> candidateList2 = subsequence(candidateList,
                                                                  transaction);
                
                for (final Sequence sequence : candidateList2) {
                    if (!sigma.containsKey(sequence)) {
                        sigma.put(sequence, 1);
                    } else {
                        sigma.put(sequence, sigma.get(sequence) + 1);
                    }
                }
            }
            
            map.put(k, getNextSequences(candidateList, sigma, minSupport));
        } while (map.get(k).size() > 0);
        
        final List<SequenceAndSupport> ret = 
                extractSequences(map, seqToSupportMap);
        
        Collections.sort(ret);
        
        return ret;
    }
    
    public List<AssociationRule> apriori(final double minSupport,
                                         final double minConfidence) {
        final Map<Integer, Set<Set<Course>>> map = new HashMap<>();
        final Map<Set<Course>, Integer> sigma = new HashMap<>();
        
        map.put(1, new HashSet<Set<Course>>());
        
        for (final Course course : getCourseList()) {
            final int supportCount = supportCount(course);
            final double support = 1.0 * supportCount / studentMap.size();
            
            if (support > minSupport) {
                final Set<Course> itemSet = new HashSet<>(1);
                itemSet.add(course);
                map.get(1).add(itemSet);
                sigma.put(itemSet, supportCount);
            }
        }
        
        int k = 1;
        
        do {
            ++k;
            
            System.out.println("Doing k = " + k);
            
            final Set<Set<Course>> candidateSet = 
                    generateCandidates(map.get(k - 1));
            
            System.out.println("Candidates: " + candidateSet.size());
            
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
            
            map.put(k, getNextItemsets(candidateSet, sigma, minSupport));
        } while (map.get(k).size() > 0);
        
        final Set<Set<Course>> frequentItemsets = extractItemSets(map);
        final List<AssociationRule> associationRules = new ArrayList<>();
        
        System.out.println("Frequent itemsets: " + frequentItemsets.size());
        
        //// Create all possible association rules (with one-element 
        //// consequents) out of frequent itemsets:
        final List<AssociationRule> inputRules = 
                extractRules1(frequentItemsets, sigma);
        
        System.out.println("Initial rules: " + inputRules.size());
        System.out.println("Initial frequent itemsets: " + frequentItemsets.size());
        
        //// Iterate through all frequent itemsets with at least two courses.
        for (final Set<Course> itemset : frequentItemsets) {
            if (itemset.size() < 2) {
                continue;
            }
            
            associationRules.addAll(generateAssociationRules(itemset,
                                                             inputRules,
                                                             sigma,
                                                             minConfidence));
        }
        
        return associationRules;
    }
    
    public List<AssociationRule> 
        extractRules1(final Set<Set<Course>> frequentItemsets,
                      final Map<Set<Course>, Integer> sigma) {
        final List<AssociationRule> ret = new ArrayList<>();
        final Set<AssociationRule> set = new HashSet<>();
        final Set<Course> workSet = new HashSet<>(1);
        
        for (final Set<Course> itemset : frequentItemsets) {
            if (itemset.size() < 2) {
                // Ignore the itemsets having less than 2 items, because they
                // may not represent association rules.
                continue;
            }
            
            for (final Course course : itemset) {
                final Set<Course> antecedent = new HashSet<>(itemset);
                final Set<Course> consequent = new HashSet<>(1);
                
                antecedent.remove(course);
                consequent.add(course);
                
                final int supportCount = sigma.get(itemset);
                final double support = 1.0 * supportCount 
                                           / studentMap.size();
                
                workSet.clear();
                workSet.add(course);
                
                final double confidence = 1.0 * supportCount 
                                              / sigma.get(workSet);
                workSet.clear();
                workSet.addAll(consequent);
                
                final double supportCountOfConsequent = sigma.get(workSet);
                
                workSet.clear();
                workSet.addAll(antecedent);
                
                final double supportCountOfAntecedent = sigma.get(workSet);
                final double lift = confidence / 
                                   (supportCountOfConsequent * 
                                    studentMap.size());
                final double isMeasure = 
                        1.0 * supportCount / 
                        Math.sqrt(supportCountOfConsequent * 
                                  supportCountOfAntecedent);
                                                
                final AssociationRule rule = new AssociationRule(antecedent,
                                                                 consequent,
                                                                 support,
                                                                 confidence,
                                                                 lift, 
                                                                 isMeasure);
                
                set.add(rule);
                ret.add(rule);
            }
        }
        
        System.out.println("extractRules1 - ret: " + ret.size() + " set: " + 
                           set.size());
        return ret;
    }
    
    private List<AssociationRule> 
        generateNextRules(final List<AssociationRule> ruleList) {
        final List<AssociationRule> ret = new ArrayList<>();
        final Set<AssociationRule> set = new HashSet<>();
        
        for (final AssociationRule rule : ruleList) {
            if (rule.getAntecedent().size() < 2) {
                continue;
            }
            
            for (final Course antecedentElement : rule.getAntecedent()) {
                final Set<Course> newAntecedent = 
                        new HashSet<>(rule.getAntecedent());
                
                final Set<Course> newConsequent =
                        new HashSet<>(rule.getConsequent().size() + 1);
                
                newAntecedent.remove(antecedentElement);
                newConsequent.addAll(rule.getConsequent());
                newConsequent.add(antecedentElement);
                
                final AssociationRule newRule = 
                        new AssociationRule(newAntecedent,
                                            newConsequent);
                
                ret.add(newRule);
                set.add(newRule);
            }
        }
        
        return new ArrayList<>(set);
    }
    
    private List<AssociationRule> 
        generateAssociationRules(final Set<Course> itemset,
                                 final List<AssociationRule> rules,
                                 final Map<Set<Course>, Integer> sigma,
                                 final double minConfidence) {
        final Set<AssociationRule> set = new HashSet<>();
        final int k = itemset.size();
        final int m = rules.get(0).getConsequent().size();
        final Set<Course> workSet = new HashSet<>(k);
        
//        System.out.println("k = " + k + ", m = " + m);
        
        if (k > m + 1) {
            final List<AssociationRule> nextRules = generateNextRules(rules);
//            System.out.println("cons: " + nextRules.get(0).getConsequent().size());
            final Iterator<AssociationRule> iterator = nextRules.iterator();
            
            while (iterator.hasNext()) {
                final AssociationRule rule = iterator.next();
                
                workSet.clear();
                workSet.addAll(itemset);
                workSet.removeAll(rule.getConsequent());
                
                final int supportCount = sigma.get(itemset);
                final double confidence = 1.0 * supportCount / 
                                                sigma.get(workSet);
                
                workSet.clear();
                workSet.addAll(rule.getConsequent());
                
                final int supportCountOfConsequent = sigma.get(workSet);
                
                workSet.clear();
                workSet.addAll(rule.getAntecedent());
                
                final int supportCountOfAntecedent = sigma.get(workSet);
                final double supportOfConsequent = 
                        1.0 * supportCountOfConsequent / studentMap.size();
                
                final double lift = confidence / supportOfConsequent;
                final double isMeasure = 
                        1.0 * supportCount / 
                        (Math.sqrt(supportCountOfConsequent *
                                   supportCountOfAntecedent));
                
                if (confidence >= minConfidence) {
                   final double support = 1.0 * supportCount 
                                              / studentMap.size();
                   rule.setSupport(support);
                   rule.setConfidence(confidence);
                   rule.setLift(lift);
                   rule.setISMeasure(isMeasure);
                   set.add(rule);
                } else {
                    // Remove 'rule'.
                    iterator.remove();
                }
            }
            
            final List<AssociationRule> rulesToAdd =
                    generateAssociationRules(itemset,
                                             nextRules,
                                             sigma,
                                             minConfidence);
            
            set.addAll(rulesToAdd);
        } 
        
        return new ArrayList<>(set);
    }
    
    public Set<Set<Course>> 
        computeClosedFrequentPatterns(final double minSupport) {
        final Map<Integer, Set<Set<Course>>> map = new HashMap<>();
        final Map<Set<Course>, Integer> sigma = new HashMap<>();
        
        map.put(1, new HashSet<Set<Course>>());
        
        for (final Course course : getCourseList()) {
            final int supportCount = supportCount(course);
            final double support = 1.0 * supportCount / studentMap.size();
            
            if (support > minSupport) {
                final Set<Course> itemSet = new HashSet<>(1);
                itemSet.add(course);
                map.get(1).add(itemSet);
                sigma.put(itemSet, supportCount);
            }
        }
        
        int k = 1;
        
        do {
            ++k;
            
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
        
        final Set<Set<Course>> patternSet = extractItemSets(map);
        final Set<Set<Course>> closedFrequentPatternSet = 
                new HashSet<>(patternSet.size());
        final Set<Course> workSet = new HashSet<>();
        
        outer:
        for (final Set<Course> pattern : patternSet) {
            workSet.clear();
            workSet.addAll(pattern);
            final int supportCount = sigma.get(workSet);
            
            for (final Course course : courseList) {
                if (!pattern.contains(course)) {
                    workSet.add(course);
                    
                    final int supersetSupportCount = sigma.get(workSet);
                    
                    if (supersetSupportCount == supportCount) {
                        continue outer;
                    }
                    
                    workSet.remove(course);
                }
            }
            
            closedFrequentPatternSet.add(pattern);
        }
        
        return extractItemSets(map);
    }
        
    public Set<Set<Course>> 
        computeMaximalFrequentItemsets(final double minSupport) {
        final Set<Set<Course>> frequentItemsets = apriori(minSupport);
        final Map<Integer, List<Set<Course>>> map = new HashMap<>();
      
        for (final Set<Course> itemset : frequentItemsets) {
            final int size = itemset.size();
            
            if (!map.containsKey(size)) {
                map.put(size, new ArrayList<Set<Course>>());
            }
            
            map.get(size).add(itemset);
        }
        
        final List<Integer> list = new ArrayList<>(map.keySet());
        Collections.sort(list);
        return new HashSet<>(map.get(list.get(list.size() - 1)));
    }
        
    public Set<Set<Course>> apriori(final double minSupport) {
        final Map<Integer, Set<Set<Course>>> map = new HashMap<>();
        final Map<Set<Course>, Integer> sigma = new HashMap<>();
        
        map.put(1, new HashSet<Set<Course>>());
        
        for (final Course course : getCourseList()) {
            final int supportCount = supportCount(course);
            final double support = 1.0 * supportCount / studentMap.size();
            
            if (support > minSupport) {
                final Set<Course> itemSet = new HashSet<>(1);
                itemSet.add(course);
                map.get(1).add(itemSet);
                sigma.put(itemSet, supportCount);
            }
        }
        
        int k = 1;
        
        do {
            ++k;
            
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
        
    private List<SequenceAndSupport> 
        extractSequences(final Map<Integer, List<Sequence>> map,
                         final Map<Sequence, Double> seqToSupportMap) {
        final List<SequenceAndSupport> ret = new ArrayList<>();
        
        for (final List<Sequence> sequences : map.values()) {
            for (final Sequence sequence : sequences) {
                ret.add(new SequenceAndSupport(sequence,
                                               seqToSupportMap.get(sequence)));
            }
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
                        final double minSupport) {
        final Set<Set<Course>> ret = new HashSet<>(candidateSet.size());
        
        for (final Set<Course> itemset : candidateSet) {
            if (sigma.containsKey(itemset)) {
                final int supportCount = sigma.get(itemset);
                final double support = 1.0 * supportCount / studentMap.size();
                
                if (support >= minSupport) {
                    ret.add(itemset);
                }
            }
        }
        
        return ret;
    }

    private List<Sequence> 
        getNextSequences(final List<Sequence> candidateList, 
                         final Map<Sequence, Integer> sigma, 
                         final double minSupport) {
        final List<Sequence> ret = new ArrayList<>(candidateList.size());
        final int ROWS = studentMap.keySet().size();
        
        for (final Sequence sequence : candidateList) {
            if (sigma.get(sequence) != null
                    && 1.0 * sigma.get(sequence) / ROWS >= minSupport) {
                ret.add(sequence);
            }
        }
        
        return ret;
    }

    private Sequence 
        getStudentCoursesAsSequenceWithTimes(final Student student) {
        final List<CourseAttendanceEntry> entryList = studentMap.get(student);
        final List<List<Course>> mainList = new ArrayList<>();
        
        Collections.<CourseAttendanceEntry>sort(entryList);
        
        int year = entryList.get(0).getYear();
        int month = entryList.get(0).getMonth();
        
        List<Course> workList = new ArrayList<>();
        workList.add(entryList.get(0).getCourse());
        
        for (int i = 1; i < entryList.size(); ++i) {
            final CourseAttendanceEntry currentEntry = entryList.get(i);
            
            if (currentEntry.getMonth() == month 
                    && currentEntry.getYear() == year) {
                workList.add(currentEntry.getCourse());
            } else {
                mainList.add(workList);
                workList = new ArrayList<>();
                workList.add(currentEntry.getCourse());
                
                year = currentEntry.getYear();
                month = currentEntry.getMonth();
            }
        }
        
        return new Sequence(mainList);
    }

    private Sequence getStudentCoursesAsSequence(final Student student) {
        final List<CourseAttendanceEntry> entryList = studentMap.get(student);
        final List<List<Course>> mainList = new ArrayList<>();
        
        Collections.<CourseAttendanceEntry>sort(entryList);
        
        int year = entryList.get(0).getYear();
        int month = entryList.get(0).getMonth();
        
        List<Course> workList = new ArrayList<>();
        workList.add(entryList.get(0).getCourse());
        
        for (int i = 1; i < entryList.size(); ++i) {
            final CourseAttendanceEntry currentEntry = entryList.get(i);
            
            if (currentEntry.getMonth() == month 
                    && currentEntry.getYear() == year) {
                workList.add(currentEntry.getCourse());
            } else {
                mainList.add(workList);
                workList = new ArrayList<>();
                workList.add(currentEntry.getCourse());
                
                year = currentEntry.getYear();
                month = currentEntry.getMonth();
            }
        }
        
        return new Sequence(mainList);
    }

    private List<Sequence> subsequence(final List<Sequence> candidateList, 
                                       final Sequence transaction,
                                       final Student owner,
                                       final int maxspan) {
        final List<Sequence> ret = new ArrayList<>(candidateList.size());
        
        for (final Sequence sequence : candidateList) {
            if (sequence.isContainedIn(transaction)) {
                loadTimestamps(sequence, owner);
                
                if (sequence.fitsInSpan(maxspan)) {
                    ret.add(sequence);
                }
            }
        }
        
        return ret;
    }
    
    private void loadTimestamps(final Sequence sequence,
                                final Student owner) {
        int first = Integer.MAX_VALUE;
        int last = Integer.MIN_VALUE;
       
        for (final Course course : sequence) {
            final List<CourseAttendanceEntry> list = 
                    matrix.get(owner).get(course);
            
            final CourseAttendanceEntry entry = list.get(list.size() - 1);
            final int time = 12 * entry.getYear() + entry.getMonth() - 1;
            
            first = Math.min(first, time);
            last = Math.max(last, time);
        }
        
        sequence.setFirstEventStart(first);
        sequence.setLastEventEnd(last);
    }

    private List<Sequence> subsequence(final List<Sequence> candidateList, 
                                       final Sequence transaction) {
        final List<Sequence> ret = new ArrayList<>(candidateList.size());
        
        for (final Sequence sequence : candidateList) {
            if (sequence.isContainedIn(transaction)) {
                ret.add(sequence);
            }
        }
        
        return ret;
    }
        
    /**
     * Sorts the courses such that those courses that had been done earlier end
     * up in the beginning of the list.
     */
    private static final class CourseComparatorByEntries 
    implements Comparator<Course> {

        private final Map<Course, List<CourseAttendanceEntry>> map;
        
        CourseComparatorByEntries(
                final Map<Course, List<CourseAttendanceEntry>> map) {
            this.map = map;
        }
        
        @Override
        public int compare(final Course c1, final Course c2) {
            final CourseAttendanceEntry e1 = findMostRecentEntry(c1);
            final CourseAttendanceEntry e2 = findMostRecentEntry(c2);
            return e1.isEarlierThan(e2) ? -1 : 1;
        }
        
        private CourseAttendanceEntry findMostRecentEntry(final Course course) {
            final List<CourseAttendanceEntry> entryList = map.get(course);
            
            CourseAttendanceEntry mostRecent = entryList.get(0);
            
            for (int i = 1; i < entryList.size(); ++i) {
                final CourseAttendanceEntry current = entryList.get(i);
                
                if (mostRecent.isEarlierThan(current)) {
                    mostRecent = current;
                }
            }
            
            return mostRecent;
        }
    }
    
    private List<Sequence> 
        generateSequenceCandidates(final List<Sequence> input,
                                   final int maxspan) {
        final List<Sequence> outputList = new ArrayList<>();
        final int inputSequenceAmount = input.size();
        
        for (int i1 = 0; i1 < inputSequenceAmount; ++i1) {
            final Sequence s1 = input.get(i1);
            final Sequence s1aux = s1.dropFirstEvent();
            
            for (int i2 = 0; i2 < inputSequenceAmount; ++i2) {
                if (i1 == i2) {
                    continue;
                }
                
                final Sequence s2 = input.get(i2);
                final Sequence s2aux = s2.dropLastEvent();
                
                if (s1aux.equals(s2aux)) {
                    final Sequence out = mergeSequencesWithTimestamps(s1, s2);
                    
                    if (out.fitsInSpan(maxspan)) {
                        outputList.add(out);
                    } 
                }
            }
        }
        
        return outputList;
    }
        
    private List<Sequence> 
        generateSequenceCandidates(final List<Sequence> input) {
        final List<Sequence> outputList = new ArrayList<>();
        final int inputSequenceAmount = input.size();
        
        for (int i1 = 0; i1 < inputSequenceAmount; ++i1) {
            final Sequence s1 = input.get(i1);
            final Sequence s1aux = s1.dropFirstEvent();
            
            for (int i2 = 0; i2 < inputSequenceAmount; ++i2) {
                if (i1 == i2) {
                    continue;
                }
                
                final Sequence s2 = input.get(i2);
                final Sequence s2aux = s2.dropLastEvent();
                
                if (s1aux.equals(s2aux)) {
                    outputList.add(mergeSequences(s1, s2));
                }
            }
        }
        
        return outputList;
    }
      
    private Sequence mergeSequencesWithTimestamps(final Sequence s1, 
                                                  final Sequence s2) {
        final Course lastEvent = s2.getLastEvent();
        
        final int start = Math.min(s1.getFirstEventStart(),
                                   s2.getFirstEventStart());
        
        final int end = Math.max(s1.getLastEventEnd(),
                                 s2.getLastEventEnd());
        
        switch (s2.getMergeType()) {
            case Sequence.SEPARATE:
                return new Sequence(s1, lastEvent, false, start, end);
                
            case Sequence.TOGETHER:
                return new Sequence(s1, lastEvent, true, start, end);
                
            default:
                throw new IllegalStateException(
                        "Unknown merge type: " + s2.getMergeType());
        }
    }
    
    private Sequence mergeSequences(final Sequence s1, final Sequence s2) {
        final Course lastEvent = s2.getLastEvent();
        
        switch (s2.getMergeType()) {
            case Sequence.SEPARATE:
                return new Sequence(s1, lastEvent, false);
                
            case Sequence.TOGETHER:
                return new Sequence(s1, lastEvent, true);
                
            default:
                throw new IllegalStateException(
                        "Unknown merge type: " + s2.getMergeType());
        }
    }
    
    private Set<CourseDate> getCourseDatesOfStudent(final Student student) {
        final List<CourseAttendanceEntry> entryList = studentMap.get(student);
        final Set<CourseDate> ret = new HashSet<>(entryList.size());
        
        for (final CourseAttendanceEntry entry : entryList) {
            ret.add(new CourseDate(entry));
        }
        
        return ret;
    }
    
    public static class Result1 {
        public final Map<Integer, List<Float>> map1;
        public final Map<Integer, Integer> map2;
        
        public Result1(final Map<Integer, List<Float>> map1,
                       final Map<Integer, Integer> map2) {
            this.map1 = map1;
            this.map2 = map2;
        }
    }
    
    public Result1 getCreditsToGPA() {
        final Map<Integer, List<Float>> ret = new HashMap<>();
        final Map<Integer, Integer> ret2 = new HashMap<>();
        
        for (final Student student : studentMap.keySet()) {
            final List<List<CourseAttendanceEntry>> periodList = 
                    getStudentSchedule(student);
            
            for (final List<CourseAttendanceEntry> period : periodList) {
                final float gpa = computeGPA(period);
                final int credits = countCredits(period);
                
                if (!ret.containsKey(credits)) {
                    ret.put(credits, new ArrayList<Float>());
                    ret2.put(credits, 1);
                }
                
                ret.get(credits).add(gpa);
                ret2.put(credits, ret2.get(credits) + 1);
            }
        }
        
        return new Result1(ret, ret2);
    }
    
    private static float mean(final List<Float> x) {
        float sum = 0.0f;
        
        for (final float f : x) {
            sum += f;
        }
        
        return sum / x.size();
    }
    
    public static float correlation(final List<Float> x, final List<Float> y) {
        final float meanx = mean(x);
        final float meany = mean(y);
        
        float upperSum = 0.0f;
        float lowerSum1 = 0.0f;
        float lowerSum2 = 0.0f;
        
        for (int i = 0; i < x.size(); ++i) {
            upperSum += (x.get(i) - meanx) * (y.get(i) - meany);
            lowerSum1 += Math.pow(x.get(i) - meanx, 2.0f);
            lowerSum2 += Math.pow(y.get(i) - meany, 2.0f);
        }
        
        return (float)(upperSum / Math.sqrt(lowerSum1 * lowerSum2));
    }
    
    public float average(final List<Float> floatList) {
        float sum = 0f;
        
        for (final float f : floatList) {
            sum += f;
        }
        
        return sum / floatList.size();
    }
    
    private List<List<CourseAttendanceEntry>> 
        getStudentSchedule(final Student student) {
        final Set<CourseDate> dateSet = getCourseDatesOfStudent(student);
        final Map<CourseDate, List<CourseAttendanceEntry>> map =
                new HashMap<>(dateSet.size());
        
        final List<List<CourseAttendanceEntry>> ret = new ArrayList<>();
        final List<CourseAttendanceEntry> entryList = studentMap.get(student);
        
        for (final CourseAttendanceEntry entry : entryList) {
            final CourseDate tmp = new CourseDate(entry);
            
            if (!map.containsKey(tmp)) {
                map.put(tmp, new ArrayList<CourseAttendanceEntry>());
            }
            
            map.get(tmp).add(entry);
        }
        
        for (final List<CourseAttendanceEntry> list : map.values()) {
            ret.add(prune(list));
        }
        
        return ret;
    }
     
    /**
     * For each course, if there is multiple course attendance entries, the one
     * with the greatest grade is retained.
     * 
     * @param  entryList the list to prune from multiple same-course 
     *                    attendancies.
     * @return the pruned list.
     */
    private List<CourseAttendanceEntry> 
        prune(final List<CourseAttendanceEntry> entryList) {
        final List<CourseAttendanceEntry> ret = 
                new ArrayList<>(entryList.size());
        
        final Map<Course, List<CourseAttendanceEntry>> map = 
                new HashMap<>(entryList.size());
        
        for (final CourseAttendanceEntry entry : entryList) {
            final Course course = entry.getCourse();
            
            if (!map.containsKey(course)) {
                map.put(course, new ArrayList<CourseAttendanceEntry>());
            }
            
            map.get(course).add(entry);
        }
        
        final EntryComparatorByGrade entryComparatorByGrade =
                new EntryComparatorByGrade();
        
        for (final List<CourseAttendanceEntry> list : map.values()) {
            Collections.sort(list, entryComparatorByGrade);
            ret.add(list.get(0));
        }
        
        return ret;
    }
        
    private static class EntryComparatorByGrade 
    implements Comparator<CourseAttendanceEntry> {

        @Override
        public int compare(CourseAttendanceEntry o1, CourseAttendanceEntry o2) {
            return -Integer.compare(o1.getGrade(), o2.getGrade());
        }
    }
        
    private float computeGPA(final List<CourseAttendanceEntry> entryList) {
        final int credits = countCredits(entryList);
        int sum = 0;
        
        for (final CourseAttendanceEntry entry : entryList) {
            if (entry.getCourse().getGradingMode() == 
                    Course.GRADING_MODE_NORMAL_SCALE) {
                sum += entry.getGrade() * entry.getCourse().getCredits();
            }
        }
        
        return 1.0f * sum / credits;
    }
        
    private int countCredits(final List<CourseAttendanceEntry> entryList) {
        final Set<Course> courseSet = new HashSet<>(entryList.size());
        
        for (final CourseAttendanceEntry entry : entryList) {
            if (entry.getCourse().getGradingMode() 
                    == Course.GRADING_MODE_NORMAL_SCALE) {
                courseSet.add(entry.getCourse());
            }
        }
        
        int credits = 0;
        
        for (final Course course : courseSet) {
            credits += course.getCredits();
        }
        
        return credits;
    }
    
    static class CourseDate {
        
        private final int year;
        private final int month;
        
        CourseDate(final int year, final int month) {
            this.year = year;
            this.month = month;
        }
        
        CourseDate(final CourseAttendanceEntry entry) {
            this(entry.getYear(), entry.getMonth());
        }
        
        public int getYear() {
            return year;
        }
        
        public int getMonth() {
            return month;
        }
        
        @Override
        public boolean equals(final Object o) {
            final CourseDate cd = (CourseDate) o;
            return getYear() == cd.getYear() && getMonth() == cd.getMonth();
        }

        @Override
        public int hashCode() {
            return 12 * getYear() + getMonth() - 1;
        }
    }
    
    public int supportCount(final Set<Course> courseSet, 
                            final int minGrade, 
                            final int maxGrade) {
        int supportCount = 0;
        
        for (final Student student : studentMap.keySet()) {
            final Set<Course> studentsCourseSet = 
                    new HashSet<>(studentToCourseListMap.get(student));
            
            for (final Course course : courseSet) {
                if (studentsCourseSet.contains(course)
                        && hasGrade(student, course, minGrade, maxGrade)) {
                    ++supportCount;
                }
            }
        }
        
        return supportCount;
    }
    
    public float week5Task16() {
        final List<Student> targetStudents = new ArrayList<>();
        final Course intro = getCourseByName("Ohjelmoinnin perusteet");
        final Course adv = getCourseByName("Ohjelmoinnin jatkokurssi");
        
        for (final Student student : studentMap.keySet()) {
            if (student.getRegistrationYear() > 2010) {
                final int grade = grade(student, intro);
                
                if (grade >= 1 && grade <= 3
                        && hasGrade(student, adv, 0, 5)) {
                    targetStudents.add(student);
                }
            }
        }
        
        int gradeSum = 0;
        
        for (final Student student : targetStudents) {
            gradeSum += grade(student, adv);
        }
        
        return 1.0f * gradeSum / targetStudents.size();
    }
    
    public float week5Task17() {
        final List<Student> targetStudents = new ArrayList<>();
        final Course intro = getCourseByName("Ohjelmoinnin perusteet");
        final Course algo = getCourseByName("Tietorakenteet ja algoritmit");
        
        intro.getCode();
        algo.getCode();
        
        for (final Student student : studentMap.keySet()) {
            if (student.getRegistrationYear() > 2010) {
                final int grade = grade(student, intro);
                
                if (grade >= 4 && grade <= 5
                        && hasGrade(student, algo, 0, 5)) {
                    targetStudents.add(student);
                }
            }
        }
        
        int gradeSum = 0;
        
        for (final Student student : targetStudents) {
            gradeSum += grade(student, algo);
        }
        
        return 1.0f * gradeSum / targetStudents.size();
    }
    
    public float week5Task18() {
        final List<Student> targetStudents = new ArrayList<>();
        final Course intro = getCourseByName("Ohjelmoinnin perusteet");
        final Course algo = getCourseByName("Tietorakenteet ja algoritmit");
        
        intro.getCode();
        algo.getCode();
        
        for (final Student student : studentMap.keySet()) {
            if (student.getRegistrationYear() < 2010) {
                final int grade = grade(student, intro);
                
                if (grade >= 4 && grade <= 5
                        && hasGrade(student, algo, 0, 5)) {
                    targetStudents.add(student);
                }
            }
        }
        
        int gradeSum = 0;
        
        for (final Student student : targetStudents) {
            gradeSum += grade(student, algo);
        }
        
        return 1.0f * gradeSum / targetStudents.size();
    }
}
