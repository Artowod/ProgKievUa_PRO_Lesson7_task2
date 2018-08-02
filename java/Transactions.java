import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "Transactions")
public class Transactions {

    @Id
    @Column
    private long id;

    public Transactions() {
    }
}
