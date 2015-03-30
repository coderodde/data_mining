package net.coderodde.datamining.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import static net.coderodde.datamining.model.Course.createCourse;
import org.junit.Test;
import static org.junit.Assert.*;

public class SequenceTest {
    
    private final Course course1;
    private final Course course2;
    private final Course course3;
    private final Course course4;
    private final Course course5;
    
    private final Random rnd = new Random();
    
    public SequenceTest() {
        this.course1 = createCourse().withName("Course I")
                                     .withCode("1")
                                     .withNormalScale()
                                     .withCredits(4.0f);
        
        this.course2 = createCourse().withName("Course II")
                                     .withCode("2")
                                     .withNormalScale()
                                     .withCredits(4.0f);
        
        this.course3 = createCourse().withName("Course III")
                                     .withCode("3")
                                     .withNormalScale()
                                     .withCredits(4.0f);
        
        this.course4 = createCourse().withName("Course IV")
                                     .withCode("4")
                                     .withNormalScale()
                                     .withCredits(4.0f);
        
        this.course5 = createCourse().withName("Course V")
                                     .withCode("5")
                                     .withNormalScale()
                                     .withCredits(4.0f);
    }

    @Test
    public void testDropFirstEvent() {
        System.out.println("--- testDropFirstEvent");
        final List<List<Course>> list = new ArrayList<>();
        
        final List<Course> element1 = new ArrayList<>();
        final List<Course> element2 = new ArrayList<>();
        
        element1.add(course1);
        element2.add(course2);
        element2.add(course3);
        
        list.add(element1);
        list.add(element2);
        
        Sequence seq = new Sequence(list);
        
        System.out.println("Original:      " + seq);
        System.out.println("Without first: " + seq.dropFirstEvent());
        System.out.println();
        
        list.clear();
        element1.clear();
        element2.clear();
        
        element1.add(course1);
        element1.add(course4);
        element2.add(course2);
        element2.add(course3);
        
        list.add(element1);
        list.add(element2);
        
        seq = new Sequence(list);
        
        System.out.println("Original:      " + seq);
        System.out.println("Without first: " + seq.dropFirstEvent());
    }

    @Test
    public void testDropLastEvent() {
        System.out.println("--- testDropLastEvent");
        final List<List<Course>> list = new ArrayList<>();
        
        final List<Course> element1 = new ArrayList<>();
        final List<Course> element2 = new ArrayList<>();
        
        element1.add(course1);
        element1.add(course2);
        element2.add(course3);
        
        list.add(element1);
        list.add(element2);
        
        Sequence seq = new Sequence(list);
        
        System.out.println("Original:      " + seq);
        System.out.println("Without first: " + seq.dropLastEvent());
        System.out.println();
        
        list.clear();
        element1.clear();
        element2.clear();
        
        element1.add(course1);
        element1.add(course4);
        element2.add(course2);
        element2.add(course3);
        
        list.add(element1);
        list.add(element2);
        
        seq = new Sequence(list);
        
        System.out.println("Original:      " + seq);
        System.out.println("Without first: " + seq.dropLastEvent());
    }

    @Test
    public void testGetLastEvent() {
        System.out.println("--- testGetLastEvent");
        
        final List<List<Course>> list = new ArrayList<>();
        
        final List<Course> element1 = new ArrayList<>();
        final List<Course> element2 = new ArrayList<>();
        
        element1.add(course1);
        element1.add(course2);
        element2.add(course3);
        
        list.add(element1);
        list.add(element2);
        
        Sequence seq = new Sequence(list);
        
        System.out.println("Last event: " + seq.getLastEvent());
        
        element1.clear();
        element1.add(course1);
        element2.add(course4);
        
        seq = new Sequence(list);
        
        System.out.println("Last event: " + seq.getLastEvent());
    }

    @Test
    public void testGetMergeType() {
    }

