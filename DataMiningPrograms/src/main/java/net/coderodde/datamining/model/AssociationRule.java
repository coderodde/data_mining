package net.coderodde.datamining.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AssociationRule {

    private final Set<Course> antecedent;
    private final Set<Course> consequent;
    private final double support;
    private final double confidence;
    
    public AssociationRule(final Set<Course> antecedent,
                           final Set<Course> consequent,
                           final double support,
                           final double confidence) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.support = support;
        this.confidence = confidence;
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
                 .toString();
    }
}
