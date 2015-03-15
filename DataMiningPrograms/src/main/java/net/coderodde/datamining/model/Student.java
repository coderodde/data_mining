package net.coderodde.datamining.model;

/**
 * This class represents a student in rather anonymous fashion.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class Student {

    /**
     * The ID of this student. The ID must not equal the ID of any other
     * student.
     */
    private final int id;
    
    /**
     * The registration year of this student.
     */
    private final int registrationYear;
    
    public Student(final int id, final int registrationYear) {
        this.id = id;
        this.registrationYear = registrationYear;
    }
    
    public static IdSelector createStudent() {
        return new IdSelector();
    }
    
    public int getId() {
        return id;
    }
    
    public int getRegistrationYear() {
        return registrationYear;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Student)) {
            return false;
        }
        
        return getId() == ((Student) obj).getId();
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    private static final class IdSelector {
        
        public static RegistrationYearSelector withId(final int id) {
            return new RegistrationYearSelector(id);
        }
    }
    
    private static final class RegistrationYearSelector {
        
        private final int id;
        
        private RegistrationYearSelector(final int id) {
            this.id = id;
        }
        
        public Student withRegistrationYear(final int registrationYear) {
            return new Student(id, registrationYear);
        }
    }
}
