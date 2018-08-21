package app.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

;

@Entity
@Table(name  = "tasks")

public class Task extends EntityObject {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "task_id",nullable = false)
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    @Column(name ="task_name",nullable = false)
    public String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "description")
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    @Column(name = "priority",nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;
    public Priority getPriority() {
        return priority;
    }
    public void setPriority(Priority priority) {
        this.priority = priority;
    }


    @Column(name = "depart_id",nullable = false)
    private int depart_id;
    public int getDepart_id(){return depart_id;}
    public void setDepart_id(int depart_id){this.depart_id = depart_id;}



    @Column(name = "customer_id",nullable = false)
    private int customer_id;
    public int getCustomer_id() {
        return customer_id;
    }
    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }


    @Column(name = "assignee")
    private int assignee;
    public int getAssignee() {
        return assignee;
    }
    public void setAssignee(int assignee) {
        this.assignee = assignee;
    }


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("Open")
    private Status status;
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }



    @Column(name  = "createdate",nullable = false)
    @Temporal(TemporalType.DATE)
    private java.util.Date createdate;
    public java.util.Date getCreatedate() {
        return createdate;
    }
    public void setCreatedate(java.util.Date createdate) {
        this.createdate = createdate;
    }



    @Column(name  = "duedate")
    @Temporal(TemporalType.DATE)
    private java.util.Date duedate;
    public java.util.Date getDuedate() {
        return duedate;
    }
    public void setDuedate(java.util.Date duedate) {
        this.duedate = duedate;
    }
}
