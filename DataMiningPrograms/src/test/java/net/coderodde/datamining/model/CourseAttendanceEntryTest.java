package net.coderodde.datamining.model;

import static net.coderodde.datamining.model.Course.createCourse;
import static net.coderodde.datamining.model.CourseAttendanceEntry.createAttendanceEntry;
import static net.coderodde.datamining.model.Student.createStudent;
import org.junit.Test;
import static org.junit.Assert.*;

public class CourseAttendanceEntryTest {
    
    private final CourseAttendanceEntry entry;
    private final Student student;
    private final Student anotherStudent;
    private final Course course;
    private final Course anotherCourse;
    
    public CourseAttendanceEntryTest() {
        student = createStudent()
                  .withId(2)
                  .withRegistrationYear(2008);
        
        course = createCourse()
                 .withName("Programming")
                 .withCode("1234")
                 .withNormalScale()
                 .withCredits(4.0f);
        
        entry = createAttendanceEntry()
                .withStudent(student)
                .withCourse(course)
                .withYear(2009)
                .withMonth(9)
                .withGrade(4);
        
        anotherStudent = createStudent()
                         .withId(10)
                         .withRegistrationYear(2006);
        
        anotherCourse = createCourse()
                        .withName("How to represent")
                        .withCode("3546")
                        .withBinaryScale()
                        .withCredits(3.0f);
    }

    @Test
    public void testGetStudent() {
        assertEquals(student, entry.getStudent());
    }

    @Test
    public void testGetCourse() {
        assertEquals(course, entry.getCourse());
    }

    @Test
    public void testGetYear() {
        assertEquals(2009, entry.getYear());
    }

    @Test
    public void testGetMonth() {
        assertEquals(9, entry.getMonth());
    }

    @Test
    public void testGetGrade() {
        assertEquals(4, entry.getGrade());
    }
    
    @Test
    public void testEquals() {
        final CourseAttendanceEntry entry1 = 
                createAttendanceEntry()
                .withStudent(student)
                .withCourse(course)
                .withYear(2009)
                .withMonth(9)
                .withGrade(5);
        
        assertTrue(entry.equals(entry1));
        
        final CourseAttendanceEntry entry2 =
                createAttendanceEntry()
                .withStudent(student)
                .withCourse(course)
                .withYear(2009)
                .withMonth(9)
                .withGrade(4);
        
        assertTrue(entry.equals(entry2));
        
        final CourseAttendanceEntry entry3 =
                createAttendanceEntry()
                .withStudent(student)
                .withCourse(course)
                .withYear(2008)
                .withMonth(9)
                .withGrade(4);
        
        assertFalse(entry.equals(entry3));
        
        final CourseAttendanceEntry entry4 =
                createAttendanceEntry()
                .withStudent(student)
                .withCourse(course)
                .withYear(2009)
                .withMonth(8)
                .withGrade(4);
        
        assertFalse(entry.equals(entry4));
        
        final CourseAttendanceEntry entry5 =
                createAttendanceEntry()
                .withStudent(anotherStudent)
                .withCourse(course)
                .withYear(2009)
                .withMonth(9)
                .withGrade(4);
        
        assertFalse(entry.equals(entry5));
        
        final CourseAttendanceEntry entry6 =
                createAttendanceEntry()
                .withStudent(student)
                .withCourse(anotherCourse)
                .withYear(2009)
                .withMonth(9)
                .withGrade(4);
        
        assertFalse(entry.equals(entry6));
    }
    
    @Test
    public void testHashCode() {
        final CourseAttendanceEntry entry1 = 
                createAttendanceEntry()
                .withStudent(student)
                .withCourse(course)
                .withYear(2009)
                .withMonth(9)
                .withGrade(2);
        
        assertEquals(entry.hashCode(), entry1.hashCode());
        
        final CourseAttendanceEntry entry2 = 
                createAttendanceEntry()
                .withStudent(anotherStudent)
                .withCourse(course)
                .withYear(2009)
                .withMonth(9)
                .withGrade(4);
        
        assertFalse(entry.hashCode() == entry2.hashCode());
        
        final CourseAttendanceEntry entry3 = 
                createAttendanceEntry()
                .withStudent(student)
                .withCourse(anotherCourse)
                .withYear(2009)
                .withMonth(9)
                .withGrade(4);
        
        assertFalse(entry.hashCode() == entry3.hashCode());
        
        final CourseAttendanceEntry entry4 = 
                createAttendanceEntry()
                .withStudent(student)
                .withCourse(course)
                .withYear(2010)
                .withMonth(9)
                .withGrade(4);
        
        assertFalse(entry.hashCode() == entry4.hashCode());
        
        final CourseAttendanceEntry entry5 = 
                createAttendanceEntry()
                .withStudent(student)
                .withCourse(course)
                .withYear(2009)
                .withMonth(7)
                .withGrade(4);
        
        assertFalse(entry.hashCode() == entry5.hashCode());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsOnNullStudent() {
        createAttendanceEntry()
                .withStudent(null)
                .withCourse(course)
                .withYear(2010)
                .withMonth(3)
                .withGrade(4);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsOnNullCourse() {
        createAttendanceEntry()
                .withStudent(student)
                .withCourse(null)
                .withYear(2010)
                .withMonth(3)
                .withGrade(4);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsOnToSmallMonth() {
        createAttendanceEntry()
                .withStudent(student)
                .withCourse(course)
                .withYear(2010)
                .withMonth(0)
                .withGrade(4);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsOnToLargeMonth() {
        createAttendanceEntry()
                .withStudent(student)
                .withCourse(course)
                .withYear(2010)
                .withMonth(13)
                .withGrade(4);
    }
    
    @Test
    public void testDoesNotThrowOnValidMonths() {
        for (int month = 1; month <= 12; ++month) {
            createAttendanceEntry()
                    .withStudent(student)
                    .withCourse(course)
                    .withYear(2010)
                    .withMonth(month)
                    .withGrade(4);
        }
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testThrowsOnTooSmallGrade() {
        createAttendanceEntry()
                .withStudent(student)
                .withCourse(course)
                .withYear(2010)
                .withMonth(13)
                .withGrade(-1);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testThrowsOnTooLargeGrade() {
        createAttendanceEntry()
                .withStudent(student)
                .withCourse(course)
                .withYear(2010)
                .withMonth(13)
                .withGrade(6);
    }
    
    @Test
    public void testDoesNotThrowOnValidGrades() {
        for (int grade = 0; grade <= 5; ++grade) {
            createAttendanceEntry()
                    .withStudent(student)
                    .withCourse(course)
                    .withYear(2010)
                    .withMonth(10)
                    .withGrade(grade);
        }
    }
}
