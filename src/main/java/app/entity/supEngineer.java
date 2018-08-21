package app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "support_staff")
public class supEngineer extends EntityObject {
    @Id
    @Column (name = "sup_id",nullable = false)
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
    //@ManyToOne
    ///@JoinColumn(name = "depart_id",
       //     foreignKey = @ForeignKey(name = "PERSON_ID_FK")
    private int depart_id;
    public int getDepart_id(){return depart_id;}
    public void setDepart_id(int depart_id){this.depart_id = depart_id;}
}