package app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "assignee_statistics")
public class AssigneeStatistics extends EntityObject {
    public AssigneeStatistics(){}
    public AssigneeStatistics(int id){
        this.id = id;
    }
    @Id
    @Column(name = "assignee_id", nullable = false)
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "income")
    private int income=0;
    public int getIncome() {
        return income;
    }
    public void setIncome(int income) { this.income = income;}

    @Column(name = "resolved")
    private int resolved=0;
    public int getResolved() {
        return resolved;
    }
    public void setResolved(int resolved) { this.resolved = resolved;}

    @Column(name = "moved")
    private int moved=0;
    public int getMoved() {
        return moved;
    }
    public void setMoved(int moved) { this.moved = moved;}

    public void incIncome(){
        income++;
    }
    public void incResolved(){
        resolved++;
    }
    public void incMoved(){
        moved++;
    }
    public void inc(Status status){
        switch (status){
            case Open:
            case Move_income:income++;return;
            case Move: moved++;return;
            case Resolved: resolved++;return;
        }
    }

    public int compareTo(AssigneeStatistics as){
        if(this.id< as.id) return 1;
        else
            return -1;
    }


}
