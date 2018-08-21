package app.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable

public class Name extends EntityObject {

    @Column(name = "first_name")
    private String first_name;
    public String getFirstName() { return first_name;  }
    public void setFirstName(String firstName) { this.first_name = firstName; }


    @Column(name = "middle_name")
    private String middle_name;
    public String getMiddleName() { return middle_name;  }
    public void setMiddleName(String middleName) { this.middle_name = middleName;    }


    @Column(name = "last_name")
    private String last_name;
    public String getLastName() {return last_name; }
    public void setLastName(String lastName) { this.last_name = lastName; }

}

