import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    static EntityManagerFactory entityMF;
    static EntityManager entityManager;

    public static void main(String[] args) {
        entityMF = Persistence.createEntityManagerFactory("JPAHibernateBank");
        entityManager = entityMF.createEntityManager();
        BankingAccountsManager accountsManager = new BankingAccountsManager(entityMF, entityManager);
            System.out.println("Create all tables and fulfill it by pre-default data ... ");
            accountsManager.initializeAllTables();
            System.out.println("Tables are created.");
        System.out.println("\n\n\n");

        accountsManager.refillAccount("Mama", "EUR", 1000);
        accountsManager.refillAccount("Mama", "UAH", 1000);
        accountsManager.refillAccount("Mama", "USD", 1000);

        accountsManager.refillAccount("Papa", "EUR", 1000);
        accountsManager.refillAccount("Papa", "UAH", 1000);
        accountsManager.refillAccount("Papa", "USD", 1000);

        accountsManager.directTransferMoneyBetweenAccounts("Mama","UAH", "Papa", "EUR", 100);
        accountsManager.directConvertMoneyWithinAccount("Mama","UAH", "EUR", 100);

        accountsManager.userInterface();

        entityManager.close();
        entityMF.close();
    }
}