package app.entity;

import app.statistics.ExtendedWeekStatistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "extendedAssigneeStatistics")
public class ExtendedAssigneeStatistics extends EntityObject {
    public ExtendedAssigneeStatistics() {  }

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
    private int income = 0;

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    @Column(name = "resolved")
    private int resolved = 0;

    public int getResolved() {
        return resolved;
    }

    public void setResolved(int resolved) {
        this.resolved = resolved;
    }

    @Column(name = "moved")
    private int moved = 0;

    public int getMoved() {
        return moved;
    }

    public void setMoved(int moved) {
        this.moved = moved;
    }

    @Column(name = "fio")
    private String fio;
    public String getFio() {
        return fio;
    }
    public void setFio(String fio) {
        this.fio = fio;
    }

    @Column(name="depart_name")
    private String departName;

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public int compareTo(ExtendedAssigneeStatistics as){
        if(this.id< as.id) return -1;
        else
            return 1;
    }
}