package net.coderodde.datamining.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AssociationRule {

    private final Set<Course> antecedent;
    private final Set<Course> consequent;
    private double support;
    private double confidence;
    private double lift;
    private double isMeasure;
    
    public AssociationRule(final Set<Course> antecedent,
                           final Set<Course> consequent) {
        this.antecedent = antecedent;
        this.consequent = consequent;
    }
    
    public AssociationRule(final Set<Course> antecedent,
                           final Set<Course> consequent,
                           final double support,
                           final double confidence,
                           final double lift,
                           final double isMeasure) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.support = support;
        this.confidence = confidence;
        this.isMeasure = isMeasure;
    }
    
    public Set<Course> getAntecedent() {
        return Collections.unmodifiableSet(antecedent);
    }
    
    public Set<Course> getConsequent() {
        return Collections.unmodifiableSet(consequent);
    }
    
    public double getSupport() {
        return support;
    }
    
    public double getConfidence() {
        return confidence;
    }
    
    public double getLift() {
        return lift;
    }
    
    public double getISMeasure() {
        return isMeasure;
    }
    
    public void setSupport(final double support) {
        this.support = support;
    }
    
    public void setConfidence(final double confidence) {
        this.confidence = confidence;
    }
    
    public void setLift(final double lift) {
        this.lift = lift;
    }
    
    public void setISMeasure(final double isMeasure) {
        this.isMeasure = isMeasure;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        
        final List<Course> antecedentList = new ArrayList<>(antecedent);
        final List<Course> consequentList = new ArrayList<>(consequent);
        
        for (int i = 0; i < antecedentList.size(); ++i) {
            sb.append(antecedentList.get(i).getName());
            
            if (i < antecedentList.size() - 1) {
                sb.append(", ");
            }
        }
        
        sb.append("} --> {");
        
        for (int i = 0; i < consequentList.size(); ++i) {
            sb.append(consequentList.get(i).getName());
            
            if (i < consequentList.size() - 1) {
                sb.append(", ");
            }
        }
        
        return sb.append("} support: ")
                 .append(support)
                 .append(", confidence: ")
                 .append(confidence)
                 .append(", lift: ")
                 .append(lift)
                 .append(", IS: ")
                 .append(isMeasure)
                 .toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        final AssociationRule other = (AssociationRule) o;
        return antecedent.equals(other.antecedent) 
                && consequent.equals(other.consequent);
    }
    
    @Override
    public int hashCode() {
        return antecedent.hashCode() ^ consequent.hashCode();
    }
}