    @Test
    public void testIsContainedIn() {
        System.out.println("--- testIsContainedIn");
        final List<List<Course>> list = new ArrayList<>();
        
        final List<Course> element1 = new ArrayList<>();
        final List<Course> element2 = new ArrayList<>();
        final List<Course> element3 = new ArrayList<>();
        
        element1.add(course1);
        element1.add(course2);
        element2.add(course3);
        element3.add(course5);
        element3.add(course4);
        
        list.add(element1);
        list.add(element2);
        list.add(element3);
        
        Sequence seqLarge = new Sequence(list);
        
        System.out.println("Large sequence: " + seqLarge);
        
        list.clear();
        
        element1.clear();
        element2.clear();
        element3.clear();
        
        element1.add(course1);
        element2.add(course3);
        element3.add(course4);
        
        list.add(element1);
        list.add(element2);
        list.add(element3);
        
        Sequence seqSmall = new Sequence(list);
        
        System.out.println("Small sequence: " + seqSmall);
        
        System.out.println(
                "Small in large: " + seqSmall.isContainedIn(seqLarge));
        
        assertTrue(seqSmall.isContainedIn(seqLarge));
        
        list.clear();
        element1.clear();
        element2.clear();
        element3.clear();
        
        element1.add(course1);
        element2.add(course4);
        
        list.add(element1);
        list.add(element2);
        
        seqSmall = new Sequence(list);
        
        System.out.println("Small sequence: " + seqSmall);
        
        System.out.println(
                "Small in large: " + seqSmall.isContainedIn(seqLarge));
        
        assertTrue(seqSmall.isContainedIn(seqLarge));
        
        list.clear();
        element1.clear();
        element2.clear();
        element3.clear();
        
        element1.add(course4);
        element2.add(course3);
        
        list.add(element1);
        list.add(element2);
        
        seqSmall = new Sequence(list);
        
        System.out.println("Small sequence: " + seqSmall);
        
        System.out.println(
                "Small in large: " + seqSmall.isContainedIn(seqLarge));
        
        assertFalse(seqSmall.isContainedIn(seqLarge));
    }

    @Test
    public void testHashCode() {
        System.out.println("--- testHashCode");
        final List<List<Course>> list = new ArrayList<>();
        
        final List<Course> element1 = new ArrayList<>();
        final List<Course> element2 = new ArrayList<>();
        
        list.add(element1);
        list.add(element2);
        
        element1.add(course2);
        element2.add(course4);
        
        Sequence seq = new Sequence(list);
        
        int hash1 = seq.hashCode();
        
        element1.clear();
        element1.add(course1);
        
        seq = new Sequence(list);
        
        int hash2 = seq.hashCode();
        
        list.clear();
        element1.clear();
        element2.clear();
        
        element1.add(course2);
        element2.add(course4);
        
        list.add(element1);
        list.add(element2);
        
        seq = new Sequence(list);
        
        int hash3 = seq.hashCode();
        
        System.out.println("hash1: " + hash1);
        System.out.println("hash2: " + hash2);
        System.out.println("hash3: " + hash3);
        
        assertTrue(hash1 != hash2);
        assertTrue(hash2 != hash3);
        assertEquals(hash1, hash3);
    }

    @Test
    public void testEquals() {
        List<List<Course>> list = new ArrayList<>();
        
        List<Course> element1 = new ArrayList<>();
        List<Course> element2 = new ArrayList<>();
        
        element1.add(course4);
        element2.add(course1);
        element2.add(course2);
        
        list.add(element1);
        list.add(element2);
        
        Sequence seq1 = new Sequence(list);
        
        list = new ArrayList<>();
        
        element1 = new ArrayList<>();
        element2 = new ArrayList<>();
        
        element1.add(course4);
        element2.add(course2);
        
        list.add(element1);
        list.add(element2);
        
        Sequence seq2 = new Sequence(list);
        
        assertFalse(seq1.equals(seq2));
        assertFalse(seq2.equals(seq1));
        assertTrue(seq1.equals(seq1));
        assertTrue(seq2.equals(seq2));
        
        list.clear();
        element1.clear();
        element2.clear();
        
        element1.add(course4);
        element2.add(course2);
        element2.add(course1);
        
        list.add(element1);
        list.add(element2);
        
        Sequence seq3 = new Sequence(list);
        
        assertTrue(seq1.equals(seq3));
        assertTrue(seq3.equals(seq1));
        assertTrue(seq1.equals(seq1));
        assertTrue(seq3.equals(seq3));
    }

    @Test
    public void testIterator() {
        final List<List<Course>> list = new ArrayList<>();
        
        final List<Course> element1 = new ArrayList<>();
        final List<Course> element2 = new ArrayList<>();
        final List<Course> element3 = new ArrayList<>();
        final List<Course> element4 = new ArrayList<>();
        
        element1.add(course4);
        element2.add(course3);
        element3.add(course5);
        element3.add(course1);
        element4.add(course2);
        element4.add(course3);
        
        list.add(element1);
        list.add(element2);
        list.add(element3);
        list.add(element4);
        
        final Sequence seq = new Sequence(list);
        final Iterator<Course> it = seq.iterator();
        
        assertEquals(course4, it.next());
        assertEquals(course3, it.next());
        assertEquals(course1, it.next());
        assertEquals(course5, it.next());
        assertEquals(course2, it.next());
        assertEquals(course3, it.next());
        
        assertFalse(it.hasNext());
    }

    @Test
    public void testCompareTo() {
        
    }
}
