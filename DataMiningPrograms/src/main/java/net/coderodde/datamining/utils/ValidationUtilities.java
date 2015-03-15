package net.coderodde.datamining.utils;

/**
 * This class contains static methods for validation.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class ValidationUtilities {
    
    /**
     * Checks that <code>num</code> is not infinite in magnitude. If it is, an
     * exception is thrown with the error message <code>errorMessage</code>.
     * 
     * @param num          the number to validate.
     * @param errorMessage the error message to pass upon validation failure.
     * 
     * @throws IllegalArgumentException if validation fails.
     */
    public static final void checkNotInfinite(final float num,
                                              final String errorMessage) {
        if (Float.isInfinite(num)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    /**
     * Checks that<code>num</code> is no less than <code>lowerBound</code>.
     * 
     * @param num          the floating-point number to validate.
     * @param lowerBound   the lower bound.
     * @param errorMessage the error message passed upon failure.
     * 
     * @throws IllegalArgumentException if validation fails.
     */
    public static final void checkNotLess(final float num, 
                                          final float lowerBound,
                                          final String errorMessage) {
        if (num < lowerBound) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    /**
     * Checks that <code>num</code> is no less than <code>lowerBound</code>.
     * 
     * @param num          the integer to validate.
     * @param lowerBound   the lower bound.
     * @param errorMessage the error message passed upon failure.
     * 
     * @throws IllegalArgumentException if validation fails.
     */
    public static final void checkNotLess(final int num,
                                          final int lowerBound,
                                          final String errorMessage) {
        if (num < lowerBound) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    /**
     * Checks that <code>num</code> is no more than <code>upperBound</code>.
     * 
     * @param num          the integer to validate.
     * @param upperBound   the upper bound.
     * @param errorMessage the error message passed upon failure.
     * 
     * @throws IllegalArgumentException if validation fails.
     */
    public static final void checkNotMore(final int num,
                                          final int upperBound,
                                          final String errorMessage) {
        if (num > upperBound) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    /**
     * Checks that <code>num</code> is not NaN. If it is, an exception is thrown
     * with the error message <code>errorMessage</code>.
     * 
     * @param num                       the number to validate.
     * @param errorMessage              the error message to pass upon 
     *                                  validation failure.
     * @throws IllegalArgumentException if validation fails.
     */
    public static final void checkNotNaN(final float num,
                                         final String errorMessage) {
        if (Float.isNaN(num)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
     
    /**
     * Checks that <code>obj</code> is not null. If it is, an exception is
     * thrown with the error message <code>errorMessage</code>.
     * 
     * @param obj                       the reference to validate.
     * @param errorMessage              the error message to pass upon 
     *                                  validation failure.
     * @throws IllegalArgumentException if validation fails.
     */
    public static final void checkNotNull(final Object obj,
                                          final String errorMessage) {
        if (obj == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
