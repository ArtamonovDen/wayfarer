package app.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class WeekStatisticsKey implements Serializable {
    public WeekStatisticsKey() {
    }
    public WeekStatisticsKey(int year, int week) {
        setWeek(week);
        setYear(year);
    }

    @Column(name = "year")
    private int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Column(name = "week")
    private int week;

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        try {
            WeekStatisticsKey nKey = (WeekStatisticsKey) obj;
            return (nKey.week == this.week && nKey.year == this.year);
        }catch (Exception e){return false;}
    }

    @Override
    public String toString() {
        return "Year: "+year+" week: "+week;
    }
}