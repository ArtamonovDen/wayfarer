package app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pizza_guys")
public class PizzaGuy extends EntityObject {

    @Id
    @Column (name = "depart_id",nullable = false)
    private int depart_id;
    public int getDepart_id() {
        return depart_id;
    }
    public void setDepart_id(int depart_id) {
        this.depart_id = depart_id;
    }


    @Column(name = "pizzaguy_id",nullable = false)
    private int Pizzaguy_id;
    public int getPizzaguy_id(){return Pizzaguy_id;}
    public void setPizzaguy_id(int Pizzaguy_id){this.Pizzaguy_id = Pizzaguy_id;}

}
