package net.coderodde.datamining.model;

import static net.coderodde.datamining.model.Student.createStudent;
import org.junit.Test;
import static org.junit.Assert.*;

public class StudentTest {
    
    private final Student student1;
    private final Student student2;
    
    public StudentTest() {
        student1 = createStudent()
                   .withId(1)
                   .withRegistrationYear(2008);
        
        student2 = createStudent()
                   .withId(2)
                   .withRegistrationYear(2008);
    }

    @Test
    public void testGetId() {
        assertEquals(1, student1.getId());
        assertEquals(2, student2.getId());
    }

    @Test
    public void testGetRegistrationYear() {
        assertEquals(2008, student1.getRegistrationYear());
        assertEquals(2008, student2.getRegistrationYear());
    }

    @Test
    public void testEquals() {
        assertFalse(student1.equals(student2));
        assertFalse(student1.equals("string"));
        assertFalse(student1.equals(null));
        assertEquals(student2, createStudent()
                               .withId(2)
                               .withRegistrationYear(2011));
    }

    @Test
    public void testHashCode() {
        assertFalse(student1.hashCode() == student2.hashCode());
        assertTrue(student1.hashCode() == createStudent()
                                          .withId(1)
                                          .withRegistrationYear(2010)
                                          .hashCode());
    }
}
