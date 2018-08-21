package app.statistics;

import java.io.Serializable;
import java.math.BigInteger;


public class StatisticsPerWeek implements Serializable {
    public int year;
    public int week;
    public BigInteger total = BigInteger.ZERO;
    public BigInteger critical =BigInteger.ZERO;

    public BigInteger created = BigInteger.ZERO;
    public BigInteger reopen = BigInteger.ZERO;
    public BigInteger move = BigInteger.ZERO;

    public BigInteger resolved = BigInteger.ZERO;
    //public BigInteger outMove;
    //public Padding?

    public BigInteger getTotalIncome() {

        return created.add(reopen).add(move);
    }

    public BigInteger getTotalOutcome() {
        return resolved;
    }
    //TODO add move | two kinds of move

    @Override
    public String toString() {
//        return year+" "+week+" "+total+" "+"critical: "+critical+" \n Income:"+created+" "+reopen+" "+move+" total: "+this.getTotalIncome()+"\n outcome: "+
//                resolved+" total:"+this.getTotalOutcome();
        return "S "+year+" "+week +" tot:"+ total +" critical :" +critical+"\n Income: Created:"+created+" Reopen"+reopen+" Move"+move+" total: "+this.getTotalIncome()+"\n Outcome: Resolved"+
                resolved+" total:"+this.getTotalOutcome();
    }
}