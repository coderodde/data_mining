package net.coderodde.datamining.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Rodion Efremov
 * @version 1.6
 */
public class Utils {
    
    /**
     * This static method returns the intersection of all the argument sets.
     * 
     * @param <T>  the element type.
     * @param sets the array of sets.
     * @return     the set containing the intersection of all the argument sets.
     */
    public static <T> Set<T> intersect(final Set<T>... sets) {
        if (sets.length == 0) {
            return Collections.<T>emptySet();
        }
        
        final Set<T> set = new HashSet<>(sets[0].size());
        set.addAll(sets[0]);
        
        final Iterator<T> iterator = set.iterator();
        
        outer:
        while (iterator.hasNext()) {
            final T element = iterator.next();
            
            for (int i = 1; i < sets.length; ++i) {
                if (!sets[i].contains(element)) {
                    iterator.remove();
                    continue outer;
                }
            }
        }
        
        return set;
    }
    
    /**
     * This static method returns the intersection of all the argument lists.
     * 
     * @param <T>   the element type.
     * @param lists the array of lists.
     * @return      the set containing the intersection of all the argument 
     *              lists.
     */
    public static <T> Set<T> intersect(final List<T>... lists) {
        if (lists.length == 0) {
            return Collections.<T>emptySet();
        }
        
        final Set<T>[] sets = new HashSet[lists.length];
        
        for (int i = 0; i < lists.length; ++i) {
            sets[i] = new HashSet<>(lists[i].size());
            sets[i].addAll(lists[i]);
        }
        
        return intersect(sets);
    }
    
    /**
     * Returns <code>true</code> if <code>set1</code> is contained in 
     * <code>set2</code>.
     * 
     * @param <T>  the set element type.
     * @param set1 the first set.
     * @param set2 the second set.
     * @return <code>true</code> if <code>set1</code> is contained entirely
     *         in <code>set2</code>. 
     */
    public static <T> boolean containsAll(final Set<T> set1, 
                                          final Set<T> set2) {
        for (final T element : set1) {
            if (!set2.contains(element)) {
                return false;
            }
        }
        
        return true;
    }
}
