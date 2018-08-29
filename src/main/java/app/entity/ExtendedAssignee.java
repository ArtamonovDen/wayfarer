package app.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "extendedAssignee")
public class ExtendedAssignee extends EntityObject {
    @Id
    @Column(name = "sup_id",nullable = false)
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    private Name name;
    public Name getName() {
        return name;
    }
    public void setName(Name name) {
        this.name = name;
    }

    @Column(name = "depart_id",nullable = false)
    private int depart_id;
    public int getDepart_id(){return depart_id;}
    public void setDepart_id(int depart_id){this.depart_id = depart_id;}

    @Column(name = "depart_name",nullable = false)
    private String depart_name;
    public String getDepart_name(){return depart_name;}
    public void setDepart_name(String depart_name){this.depart_name = depart_name;}

    public StringBuffer getFIO(){
        StringBuffer s = new StringBuffer();
        s.append(this.name.getLastName()+" ");
        s.append(this.name.getLastName().substring(0,1)+".");
        if (!this.name.getMiddleName().isEmpty()){
            s.append(this.name.getMiddleName().substring(0,1)+".");
        }
        return s;
    }
}