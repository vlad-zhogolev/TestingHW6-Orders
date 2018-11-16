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
                        "MD", "18769", "1978201255", "01/10/22"),
                new CustomerData("Mia Pratt", "27891 Saint Lucia Blvd.", "Kona",
                        "FL", "42423", "2062480260", "01/11/22" ),
                new CustomerData("Gavin Roberson", "19804 Uzbekistan Ct.", "Atlantic City",
                        "NY", "06595", "1528583210", "01/03/25"),
                new CustomerData("Uma Donovan", "64440 England Ln.", "Bakersfield",
                        "NJ", "18396", "1621081159", "01/01/20"),
                new CustomerData("Igor Newman", "65172 Bolivia Way", "Homer",
                        "NE", "90889" , "1516240863", "01/05/22"),
                new CustomerData("Amery Freeman", "50923 Garner Ct.", "Santa Barbara",
                        "CT", "37177", "2052255336", "01/08/24"),
                new CustomerData("Rana Rodriquez", "74465 North Barbados Ct.", "Biddeford",
                        "MA", "94671","1669447557", "01/01/23"),
                new CustomerData("Chelsea Sullivan", "82225 East Boyer St.", "Pittsburgh",
                        "MA", "20188", "2146313492", "01/04/24"),
                new CustomerData("Norman Ward", "26083 Hartman Ln.", "Weirton",
                        "WA", "38447", "1478127250" , "01/02/25")
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

    @AfterMethod
    public void tearDown() {
        window.cleanUp();
    }
}