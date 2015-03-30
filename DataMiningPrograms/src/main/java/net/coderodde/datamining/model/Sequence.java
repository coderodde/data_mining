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
implements Iterable<Course>, Comparable<Sequence> {
        
        public static final int SEPARATE = 1;
        public static final int TOGETHER = 2;
        
        /**
         * The actual sequence.
         */
        private final List<List<Course>> sequence;
        
        public Sequence(final List<List<Course>> sequence) {
            this.sequence = new ArrayList<>(sequence.size());
            
            for (final List<Course> element : sequence) {
                this.sequence.add(new ArrayList<>(element));
            }
        }
        
        public Sequence(final Course event) {
            final List<List<Course>> elementList
                    = new ArrayList<>();
            
            final List<Course> eventList = new ArrayList<>();
            eventList.add(event);
            elementList.add(eventList);
            
            this.sequence = elementList;
        }
        
        public Sequence(final Sequence s,
                        final Course event, 
                        final boolean doMerge) {
            this.sequence = new ArrayList<>(s.sequence.size() + 1);
            
            for (final List<Course> element : s.sequence) {
                this.sequence.add(new ArrayList<>(element));
            }
            
            if (doMerge) {
                final int elements = this.sequence.size();
                final List<Course> lastElement 
                        = this.sequence.get(elements - 1);
                
                lastElement.add(event);
                Collections.<Course>sort(lastElement);
            } else {
                final List<Course> lastElement 
                        = new ArrayList<>(1);
                
                lastElement.add(event);
                this.sequence.add(lastElement);
            }
        }
        
        public Sequence dropFirstEvent() {
            final List<List<Course>> newSequence = 
                    new ArrayList<>(sequence.size());
            
            for (final List<Course> element : sequence) {
                newSequence.add(new ArrayList<>(element));
            }
            
            newSequence.get(0).remove(0);
            
            if (newSequence.get(0).isEmpty()) {
                newSequence.remove(0);
            }
            
            return new Sequence(newSequence);
        }
        
        public Sequence dropLastEvent() {
            final List<List<Course>> newSequence = 
                    new ArrayList<>(sequence.size());
            
            for (final List<Course> element : sequence) {
                newSequence.add(new ArrayList<>(element));
            }
            
            final List<Course> lastElement = 
                    newSequence.get(newSequence.size() - 1);
            
            lastElement.remove(lastElement.size() - 1);
            
            if (lastElement.isEmpty()) {
                newSequence.remove(newSequence.size() - 1);
            }
            
            return new Sequence(newSequence);
        }
        
        public Course getLastEvent() {
            final List<Course> lastElement = sequence.get(sequence.size() - 1);
            return lastElement.get(lastElement.size() - 1);
        }
        
        public int getMergeType() {
            final List<Course> lastElement = sequence.get(sequence.size() - 1);
            return lastElement.size() > 1 ? TOGETHER : SEPARATE;
        }
        
        @Override
        public int hashCode() {
            return sequence.hashCode();
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Sequence)) {
                return false;
            }
            
            final Sequence other = (Sequence) o;
            final Iterator<Course> iter1 = iterator();
            final Iterator<Course> iter2 = other.iterator();
            
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
                
                final List<Course> eventList = sequence.get(i);
                final int eventAmount = eventList.size();
                
                for (int j = 0; j < eventAmount; ++j) {
                    sb.append(eventList.get(j).toString());
                    
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
        public Iterator<Course> iterator() {
            return new SequenceIterator();
        }

        @Override
        public int compareTo(Sequence o) {
            final Iterator<Course> iter1 = iterator();
            final Iterator<Course> iter2 = o.iterator();
            
            while (iter1.hasNext()) {
                if (!iter2.hasNext()) {
                    return 1;
                }
                
                final Course e1 = iter1.next();
                final Course e2 = iter2.next();
                
                if (e1.equals(e2)) {
                    continue;
                }
                
                return e1.compareTo(e2);
            }
            
            return iter2.hasNext() ? -1 : 0;
        }
        
        private class SequenceIterator 
        implements Iterator<Course> {
            
            private int globalIndex;
            private int localIndex;
            private int left;
            
            SequenceIterator() {
                this.left = 0;
                
                for (final List<Course> element : Sequence.this.sequence) {
                    left += element.size();
                }
            }

            @Override
            public boolean hasNext() {
                return left > 0;
            }

            @Override
            public Course next() {
                final List<Course> element = 
                        Sequence.this.sequence.get(globalIndex);
                
                final Course ret = element.get(localIndex++);
                
                if (element.size() == localIndex) {
                    localIndex = 0;
                    ++globalIndex;
                }
                
                --left;
                return ret;
            }
        }
    }
