package leisureCenter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

public class Booking {

    @Test
    public void browser_actions() {

    	// Capture start time
        long startTime = System.currentTimeMillis();
        
     // Get current day and time in 24-hour format
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        String currentTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));  // e.g., 14:30
        System.out.println("Today is: " + dayOfWeek + ", Current time: " + currentTime);

    	
        // Initialize Playwright
        Playwright pw = Playwright.create();
        Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
        Page page = browser.newPage();
        page.navigate("https://portal.everybody.org.uk/LhWeb/en/members/home/");
        page.waitForLoadState(LoadState.LOAD);
        page.waitForLoadState(LoadState.NETWORKIDLE);  // Wait until no network activity for 500ms
        
        
                      
        page.waitForTimeout(2000);
        
        //Accpet the cookies popup if present
        Locator yesRadio = page.locator("input[type='radio'][name='rbGoogle'][value='1']");
        Locator acceptButton = page.locator("button.xn-button.xn-cta", new Page.LocatorOptions().setHasText("Accept"));

        if (yesRadio.count() > 0 && yesRadio.isVisible()) {
            yesRadio.check();  // Use .check() for radio buttons
            System.out.println("Selected the 'Yes' radio button.");
        } else {
            System.out.println("'Yes' radio button not found.");
        }
       
        
        if (yesRadio.count() > 0 && yesRadio.isChecked()) {
            if (acceptButton.count() > 0 && acceptButton.isVisible()) {
                acceptButton.click();
                System.out.println("Clicked on the 'Accept' button because 'Yes' was selected.");
            } else {
                System.out.println("'Accept' button not found.");
            }
        } else {
            System.out.println("'Yes' radio button is not selected.");
        }
        
        
        
        //handle the preferred site popup
        Locator applyButton = page.locator("button.xn-button.xn-cta", new Page.LocatorOptions().setHasText("Apply"));     
        
        if (applyButton.isVisible()) {
            applyButton.click();
            System.out.println("Clicked on the Apply button in Preferred site Popup.");
        } else {
            System.out.println("Apply button not found in Preferred site Popup");
        }
      
        //login details
        page.type("#xn-Username", "prabhureuben@gmail.com");
        page.type("#xn-Password", "Rosary08**");
        page.click("#login");

        page.waitForTimeout(4000);

        
        page.waitForSelector("text=Online Bookings", new Page.WaitForSelectorOptions().setTimeout(5000));
        page.locator("text=Online Bookings").click();
        

        page.waitForTimeout(2000);
        
        //handle the preferred site popup
        Locator applyButton1 = page.locator("button.xn-button.xn-cta", new Page.LocatorOptions().setHasText("Apply"));     
        
        if (applyButton1.isVisible()) {
            applyButton1.click();
            System.out.println("Clicked on the Apply button in Preferred site Popup.");
        } else {
            System.out.println("Apply button not found in Preferred site Popup");
        }




        page.waitForSelector("text=Sport Courts and Pitches", new Page.WaitForSelectorOptions().setTimeout(5000));
        page.locator("text=Sport Courts and Pitches").click();

        page.fill("input[placeholder='Search activities']", "Squash");
        page.click("#calendar");

        // Calculate current date + 8 days
        LocalDate currentDate = LocalDate.now();
        LocalDate targetDate = currentDate.plusDays(8);
        int targetDay = targetDate.getDayOfMonth();
        String targetDateString = String.valueOf(targetDay);

        page.click("span.day-number:text('" + targetDateString + "')");

        

        // Parse current time to LocalTime for comparison
        LocalTime timeNow = LocalTime.parse(currentTime);
        System.out.println("End time " + timeNow);
        

        /*
        
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

        */
        
        page.waitForTimeout(2000);
        
        page.click("(//button[@class='xn-button xn-primary']/span[@data-bind='text: locationTypeSingular'])[1]");  //delete this
        
        page.waitForTimeout(2000);  //delete this

        int[] selectionOrder = {7, 8, 6, 5, 3, 4, 2, 1};
        for (int courtNumber : selectionOrder) {
            Locator radioButton = page.locator("input[type='radio'][value='" + courtNumber + "']");
            if (radioButton.isVisible()) {
                radioButton.click();
                System.out.println("Clicked on Court " + courtNumber);
                break;
            }
        }

        // Calculate elapsed time
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;  // Time in seconds
        System.out.println("Elapsed Time: " + elapsedTime + " seconds");

        // Wait for remaining time if less than 60 seconds
        if (elapsedTime < 60) {
            long waitTime = 60 - elapsedTime;
            System.out.println("Waiting for " + waitTime + " seconds to complete 60 seconds...");
            page.waitForTimeout(waitTime * 1000);  // Convert seconds to milliseconds
        }

        // Click 'Add to Basket' after the wait
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
