import application.caluclator.InvoiceCalculator;
import application.database.DummyDatabase;
import application.model.Customer;
import application.model.Invoice;
import application.model.ParkInfo;
import application.model.PremiumCustomer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.fail;


/**
 * Created by henry on 2.05.17.
 */
public class CalculatorTest {
    private static final LocalDateTime CALCULATION_DATE = LocalDateTime.of(2017, 04, 04, 00, 00);
    Customer testRegular;
    Customer testPremium;
    BigDecimal total;

    private void setupPremium() {
        if (testPremium == null) {
            testPremium = new PremiumCustomer();
            testPremium.setName("Alice");
            testPremium.setCustomerID(1);
        }
    }

    private void setup() {
        if (testRegular == null) {
            testRegular = new Customer();
            testRegular.setName("Bob");
            testRegular.setCustomerID(0);
        }
    }

    //REGULAR CUSTOMER ------------ //

    //08:12 – 10:45 (6 * 1.50 = 9.00 EUR)
    @Test
    public void firstTestRegular() {
        setup();
        total = testRegular.addParkingInfo(LocalDateTime.of(2017, 04, 30, 8, 12),
                LocalDateTime.of(2017, 04, 30, 10, 45), 0);
        assertEquals(9.0, total.doubleValue());
    }
    //19:40 – 20:35 (2 * 1.00 = 2.00 EUR)
    @Test
    public void secondTestRegular() {
        setup();
        total = testRegular.addParkingInfo(LocalDateTime.of(2017, 04, 15, 19, 40),
                LocalDateTime.of(2017, 04, 15, 20, 35),
                0);
        assertEquals(2.0, total.doubleValue());

    }
    // Complex case: Start at 05.40 - 20.35. The system will divide the parking information into 3 different periods.
    // 1st - 05.40 - 07.00 (80 minutes) 3 parking periods with rate 1.0 (3.0 eur)
    // 2nd 10 parking intervals with rate 0.75 (7.50 eur)- 07.00 - 19.00 (720 minutes) 24 parking periods with rate 1.5 (36 eur)
    // 3rd - 19.00 - 20.35 (95 minutes) 4 parking periods with rate 1.0 (4 eur)
    // sum them together - 3 + 36 + 4 = 43 eur.
    @Test
    public void complexTestRegular() {
        setup();
        total = testRegular.addParkingInfo(LocalDateTime.of(2017, 04, 15, 05, 40),
                LocalDateTime.of(2017, 04, 15, 20, 35),
                0);
        assertEquals(43.0, total.doubleValue());
        assertEquals(1, testRegular.getGroupedParkings().size());

    }

    //PREMIUM CUSTOMER ------------ //

    //08:12 – 10:45 (6 * 1.00 = 6.00 EUR)
    @Test
    public void firstTestPremium() {
        setupPremium();
        total = testPremium.addParkingInfo(LocalDateTime.of(2017, 04, 10, 8, 12),
                LocalDateTime.of(2017, 04, 10, 10, 45), 0);
        assertEquals(6.0, total.doubleValue());
    }
    //07:02 – 11:56 (10 * 1.00 = 10.00 EUR)
    @Test
    public void secondTestPremium() {
        setupPremium();
        total = testPremium.addParkingInfo(LocalDateTime.of(2017, 04, 13, 07, 02),
                LocalDateTime.of(2017, 04, 13, 11, 56), 0);

        assertEquals(10.0, total.doubleValue());
    }



    //22:10 – 22:35 (1 * 0.75 = 0.75 EUR)
    @Test
    public void testThirdPremium() {
        setupPremium();
        total = testPremium.addParkingInfo(LocalDateTime.of(2017, 04, 18, 22, 10),
                LocalDateTime.of(2017, 04, 18, 22, 35), 0);
        assertEquals(0.75, total.doubleValue());
    }

    //19:40 – 20:35 (2 * 0.75 = 1.50 EUR)
    @Test
    public void testFourthPremium() {
        setupPremium();
        total = testPremium.addParkingInfo(LocalDateTime.of(2017, 04, 23, 19, 40),
                LocalDateTime.of(2017, 04, 23, 20, 35), 0);
        assertEquals(1.50, total.doubleValue());
    }

