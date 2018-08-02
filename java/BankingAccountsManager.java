import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankingAccountsManager {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public BankingAccountsManager() {
    }

    public BankingAccountsManager(EntityManagerFactory entityManagerFactory, EntityManager entityManager) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityManager = entityManager;
    }

    public void userInterface() {
        int selectedFunction = 1;
        Scanner function = new Scanner(System.in);
        for (; selectedFunction != 0; ) {
            System.out.println("+----------= Welcome to the User Interface =---------+");
            System.out.println("| Please make your choice:                           |");
            System.out.println("| 1 - add New Client                                 |");
            System.out.println("| 2 - change Exchange Rate                           |");
            System.out.println("| 3 - convert money within one account               |");
            System.out.println("| 4 - transfer money between accounts                |");
            System.out.println("| 5 - Count SUM of all accounts of one Client in UAH |");
            System.out.println("| 0 - Exit                                           |");
            System.out.println("+----------------------------------------------------+");
            try {
                selectedFunction = Integer.valueOf(function.nextLine());
                if (selectedFunction >= 0 && selectedFunction < 6) {
                    performSelectedFunction(selectedFunction);
                } else {
                    System.out.print("Wrong choice.");
                }
            } catch (NumberFormatException nfe) {
                System.out.print("Wrong choice.");
            }
        }

    }

    private boolean isEnteredCurrencyCorrect(String currency) {
        return currency.equals("EUR") || currency.equals("UAH") || currency.equals("USD");
    }

    private double findRateBetweenCurrencies(String currencyFrom, String currencyTo) {
        List<ExchangeRates> result = new ArrayList<ExchangeRates>();
        try {
            Query query = entityManager.createQuery("SELECT exch FROM ExchangeRates exch", ExchangeRates.class);
            result = query.getResultList();
        } catch (NoResultException ex) {
            System.out.println("ExchangeRate table is blank!");
        }
        double rateEurUah = 0;
        double rateUsdUah = 0;
        for (ExchangeRates eachRow : result) {
            if (eachRow.getFromMoneyType().equals("EUR")) {
                rateEurUah = eachRow.getRate();
            }
            if (eachRow.getFromMoneyType().equals("USD")) {
                rateUsdUah = eachRow.getRate();
            }
        }
        if (currencyFrom.equals(currencyTo)) {
            return 1;
        }

        if (currencyFrom.equals("EUR") && currencyTo.equals("UAH")) {
            return rateEurUah;
        }

        if (currencyFrom.equals("USD") && currencyTo.equals("UAH")) {
            return rateUsdUah;
        }

        if (currencyFrom.equals("EUR") && currencyTo.equals("USD")) {
            return rateEurUah / rateUsdUah;
        }

        if (currencyFrom.equals("UAH") && currencyTo.equals("USD")) {
            return 1 / rateUsdUah;
        }

        if (currencyFrom.equals("UAH") && currencyTo.equals("EUR")) {
            return 1 / rateEurUah;
        }

        if (currencyFrom.equals("USD") && currencyTo.equals("EUR")) {
            return rateUsdUah / rateEurUah;
        }

        System.out.println("Not correct Currency! - please check!");
        return 0;
    }

    public void directConvertMoneyWithinAccount(String clientName, String transferredCurrency, String destinationCurrency, double money){
        double rate = findRateBetweenCurrencies(transferredCurrency, destinationCurrency);
        if (rate == 0) {
            System.out.println("Money Transaction within account is cancelled");
        } else {
            refillAccount(clientName, transferredCurrency, -money);
            refillAccount(clientName, destinationCurrency, money * rate);
        }
    }

    private void manualConvertMoneyWithinAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Convert Money within one Account:");
        System.out.print("Enter Client Name: ");
        String clientName = scanner.nextLine();
        System.out.print("Enter Currency you want to Transfer (f.e. \"EUR\"): ");
        String transferredCurrency = scanner.nextLine();
        if (!isEnteredCurrencyCorrect(transferredCurrency)) {
            System.out.println("Wrong entered Currency. Exit.");
            return;
        }
        System.out.println("Enter Destination Currency (f.e. \"UAH\"): ");
        String destinationCurrency = scanner.nextLine();
        if (!isEnteredCurrencyCorrect(destinationCurrency)) {
            System.out.println("Wrong entered Currency. Exit.");
            return;
        }
        if (transferredCurrency.equals(destinationCurrency)) {
            System.out.println("Transferred and Destination currencies should not be equal!");
            return;
        }
        System.out.println("Enter Money: ");
        double money;
        try {
            money = Double.valueOf(scanner.nextLine());
        } catch (NumberFormatException exc) {
            System.out.println("Wrong entered money. Exit.");
            return;
        }

        double rate = findRateBetweenCurrencies(transferredCurrency, destinationCurrency);
        if (rate == 0) {
            System.out.println("Money Transaction within account is cancelled");
        } else {
            refillAccount(clientName, transferredCurrency, -money);
            refillAccount(clientName, destinationCurrency, money * rate);
        }
    }

    public void directTransferMoneyBetweenAccounts(String fromClientName, String transferredCurrency, String toClientName, String destinationCurrency, double money) {
        double rate = findRateBetweenCurrencies(transferredCurrency, destinationCurrency);
        if (rate == 0) {
            System.out.println("Money Transaction within account is cancelled");
        } else {
            refillAccount(fromClientName, transferredCurrency, -money);
            refillAccount(toClientName, destinationCurrency, money * rate);
        }
    }

    private void manualTransferMoneyBetweenAccounts() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Transfer Money Between Accounts:");
        System.out.print("Enter Client Name whom Money you want to transfer: ");
        String fromClientName = scanner.nextLine();
        System.out.print("Enter Currency you want to Transfer (f.e. \"EUR\"): ");
        String transferredCurrency = scanner.nextLine();
        if (!isEnteredCurrencyCorrect(transferredCurrency)) {
            System.out.println("Wrong entered Currency. Exit.");
            return;
        }
        System.out.println("Enter Destination Client: ");
        String toClientName = scanner.nextLine();
        System.out.println("Enter Destination Currency (f.e. \"UAH\"): ");
        String destinationCurrency = scanner.nextLine();
        if (!isEnteredCurrencyCorrect(destinationCurrency)) {
            System.out.println("Wrong entered Currency. Exit.");
            return;
        }
        System.out.println("Enter Money: ");
        double money;
        try {
            money = Double.valueOf(scanner.nextLine());
        } catch (NumberFormatException exc) {
            System.out.println("Wrong entered money. Exit.");
            return;
        }

        double rate = findRateBetweenCurrencies(transferredCurrency, destinationCurrency);
        if (rate == 0) {
            System.out.println("Money Transaction within account is cancelled");
        } else {
            refillAccount(fromClientName, transferredCurrency, -money);
            refillAccount(toClientName, destinationCurrency, money * rate);
        }
    }

    private boolean isTypedCurrencyPairCorrect(String oneOrTwo) {
        if (oneOrTwo.isEmpty()) {
            System.out.println("Wrong data. Exit");
            return false;
        }
        if (oneOrTwo.equals("1") || oneOrTwo.equals("2")) {
            return true;
        }
        System.out.println("Wrong data. Exit");
        return false;
    }

    private boolean isTypedRateCorrect(String rate) {
        if (rate.isEmpty()) {
            System.out.println("Wrong data. Exit");
            return false;
        }
        try {
            if (Double.valueOf(rate) > 0) {
                return true;
            }
            System.out.println("Wrong data. Exit");
            return false;
        } catch (NumberFormatException exc) {
            System.out.println("Wrong data. Exit");
            return false;
        }
    }

    private void functionChangeExchangeRate() {
        System.out.println("Please select \"1\" if ExchangeRate for EUR->UAH or \"2\" for USD->UAH");
        System.out.println("(For EUR->USD the exchange rate is counted automatically.)");
        System.out.print("Please make your choice: ");
        String currencyPair = new Scanner(System.in).nextLine();
        if (!isTypedCurrencyPairCorrect(currencyPair)) {
            return;
        }
        System.out.println("please enter Rate: ");
        String rate = new Scanner(System.in).nextLine();
        if (!isTypedRateCorrect(rate)) {
            return;
        }

        if (currencyPair.equals("1")) {
            changeExchangeRateData("EUR", "UAH", Double.valueOf(rate));
        } else {
            changeExchangeRateData("USD", "UAH", Double.valueOf(rate));
        }

    }

    private double countSumAllAccountOneClientInUAH() {
        String clientName;
        System.out.print("Enter Client Name: ");
        clientName = (new Scanner(System.in)).nextLine();
        if (!isClientExists(clientName)) {
            System.out.println("Such Client is missing in DataBase. Exit.");
            return 0;
        }
        Client client;
        List<Account> clientAccounts = new ArrayList<Account>();
        try {
            Query query = entityManager.createQuery("SELECT client FROM Client client WHERE client.name=:name", Client.class);
            query.setParameter("name", clientName);
            client = (Client) query.getSingleResult();
            query = entityManager.createQuery("SELECT account FROM Account account WHERE account.client=:client", Account.class);
            query.setParameter("client", client);
            clientAccounts = query.getResultList();

        } catch (NoResultException ex) {
            ex.printStackTrace();
        }

        double resultedSum = 0;
        for (Account accountByCurrency : clientAccounts) {
            if (accountByCurrency.getMoneyType().equals("UAH")) {
                resultedSum = resultedSum + accountByCurrency.getMoney();
            }
            if (accountByCurrency.getMoneyType().equals("USD")) {
                resultedSum = resultedSum + accountByCurrency.getMoney() * findRateBetweenCurrencies("USD", "UAH");
            }
            if (accountByCurrency.getMoneyType().equals("EUR")) {
                resultedSum = resultedSum + accountByCurrency.getMoney() * findRateBetweenCurrencies("EUR", "UAH");
            }
        }
        return resultedSum;
    }


    public void performSelectedFunction(int userInterfaceFunction) {
        if (userInterfaceFunction == 1) {
            addNewClient();
        }
        if (userInterfaceFunction == 2) {
            functionChangeExchangeRate();
        }
        if (userInterfaceFunction == 3) {
            manualConvertMoneyWithinAccount();
        }
        if (userInterfaceFunction == 4) {
            manualTransferMoneyBetweenAccounts();
        }
        if (userInterfaceFunction == 5) {
            System.out.println("SUM of all accounts in UAH of the current Client is: " + countSumAllAccountOneClientInUAH());
        } else {
        }
    }


    public void initializeAllTables() {

        addNewClient("Tonia", 31); //плюс создаём сразу 3 нулевых счета грн евро долл
        addNewClient("Mama", 70); //плюс создаём сразу 3 нулевых счета грн евро долл
        addNewClient("Papa", 71); //плюс создаём сразу 3 нулевых счета грн евро долл

        changeExchangeRateData("EUR", "UAH", 31.2);
        changeExchangeRateData("USD", "UAH", 26.8);

    }


    private Client getClientByName(String clientName) {
        if (isClientExists(clientName)) {
            Client changedClient;
            Query query = entityManager.createQuery("SELECT client FROM Client client WHERE client.name = :name", Client.class);
            query.setParameter("name", clientName);
            changedClient = (Client) query.getSingleResult();
            return changedClient;
        }
        return null;
    }

    public void refillAccount(String clientName, String moneyType, double sum) {
        Client changedClient = getClientByName(clientName);
        if (changedClient == null) {
            return;
        } else {
            for (Account acc : changedClient.getAccounts()) {
                if (acc.getMoneyType().equals(moneyType)) {
                    acc.setMoney(acc.getMoney() + sum);
                    updateClientTransaction(changedClient);
                    return;
                }
            }
        }
    }


    private boolean isClientExists(String clientName) {
        try {
            Client client;
            Query query = entityManager.createQuery("SELECT client FROM Client client WHERE client.name = :name", Client.class);
            query.setParameter("name", clientName);
            client = (Client) query.getSingleResult();
        } catch (NoResultException ex) {
            return false;
        }
        return true;
    }

    private void addingClientTransaction(Client newClient) {
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(newClient);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
    }

    private void updateClientTransaction(Client newClient) {
        entityManager.getTransaction().begin();
        try {
            entityManager.merge(newClient);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
    }

    public void addNewClient() {
        System.out.println("Adding new Client to Banking DataBase:");
        String clientName;
        int clientAge;
        try {
            System.out.print("Enter Client Name: ");
            clientName = (new Scanner(System.in)).nextLine();
            if (isClientExists(clientName)) {
                return;
            }
            System.out.print("Enter client Age: ");
            clientAge = Integer.valueOf((new Scanner(System.in)).nextLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Entered data is incorrect.");
            return;
        }
        Client newClient = new Client(clientName, clientAge);
        newClient.addAccount(new Account("USD", 0));
        newClient.addAccount(new Account("UAH", 0));
        newClient.addAccount(new Account("EUR", 0));
        addingClientTransaction(newClient);

    }

    public void addNewClient(String clientName, int clientAge) {
        System.out.println("Adding new Client to Banking DataBase:");

        if (isClientExists(clientName)) {
            return;
        }

        Client newClient = new Client(clientName, clientAge);
        newClient.addAccount(new Account("USD", 0));
        newClient.addAccount(new Account("UAH", 0));
        newClient.addAccount(new Account("EUR", 0));
        addingClientTransaction(newClient);
    }

    private void exchangeRateTransaction(ExchangeRates rates) {
        entityManager.getTransaction().begin();
        try {
            entityManager.merge(rates);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
    }

    public void changeExchangeRateData(String fromMoneyType, String toMoneyType, double rate) {
        ExchangeRates resultOneLine;
        try {
            Query query = entityManager.createQuery("SELECT exch FROM ExchangeRates exch WHERE exch.fromMoneyType=:from AND exch.toMoneyType=:to", ExchangeRates.class);
            query.setParameter("from", fromMoneyType);
            query.setParameter("to", toMoneyType);
            resultOneLine = (ExchangeRates) query.getSingleResult();
            resultOneLine.setRate(rate);
            exchangeRateTransaction(resultOneLine);
        } catch (NoResultException ex) {
            exchangeRateTransaction(new ExchangeRates(fromMoneyType, toMoneyType, rate));
        }
    }

}
