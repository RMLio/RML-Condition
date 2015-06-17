/***************************************************************************
 * 
 * RML - Conditions : TermMap Interface
 * 
 * @author andimou
 * 
 ****************************************************************************/
package be.ugent.mmlab.rml.condition.model;

import be.ugent.mmlab.rml.model.TermMap;
import java.util.HashSet;


public interface conditionalTermMap extends TermMap{
        
        /**
        *
        * @return
        */
       public String getSplit();
        
        /**
        *
        * @return
        */
       public String getProcess();

        /**
        *
        * @return
        */
       public String getReplace();
        
        /**
        *
        * @return
        */
       public HashSet<BooleanCondition> getBooleanConditions();
        
        /**
        *
        * @return
        */
       public HashSet<ProcessCondition> getProcessConditions();
        
        /**
        *
        * @return
        */
       public HashSet<SplitCondition> getSplitConditions();
        
        /**
        *
        * @return
        */
       public Condition getCondition();


}
