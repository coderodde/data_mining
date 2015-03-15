package net.coderodde.datamining.model;

import static net.coderodde.datamining.model.Course.createCourse;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class CourseTest {
    
    private static final String COURSE_NAME1 = "Parallel kewlness optimization";
    private static final String COURSE_NAME2 = "Kewl programming";
    private static final String COURSE_CODE1 = "13337";
    private static final String COURSE_CODE2 = "13377";
    
    private static final boolean COURSE_GRADING1 = 
            Course.GRADING_MODE_PASS_FAIL;
    
    private static final boolean COURSE_GRADING2 = 
            Course.GRADING_MODE_NORMAL_SCALE;
    
    private static final float COURSE_CREDITS1 = 3.0f;
    private static final float COURSE_CREDITS2 = 4.5f;
    
    private Course course1;
    private Course course2;
    
    public CourseTest() {
        course1 = createCourse()
                  .withName(COURSE_NAME1)
                  .withCode(COURSE_CODE1)
                  .withBinaryScale()
                  .withCredits(COURSE_CREDITS1);
        
        course2 = createCourse()
                  .withName(COURSE_NAME2)
                  .withCode(COURSE_CODE2)
                  .withNormalScale()
                  .withCredits(COURSE_CREDITS2);
    }

    @Test
    public void testGetName() {
        assertEquals(COURSE_NAME1, course1.getName());
        assertEquals(COURSE_NAME2, course2.getName());
    }

    @Test
    public void testGetCode() {
        assertEquals(COURSE_CODE1, course1.getCode());
        assertEquals(COURSE_CODE2, course2.getCode());
    }

    @Test
    public void testGetGradingMode() {
        assertEquals(COURSE_GRADING1, course1.getGradingMode());
        assertEquals(COURSE_GRADING2, course2.getGradingMode());
    }

    @Test
    public void testGetCredits() {
        assertEquals(COURSE_CREDITS1, course1.getCredits(), 0.0f);
        assertEquals(COURSE_CREDITS2, course2.getCredits(), 0.0f);
    }

    @Test
    public void testEquals() {
        assertFalse(course1.equals(course2));
        final Course copy = createCourse()
                            .withName("Copy")
                            .withCode(course1.getCode())
                            .withNormalScale()
                            .withCredits(10.0f);
        assertTrue(course1.equals(copy));
        assertFalse(course1.equals(""));
        assertFalse(course1.equals(null));
    }

    @Test
    public void testHashCode() {
        assertFalse(course1.hashCode() == course2.hashCode());
        final Course copy = createCourse()
                            .withName("N/A")
                            .withCode(COURSE_CODE1)
                            .withNormalScale()
                            .withCredits(4.2f);
        
        // hash codes same on same course codes?
        assertTrue(course1.hashCode() == copy.hashCode());
        assertEquals(course1.hashCode(), course1.hashCode());
        assertEquals(course2.hashCode(), course2.hashCode());
    }
}
