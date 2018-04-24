package ro.ubb.catalog.core.model;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Blood extends BaseEntity<Long>{
    String collectionDate;
    float quantity;
    int state;
    String type;
    int shelfLife;

    public Blood() {
    }

    public Blood(String collectionDate, float quantity, int state, String type) {
        this.collectionDate = collectionDate;
        this.quantity = quantity;
        this.state = state;
        this.type = type;
        if(type.equals("t"))
            shelfLife=1;
        else if (type.equals("p"))
            shelfLife=2;
        else
            shelfLife=3;
    }

    public String getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Blood{" +
                "collectionDate='" + collectionDate + '\'' +
                ", quantity=" + quantity +
                ", state=" + state +
                ", type='" + type + '\'' +
                '}';
    }
}