    // Complex case, multiple days parking. Start 01.04.2017 15:00 - 03.04.2017 21:35
    // The system will divide the parking info into
    // 1st - 01.04 15:00 - 19:00 (240 minutes) 8 parking intervals with rate 1.0 (8 eur)
    // 2nd - 01.04 19:00 - 23:59 (299 minutes) 10 parking intervals with rate 0.75 (7.50 eur)
    // 3rd - 02.04 00:00 - 07:00 (420 minutes) 14 parking intervals with rate 0.75 (10.50 eur)
    // 4th - 02.04 07:00 - 19:00 (720 minutes) 24 parking intervals with rate 1.0 (24 eur)
    // 5th - 02.04 19:00 - 23:59 (299 minutes) 10 parking intervals with rate 0.75 (7.50 eur)
    // 5th - 03.04 00:00 - 07:00 (299 minutes) 14 parking intervals with rate 0.75 (10.50 eur)
    // 6th - 03.04 07:00 - 19:00 (720 minutes) 24 parking intervals with rate 1.0 (24 eur)
    // 7th - 03.04 19:00 - 21:35 (155 minutes) 6 parking intervals with rate 0.75 (4.50 eur)
    // 8 + 7.5 + 10.5 + 24 +7.5 + 10.5 + 24 + 4.5 =
    @Test
    public void testComplexCasePremium() {
        setupPremium();
        total = testPremium.addParkingInfo(LocalDateTime.of(2017, 04, 01, 15, 00),
                LocalDateTime.of(2017, 04, 03, 21, 35), 0);
        assertEquals(96.50, total.doubleValue());
        assertEquals(1, testPremium.getGroupedParkings().size());
    }


    // Test to add parking information via rest information
    @Test
    public void testAddParkingInfo() {
        setup();
        ParkInfo info = new ParkInfo();
        info.setCustomerID(testRegular.getCustomerID());
        info.setStartTime(1492430400000L);
        info.setEndTime(1492437600000L);
        info.setGroupID(0);
        info.setParkHouseID(0);
        DummyDatabase.addCustomer(testRegular);
        DummyDatabase.addParkInfo(info);
        assertEquals(1, testRegular.getAllParkings().size());
    }

    // Invoice for regular, 11.00
    @Test
    public void testGenerateRegularInvoice() {
        setup();
        firstTestRegular();
        secondTestRegular();
        InvoiceCalculator invoiceCalculator = new InvoiceCalculator();
        ArrayList<Invoice> invoices = invoiceCalculator.calculateInvoicesForMonth(testRegular, CALCULATION_DATE);
        assertEquals(1, invoices.size());
        assertEquals(11.0, invoices.get(0).getAmount().doubleValue());
        assertEquals(0.0, invoices.get(0).getMonthlyFee());
    }
    // Invoice premium. Has not asked for one ever and will get 3 invoices, for march, april and may.
    @Test
    public void testGeneratePremiumInvoice() {
        setupPremium();
        addMarchParking();
        addMayParking();
        firstTestPremium();
        secondTestPremium();
        testThirdPremium();
        testFourthPremium();
        InvoiceCalculator invoiceCalculator = new InvoiceCalculator();
        ArrayList<Invoice> invoices = invoiceCalculator.calculateInvoicesForMonth(testPremium, CALCULATION_DATE.plusMonths(1));
        assertEquals(3, invoices.size());

        Invoice march = invoices.get(0);
        Invoice april = invoices.get(1);
        Invoice may = invoices.get(2);

        checkMarchInvoice(march);
        checkAprilInvoice(april);
        checkMayInvoice(may);

        invoices = invoiceCalculator.calculateInvoicesForMonth(testPremium, CALCULATION_DATE.plusMonths(1));
        assertEquals(1, invoices.size());
    }
    // May invoice. Most importantly, monthlyfee is not 0 and is not 20. Calculated according to current date.
    private void checkMayInvoice(Invoice may) {
        assertEquals(1, may.getParkings().size());
        assertEquals(1.5, may.getAmount().doubleValue());
        assertNotSame(20.0, may.getMonthlyFee());
        assertNotSame(0.0, may.getMonthlyFee());
    }
    // April invoice. Parking amounts and monthly fees.
    private void checkAprilInvoice(Invoice april) {
        assertEquals(4, april.getParkings().size());
        assertEquals(18.25, april.getAmount().doubleValue());
        assertEquals(20.0, april.getMonthlyFee());
    }
    // March invoice. Parking amounts.
    private void checkMarchInvoice(Invoice march) {
        assertEquals(1, march.getParkings().size());
        assertEquals(13.00, march.getAmount().doubleValue());
        assertEquals(20.0, march.getMonthlyFee());
    }

    // Add parking in month March
    private void addMarchParking() {
        testPremium.addParkingInfo(LocalDateTime.of(2017, 03, 13, 05, 02),
                LocalDateTime.of(2017, 03, 13, 11, 56), 0);
    }
    // Add parking in month May
    private void addMayParking() {
        testPremium.addParkingInfo(LocalDateTime.of(2017, 05, 02, 19, 40),
                LocalDateTime.of(2017, 05, 02, 20, 35), 0);
    }
}
