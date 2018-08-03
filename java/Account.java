import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Accounts")
public class Account {

    @Id
    @GeneratedValue()
    @Column
    private long id;

    @Column
    private String moneyType;

    @Column
    private double money;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;


    @OneToMany(mappedBy = "accountFrom", cascade = CascadeType.ALL)
    private List<Transactions> transactionsFrom = new ArrayList<Transactions>();

    @OneToMany(mappedBy = "accountTo", cascade = CascadeType.ALL)
    private List<Transactions> transactionsTo = new ArrayList<Transactions>();

    public Account() {
    }

    public Account(String moneyType, long amountMoney) {
        this.moneyType = moneyType;
        this.money = amountMoney;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", moneyType='" + moneyType + '\'' +
                ", money=" + money +
                ", client=" + client +
                '}';
    }
}
