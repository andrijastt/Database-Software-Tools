package rs.etf.sab.student;

import rs.etf.sab.operations.*;
import org.junit.Test;
import rs.etf.sab.tests.*;

public class StudentMain {

    public static void main(String[] args) {

        ArticleOperations articleOperations = new sa190222_ArticleOperations(); // Change this for your implementation (points will be negative if interfaces are not implemented).
        BuyerOperations buyerOperations = new sa190222_BuyerOperations();
        CityOperations cityOperations = new sa190222_CityOperations();
        GeneralOperations generalOperations = new sa190222_GeneralOperations();
        OrderOperations orderOperations = new sa190222_OrderOperations();
        ShopOperations shopOperations = new sa190222_ShopOperations();
        TransactionOperations transactionOperations = new sa190222_TransactionOperations();

        TestHandler.createInstance(
                articleOperations,
                buyerOperations,
                cityOperations,
                generalOperations,
                orderOperations,
                shopOperations,
                transactionOperations
        );

        TestRunner.runTests();
    }
}
