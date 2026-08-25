package age.of.civilizations2.jakowski.lukasz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event_Trigger implements Serializable {
   private static final long serialVersionUID = 0L;
   public List<Event_Conditions> lConditions = new ArrayList<>();
   public Event_Type triggerType = Event_Type.AND;
   public boolean isDisplayCondition = false;

   public final boolean getTriggerOut() {
      for (int i = 0; i < this.lConditions.size(); i++) {
         if (this.lConditions.get(i).conditionType == Event_Type.OR && this.lConditions.get(i).outCondition()) {
            return true;
         }
      }

      for (int ix = 0; ix < this.lConditions.size(); ix++) {
         if (this.lConditions.get(ix).conditionType != Event_Type.OR) {
            if (this.lConditions.get(ix).conditionType == Event_Type.AND) {
               if (!this.lConditions.get(ix).outCondition()) {
                  return false;
               }
            } else if (this.lConditions.get(ix).conditionType == Event_Type.NOT && this.lConditions.get(ix).outCondition()) {
               return false;
            }
         }
      }

      return true;
   }

   public final String getTriggerText() {
      String out = "";

      for (int i = 0; i < this.lConditions.size() & i < 5; i++) {
         out = out + "" + this.lConditions.get(i).getConditionText() + " ";
      }

      return out;
   }
}
