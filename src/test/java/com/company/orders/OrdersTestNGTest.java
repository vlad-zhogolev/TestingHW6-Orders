package com.company.orders;

import org.assertj.swing.core.matcher.DialogMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.data.TableCell;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.testng.annotations.*;

public class OrdersTestNGTest {
    class CustomerData {
        CustomerData(String name, String street, String city, String state, String zip, String cardNo, String expDate) {
            this.name = name;
            this.street = street;
            this.city = city;
            this.state = state;
            this.zip = zip;
            this.cardNo = cardNo;
            this.expDate = expDate;
        }

        String name;
        String street;
        String city;
        String state;
        String zip;
        String cardNo;
        String expDate;
    }

    private FrameFixture window;

    @BeforeClass
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeMethod
    public void setUp() {
        final JfrmMain frame = GuiActionRunner.execute(new GuiQuery<JfrmMain>() {
            @Override
            protected JfrmMain executeInEDT() throws Throwable {
                return new JfrmMain();
            }
        });

        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
    }

    @Test
    public void testOrders() throws InterruptedException {
        CustomerData[] customerDataArray = new CustomerData[]{
                new CustomerData("Kenyon Stuart", "73668 East Daniels Way", "Thousand Oaks",
                        "NE", "06842", "1330019087", "01/01/21"),
                new CustomerData("Ezra Sparks", "64574 Mcclain Way", "Florence",
                        "MD", "18769", "1978201255", "01/10/22")
        };

        // Menu Item Selection
        window.menuItemWithPath("File", "New").click();

        for (int i = 0; i < customerDataArray.length; ++i) {
            window.menuItemWithPath("Orders", "New order...").click();

            // Filling an order
            final DialogFixture dialog = window.dialog();

            dialog.comboBox("jcbProduct").selectItem("FamilyAlbum");
            dialog.spinner("jspQuantity").enterText("10");
            dialog.textBox("jtfName").enterText(customerDataArray[i].name);
            dialog.textBox("jtfStreet").enterText(customerDataArray[i].street);
            dialog.textBox("jtfState").enterText(customerDataArray[i].state);
            dialog.textBox("jtfCity").enterText(customerDataArray[i].city);
            dialog.textBox("jtfZip").enterText(customerDataArray[i].zip);
            dialog.textBox("jtfCardNumber").enterText(customerDataArray[i].cardNo);
            dialog.textBox("jftfExpiration").setText("").enterText(customerDataArray[i].expDate);

            dialog.button(JButtonMatcher.withText("Ok")).click();

            // Checking table contents
            window.table().requireCellValue(
                    TableCell.row(i).column(0),
                    customerDataArray[i].name
            );
        }
        // Closing the window
        /*window.close();
        final DialogFixture confirmation =
                window.dialog(DialogMatcher.withTitle("Confirmation"));
        confirmation.button(JButtonMatcher.withText("No")).click();*/
    }

    @DataProvider(name = "test data")

    @AfterMethod
    public void tearDown() {
        window.cleanUp();
    }
}