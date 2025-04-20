package leisureCenter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.testng.annotations.Test;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

public class Booking {

    private void handlePreferredSitePopup(Page page) {
        Locator applyButton = page.locator("button.xn-button.xn-cta", new Page.LocatorOptions().setHasText("Apply"));
        if (applyButton.isVisible()) {
            applyButton.click();
        }
    }

    private void handleCookiesPopup(Page page) {
        Locator yesRadio = page.locator("input[type='radio'][name='rbGoogle'][value='1']");
        Locator acceptButton = page.locator("button.xn-button.xn-cta", new Page.LocatorOptions().setHasText("Accept"));
        if (yesRadio.count() > 0 && yesRadio.isVisible()) {
            yesRadio.check();
        }

        if (yesRadio.count() > 0 && yesRadio.isChecked()) {
            if (acceptButton.count() > 0 && acceptButton.isVisible()) {
                acceptButton.click();
            }
        }
    }
    
    
    private void WaitforExactTime() throws InterruptedException {

   	 // Loop to check if the time is 14:30, 15:30 or 16:30, and click at the correct time
       boolean timeMatched = false;
       DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
       while (!timeMatched) {
           // Get the current time in UK timezone
           LocalTime currentLocalTime = LocalTime.now(ZoneId.of("Europe/London"));
           String currentTimeStr = currentLocalTime.format(timeFormatter);
           System.out.println("Current UK time: " + currentTimeStr);

           // Check if the current time matches 14:30, 15:30, or 16:30
           if (currentTimeStr.equals("14:30") ||
               currentTimeStr.equals("15:30") ||
               currentTimeStr.equals("16:30")) {
               timeMatched = true; // Stop the loop after clicking
               System.out.println("Time matched! Proceeding..."); // Added a confirmation message
           } else {
               // Wait for 2 seconds before checking again
               System.out.println("Waiting for the correct time...");
               Thread.sleep(1000);
           }
       }
	}
    

    @Test
    public void browser_actions() throws InterruptedException {

        // Use UK time zone explicitly
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/London"));
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        String currentTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        System.out.println("Today is: " + dayOfWeek + ", Current time: " + currentTime);

        Playwright pw = Playwright.create();
        Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(true));
        Page page = browser.newPage();
        page.navigate("https://portal.everybody.org.uk/LhWeb/en/members/home/");
        page.waitForLoadState(LoadState.LOAD);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);

        // Handle cookies and preferred site popups
        handleCookiesPopup(page);
        handlePreferredSitePopup(page);

        // Login
        page.type("#xn-Username", "prabhureuben@gmail.com");
        page.type("#xn-Password", "Rosary08**");
        page.click("#login");
        page.waitForTimeout(4000);

        page.waitForSelector("text=Online Bookings", new Page.WaitForSelectorOptions().setTimeout(5000));
        page.locator("text=Online Bookings").click();
        page.waitForTimeout(2000);
        handlePreferredSitePopup(page);

        page.waitForSelector("text=Sport Courts and Pitches", new Page.WaitForSelectorOptions().setTimeout(5000));
        page.locator("text=Sport Courts and Pitches").click();

        page.fill("input[placeholder='Search activities']", "Badminton");
        page.click("#calendar");

        LocalDate targetDate = LocalDate.now(ZoneId.of("Europe/London")).plusDays(8);
        int targetDay = targetDate.getDayOfMonth();
        String targetDateString = String.valueOf(targetDay);
        page.click("span.day-number:text('" + targetDateString + "')");       
        
        
        // Convert the string time into LocalTime in UK timezone
        LocalTime timeNow = LocalTime.parse(currentTime);

        if (dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
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
        	if ((timeNow.equals(LocalTime.of(15, 30)) || timeNow.isAfter(LocalTime.of(15, 30))) && timeNow.isBefore(LocalTime.of(16, 30))) {
                page.click("(//button[@class='xn-button xn-primary']/span[@data-bind='text: locationTypeSingular'])[7]");
            } else if ((timeNow.equals(LocalTime.of(16, 30)) || timeNow.isAfter(LocalTime.of(16, 30))) && timeNow.isBefore(LocalTime.of(17, 30))) {
                page.click("(//button[@class='xn-button xn-primary']/span[@data-bind='text: locationTypeSingular'])[8]");
            } else {
                System.out.println("No slot selection matched for Mon-Thu.");
            }
        }

        
        page.waitForTimeout(4000);

        int[] selectionOrder = {7, 8, 6, 5, 3, 4, 2, 1};
        for (int courtNumber : selectionOrder) {
            Locator radioButton = page.locator("input[type='radio'][value='" + courtNumber + "']");
            if (radioButton.isVisible()) {
                radioButton.click();
                System.out.println("Clicked on Court " + courtNumber);
                break;
            }
        }

        
        WaitforExactTime();
        
        page.click("button.xn-button.xn-primary:has-text('Add to Basket')");
        page.waitForTimeout(2000);
        page.click("div.xn-icon");
        page.click("//a[@class='xn-button xn-cta']");
        page.waitForTimeout(2000);
        page.click("text=Pay Now");
        page.waitForTimeout(6000);

        Locator confirmationText = page.locator("h1.xn-title");
        if (confirmationText.textContent().equals("Transaction Confirmation")) {
            System.out.println("Booking is successful");
        } else {
            System.out.println("Booking is NOT successful.");
        }

        browser.close();
    }
}
