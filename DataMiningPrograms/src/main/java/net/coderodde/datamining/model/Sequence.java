package net.coderodde.datamining.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a sequence.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class Sequence 
implements Iterable<CourseAttendanceEntry>,  
           Comparable<Sequence> {
        
        public static final int SEPARATE = 1;
        public static final int TOGETHER = 2;
        
        /**
         * The actual sequence.
         */
        private final List<List<CourseAttendanceEntry>> sequence;
        
        public Sequence(final List<List<CourseAttendanceEntry>> sequence) {
            this.sequence = new ArrayList<>(sequence.size());
            
            for (final List<CourseAttendanceEntry> element : sequence) {
                this.sequence.add(new ArrayList<>(element));
            }
        }
        
        public Sequence(final Sequence s,
                        final CourseAttendanceEntry event, 
                        final boolean doMerge) {
            this.sequence = new ArrayList<>(s.sequence.size() + 1);
            
            for (final List<CourseAttendanceEntry> element : s.sequence) {
                this.sequence.add(new ArrayList<>(element));
            }
            
            if (doMerge) {
                final int elements = this.sequence.size();
                final List<CourseAttendanceEntry> lastElement = 
                        this.sequence.get(elements - 1);
                
                lastElement.add(event);
                Collections.<CourseAttendanceEntry>sort(lastElement);
            } else {
                final List<CourseAttendanceEntry> lastElement 
                        = new ArrayList<>(1);
                
                lastElement.add(event);
                this.sequence.add(lastElement);
            }
        }
        
        public Sequence dropFirstEvent() {
            final List<List<CourseAttendanceEntry>> newSequence = 
                    new ArrayList<>(sequence.size());
            
            for (final List<CourseAttendanceEntry> element : sequence) {
                newSequence.add(new ArrayList<>(element));
            }
            
            newSequence.get(0).remove(0);
            
            if (newSequence.get(0).isEmpty()) {
                newSequence.remove(0);
            }
            
            return new Sequence(newSequence);
        }
        
        public Sequence dropLastEvent() {
            final List<List<CourseAttendanceEntry>> newSequence = 
                    new ArrayList<>(sequence.size());
            
            for (final List<CourseAttendanceEntry> element : sequence) {
                newSequence.add(new ArrayList<>(element));
            }
            
            final List<CourseAttendanceEntry> lastElement = 
                    newSequence.get(newSequence.size() - 1);
            
            lastElement.remove(lastElement.size() - 1);
            
            if (lastElement.isEmpty()) {
                newSequence.remove(newSequence.size() - 1);
            }
            
            return new Sequence(newSequence);
        }
        
        public CourseAttendanceEntry getLastEvent() {
            final List<CourseAttendanceEntry> lastElement 
                    = sequence.get(sequence.size() - 1);
            
            return lastElement.get(lastElement.size() - 1);
        }
        
        public int getMergeType() {
            final List<CourseAttendanceEntry> lastElement 
                    = sequence.get(sequence.size() - 1);
            
            return lastElement.size() > 1 ? TOGETHER : SEPARATE;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Sequence)) {
                return false;
            }
            
            final Sequence other = (Sequence) o;
            final Iterator<CourseAttendanceEntry> iter1 = iterator();
            final Iterator<CourseAttendanceEntry> iter2 = other.iterator();
            
            while (iter1.hasNext()) {
                if (!iter2.hasNext()) {
                    return false;
                }
                
                if (!iter1.next().equals(iter2.next())) {
                    return false;
                }
            }
            
            return !iter2.hasNext();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            final int elementAmount = sequence.size();
            
            for (int i = 0; i < elementAmount; ++i) {
                sb.append('{');
                
                final List<CourseAttendanceEntry> eventList = sequence.get(i);
                final int eventAmount = eventList.size();
                
                for (int j = 0; j < eventAmount; ++j) {
                    sb.append(eventList.get(j));
                    
                    if (j < eventAmount - 1) {
                        sb.append(',');
                    }
                }
                
                sb.append('}');
                
                if (i < elementAmount - 1) {
                    sb.append(',');
                }
            }
            
            return sb.toString();
        }

        @Override
        public Iterator<CourseAttendanceEntry> iterator() {
            return new SequenceIterator();
        }

        @Override
        public int compareTo(Sequence o) {
            final Iterator<CourseAttendanceEntry> iter1 = iterator();
            final Iterator<CourseAttendanceEntry> iter2 = o.iterator();
            
            while (iter1.hasNext()) {
                if (!iter2.hasNext()) {
                    return 1;
                }
                
                final CourseAttendanceEntry e1 = iter1.next();
                final CourseAttendanceEntry e2 = iter2.next();
                
                if (e1.equals(e2)) {
                    continue;
                }
                
                return e1.compareTo(e2);
            }
            
            return iter2.hasNext() ? -1 : 0;
        }
        
        private class SequenceIterator 
        implements Iterator<CourseAttendanceEntry> {
            
            private int globalIndex;
            private int localIndex;
            private int left;
            
            SequenceIterator() {
                this.left = 0;
                
                for (final List<CourseAttendanceEntry> element : 
                     Sequence.this.sequence) {
                    left += element.size();
                }
            }

            @Override
            public boolean hasNext() {
                return left > 0;
            }

            @Override
            public CourseAttendanceEntry next() {
                final List<CourseAttendanceEntry> element = 
                        Sequence.this.sequence.get(globalIndex);
                
                final CourseAttendanceEntry ret = element.get(localIndex++);
                
                if (element.size() == localIndex) {
                    localIndex = 0;
                    ++globalIndex;
                }
                
                --left;
                return ret;
            }
        }
    }
