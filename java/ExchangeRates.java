import javax.persistence.*;

@Entity
@Table(name="exchangerates")
public class ExchangeRates {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String fromMoneyType;

    @Column
    private String toMoneyType;

    @Column
    private double rate;

    public ExchangeRates() {
    }

    public ExchangeRates(String fromMoneyType, String toMoneyType, double rate) {
        this.fromMoneyType = fromMoneyType;
        this.toMoneyType = toMoneyType;
        this.rate = rate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromMoneyType() {
        return fromMoneyType;
    }

    public void setFromMoneyType(String fromMoneyType) {
        this.fromMoneyType = fromMoneyType;
    }

    public String getToMoneyType() {
        return toMoneyType;
    }

    public void setToMoneyType(String toMoneyType) {
        this.toMoneyType = toMoneyType;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double ratio) {
        this.rate = ratio;
    }

    @Override
    public String toString() {
        return "ExchangeRates{" +
                "id=" + id +
                ", fromMoneyType='" + fromMoneyType + '\'' +
                ", toMoneyType='" + toMoneyType + '\'' +
                ", rate=" + rate +
                '}';
    }
}
