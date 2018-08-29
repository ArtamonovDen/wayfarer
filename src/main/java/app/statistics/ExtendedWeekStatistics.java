package app.statistics;

import app.entity.WeekStatistics;

import java.io.Serializable;
import java.math.BigInteger;

public class ExtendedWeekStatistics implements Serializable {

    public BigInteger total = BigInteger.ZERO;
    public BigInteger critical =BigInteger.ZERO;
    public WeekStatistics weekStat;
}
