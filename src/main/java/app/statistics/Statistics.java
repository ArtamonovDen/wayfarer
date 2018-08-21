package app.statistics;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;

public class Statistics implements Serializable {
    public Calendar date;
    public BigInteger total = BigInteger.ZERO;
    public BigInteger critical = BigInteger.ZERO;

    public BigInteger created =BigInteger.ZERO;
    public BigInteger reopen = BigInteger.ZERO;
    public BigInteger move = BigInteger.ZERO;

    public BigInteger resolved= BigInteger.ZERO;
    //public BigInteger outMove;
    //public Padding?

    public int getWeek(){
        return date.getWeeksInWeekYear();
    }
    public BigInteger getTotalIncome(){

        return created.add(reopen).add(move);
    }
    public BigInteger getTotalOutcome(){
        return resolved;
    }


}
