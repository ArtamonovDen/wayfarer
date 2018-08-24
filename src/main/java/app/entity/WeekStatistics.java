package app.entity;

import net.bytebuddy.implementation.HashCodeMethod;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "week_statistics")
public class WeekStatistics extends EntityObject{

    public WeekStatistics(){}

    @EmbeddedId
    private WeekStatisticsKey key;
    public WeekStatisticsKey getKey(){return this.key;}
    public void setKey(WeekStatisticsKey key) { this.key = key; }

    @Column(name="created")
    private int created;
    public int getCreated() {return created;}
    public void setCreated(int created) {this.created = created;}

    @Column(name="moved_income")
    private int moved_income;
    public int getMoved_income() {return moved_income;}
    public void setMoved_income(int moved_income) {this.moved_income = moved_income;}

    @Column(name="reopened")
    private int reopened;
    public int getReopened() {return reopened;}
    public void setReopened(int reopened) {this.reopened = reopened;}

    @Column(name="closed")
    private int closed;
    public int getClosed() {return closed;}
    public void setClosed(int closed) {this.closed = closed;}

    @Column(name="moved")
    private int moved;
    public int getMoved() {return moved;}
    public void setMoved(int moved) {this.moved = moved;}



    public void inc(Status sourceStatus){
        switch (sourceStatus){
            case Open: created++;return;
            case Closed: closed++;return;
            case Move: moved++; return;
            case Reopen: reopened++;return;
            case Move_income:moved_income++;return;
            default:return;

        }
    }

    public int getTotalIncome(){
        return created+moved_income+reopened;

    }
    public int getTotalOutcome(){
        return closed+moved;

    }

    public int compareTo(WeekStatistics ws){
        if(this.getKey().getYear() == ws.getKey().getYear()){
            if( this.getKey().getWeek()<ws.getKey().getWeek())
                return -1;
            else return 1;

        }
        else{

            if (this.getKey().getYear() < ws.getKey().getYear())
                return -1;
            else return 1;
        }
    }

}

