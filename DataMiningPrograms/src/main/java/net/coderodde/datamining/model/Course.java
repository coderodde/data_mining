package net.coderodde.datamining.model;

import static net.coderodde.datamining.utils.ValidationUtilities.checkNotInfinite;
import static net.coderodde.datamining.utils.ValidationUtilities.checkNotLess;
import static net.coderodde.datamining.utils.ValidationUtilities.checkNotNaN;
import static net.coderodde.datamining.utils.ValidationUtilities.checkNotNull;

/**
 * This class encapsulates information for representing a course.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class Course {
    
    /**
     * The value of the least grade. This grade represents failure to pass a
     * course.
     */
    public static final int COURSE_FAILED_GRADE = 0;
    
    /**
     * The value of the best grade.
     */
    public static final int MAXIMUM_COURSE_GRADE = 5;
    
    /**
     * The grading mode of any course that assigns only <tt>pass/fail</tt> to
     * each participant.
     */
    public static final boolean GRADING_MODE_PASS_FAIL = false;
    
    /**
     * The grading mode of any course that assigns an integer grade within the 
     * interval <tt>[1, 5]</tt> to students having passed the course, or 
     * <tt>0</tt> to those students that have failed the course.
     */
    public static final boolean GRADING_MODE_NORMAL_SCALE = true;
    
    /**
     * Holds the name of the course.
     */
    private final String name;
    
    /**
     * Holds the course code.
     */
    private final String code;
    
    /**
     * Holds the grading mode of this course.
     */
    private final boolean gradingMode;
    
    /**
     * The amount of credits this course offers upon completion.
     */
    private final float credits;
    
    public Course(final String name,
                  final String code,
                  final boolean gradingMode,
                  final float credits) {
        checkNotNull(name, "The name of a course is null.");
        checkNotNull(code, "The code of a course is null.");
        
        checkNotNaN(credits, "The credit amount is NaN");
        checkNotInfinite(credits, "The credit amount is infinite in absolute " +
                                  "value.");
        checkNotLess(credits, 
                     0.0f, 
                     "Credit amount too small. Received: " + credits + ". " +
                     "Should be at least 0.0");
        
        this.name = name;
        this.code = code;
        this.gradingMode = gradingMode;
        this.credits = credits;
    }
    
    public static NameSelector createCourse() {
        return new NameSelector();
    }
    
    public String getName() {
        return name;
    }
    
    public String getCode() {
        return code;
    }
    
    public boolean getGradingMode() {
        return gradingMode;
    }
    
    public float getCredits() {
        return credits;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Course)) {
            return false;
        }
        
        return getCode().equals(((Course) obj).getCode());
    }
    
    @Override
    public int hashCode() {
        return getCode().hashCode();
    }
    
    @Override
    public String toString() {
        return "[" + name + ", " + code + ", " + 
                (gradingMode == GRADING_MODE_PASS_FAIL ? 
                 "binary scale, " : "normal scale, ") +
                credits + " credits]";
    }
    
    public static final class NameSelector {
        
        public CodeSelector withName(final String name) {
            return new CodeSelector(name);
        }
    }
    
    public static final class CodeSelector {
        
        private final String name;
        
        public CodeSelector(final String name) {
            this.name = name;
        }
        
        public GradingModeSelector withCode(final String code) {
            return new GradingModeSelector(name, code);
        }
    }
    
    public static final class GradingModeSelector {
        
        private final String name;
        private final String code;
        
        public GradingModeSelector(final String name, final String code) {
            this.name = name;
            this.code = code;
        }
        
        public CreditsSelector withNormalScale() {
            return new CreditsSelector(name, 
                                       code, 
                                       GRADING_MODE_NORMAL_SCALE);
        }
        
        public CreditsSelector withBinaryScale() {
            return new CreditsSelector(name, 
                                       code,
                                       GRADING_MODE_PASS_FAIL);
        }
    }
    
    public static final class CreditsSelector {
        
        private final String name;
        private final String code;
        private final boolean gradingMode;
        
        public CreditsSelector(final String name, 
                               final String code,
                               final boolean gradingMode) {
            this.name = name;
            this.code = code;
            this.gradingMode = gradingMode;
        }
        
        public Course withCredits(final float credits) {
            return new Course(name, code, gradingMode, credits);
        }
    }
}
