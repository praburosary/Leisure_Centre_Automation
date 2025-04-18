package leisureCenter;

import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Booking {

    @Test
    public void browser_actions() {

        // Initialize Playwright
        Playwright pw = Playwright.create();
        Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
        Page page = browser.newPage();
        page.navigate("https://portal.everybody.org.uk/LhWeb/en/members/home/");

        // Wait for the popup to appear
        page.waitForSelector(".xn-options input[value='1']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(5000));
        page.click(".xn-options input[value='1']");

        page.waitForSelector("span[data-bind='text: confirmationYes']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
        page.click("span[data-bind='text: confirmationYes']");

        page.type("#xn-Username", "prabhureuben@gmail.com");
        page.type("#xn-Password", "Rosary08**");
        page.click("#login");

        page.waitForTimeout(4000);

        try {
            Locator popupClose = page.locator("xpath=(//div[@class='xn-close'])[2]");
            if (popupClose.isVisible()) {
                popupClose.click();
                System.out.println("Clicked on the 2nd .xn-close popup.");
            } else {
                System.out.println("2nd .xn-close popup is not visible.");
            }
        } catch (Exception e) {
            System.out.println("Error during 2nd popup close handling: " + e.getMessage());
        }

        page.waitForSelector("text=Online Bookings", new Page.WaitForSelectorOptions().setTimeout(5000));
        page.locator("text=Online Bookings").click();

        page.waitForSelector("text=Sport Courts and Pitches", new Page.WaitForSelectorOptions().setTimeout(5000));
        page.locator("text=Sport Courts and Pitches").click();

        page.fill("input[placeholder='Search activities']", "badminton");
        page.click("#calendar");

        // Calculate current date + 8 days
        LocalDate currentDate = LocalDate.now();
        LocalDate targetDate = currentDate.plusDays(8);
        int targetDay = targetDate.getDayOfMonth();
        String targetDateString = String.valueOf(targetDay);

        page.click("span.day-number:text('" + targetDateString + "')");

     // Get current day and time in 24-hour format
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        String currentTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));  // e.g., 14:30
        System.out.println("Today is: " + dayOfWeek + ", Current time: " + currentTime);

        // Parse current time to LocalTime for comparison
        LocalTime timeNow = LocalTime.parse(currentTime);

     // Logic for slot selection based on day and time
        if (dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            // For Fri-Sun, select the 13:30 slot only if time is between 13:30 and 14:30
            if ((timeNow.equals(LocalTime.of(13, 30)) || timeNow.isAfter(LocalTime.of(13, 30))) && timeNow.isBefore(LocalTime.of(14, 30))) {
                page.click("(//button[@class='xn-button xn-primary']/span[@data-bind='text: locationTypeSingular'])[5]");
            } else if ((timeNow.equals(LocalTime.of(14, 30)) || timeNow.isAfter(LocalTime.of(14, 30))) && timeNow.isBefore(LocalTime.of(15, 30))) {
                page.click("(//button[@class='xn-button xn-primary']/span[@data-bind='text: locationTypeSingular'])[6]");
            } else if ((timeNow.equals(LocalTime.of(15, 30)) || timeNow.isAfter(LocalTime.of(15, 30))) && timeNow.isBefore(LocalTime.of(16, 30))) {
                page.click("(//button[@class='xn-button xn-primary']/span[@data-bind='text: locationTypeSingular'])[7]");
            } else if ((timeNow.equals(LocalTime.of(16, 30)) || timeNow.isAfter(LocalTime.of(16, 30))) && timeNow.isBefore(LocalTime.of(17, 30))) {
                page.click("(//button[@class='xn-button xn-primary']/span[@data-bind='text: locationTypeSingular'])[8]");
            } else {
                System.out.println("No slot selection matched for Fri-Sun.");
            }
        } else {
            // Monday to Thursday logic starts from 15:30
            if ((timeNow.equals(LocalTime.of(15, 30)) || timeNow.isAfter(LocalTime.of(15, 30))) && timeNow.isBefore(LocalTime.of(16, 30))) {
                page.click("(//button[@class='xn-button xn-primary']/span[@data-bind='text: locationTypeSingular'])[7]");
            } else if ((timeNow.equals(LocalTime.of(16, 30)) || timeNow.isAfter(LocalTime.of(16, 30))) && timeNow.isBefore(LocalTime.of(17, 30))) {
                page.click("(//button[@class='xn-button xn-primary']/span[@data-bind='text: locationTypeSingular'])[8]");
            } else {
                System.out.println("No slot selection matched for Mon-Thu.");
            }
        }



        page.waitForTimeout(2000);

        int[] selectionOrder = {7, 8, 6, 5, 3, 4, 2, 1};
        for (int courtNumber : selectionOrder) {
            Locator radioButton = page.locator("input[type='radio'][value='" + courtNumber + "']");
            if (radioButton.isVisible()) {
                radioButton.click();
                System.out.println("Clicked on Court " + courtNumber);
                break;
            }
        }

        page.click("button.xn-button.xn-primary:has-text('Add to Basket')");
        page.waitForTimeout(2000);
        page.click("div.xn-icon");
        page.click("//a[@class='xn-button xn-cta']");
        page.waitForTimeout(2000);
        page.click("text=Pay Now");
        page.waitForTimeout(4000);
        
        Locator confirmationText = page.locator("h1.xn-title");
        if (confirmationText.textContent().equals("Transaction Confirmation")) {
            System.out.println("Booking is successful");
        } else {
            System.out.println("Booking confirmation text not found.");
        }


        browser.close();
    }
}
