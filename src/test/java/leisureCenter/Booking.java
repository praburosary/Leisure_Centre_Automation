package leisureCenter;

import java.io.File;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;

public class Booking {

    // Author: Prabu Munuswamy || prabhureuben@gmail.com

    @Test
    public void browser_actions() throws InterruptedException {

    	
    	
        // Use UK time zone explicitly
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/London"));
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        String currentTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        System.out.println("Today is: " + dayOfWeek + ", Current time: " + currentTime);

        Playwright pw = Playwright.create();
        Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(false));
        Page page = browser.newPage();
                
        // Prepare the screenshot directory
        prepareScreenshotDirectory("Screenshot");

        page.navigate("https://portal.everybody.org.uk/LhWeb/en/members/home/");
        page.waitForLoadState(LoadState.LOAD);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        takeScreenshot(page, "Screenshot", "01_Application_Opened.png");
        
        handleCookiesPopup(page);
        handlePreferredSitePopup(page);
        
        LocalTime timeNow = LocalTime.now(ZoneId.of("Europe/London"));
        System.out.println("Current UK Time now is: " + timeNow);
        
     // Determine credentials based on UK time
        String username = "";
        String password = "";
        
        if (timeNow.isAfter(LocalTime.of(15, 39)) && timeNow.isBefore(LocalTime.of(16, 30))) {
            username = "Lorenzomichaeluk@gmail.com";
            password = "Appleuk@123";
        } else if (timeNow.isAfter(LocalTime.of(14, 39)) && timeNow.isBefore(LocalTime.of(15, 30))) {
            username = "ponshirly@gmail.com";
            password = "Iniya2015$";
        } else if (timeNow.isAfter(LocalTime.of(13, 39)) && timeNow.isBefore(LocalTime.of(14, 30))) {
            username = "";
            password = "";
        } else {
        	
        	//username = "prabhureuben@gmail.com";
            //password = "Rosary08**";
            throw new RuntimeException("No matching login window for current time: " + timeNow);
        }
        

        page.type("#xn-Username", username);
        page.type("#xn-Password", password);
        page.click("#login");
        System.out.println("Clicked on Login Button successfully");
        page.waitForTimeout(6000);
        
        closeLocationAlert(page);
        
        //confirm Login
        Locator loginHeader = page.locator("//h1[@class='xn-title']");

        if (loginHeader.count() > 0 && loginHeader.first().isVisible()) {
            System.out.println("Login is successful");
            takeScreenshot(page, "Screenshot", "07_login_successful.png");
        } else {
            System.out.println("Login is NOT successful");
        }

        handleCookiesPopup(page);
        
        handlePreferredSitePopup(page);
        
        closeLocationAlert(page);

        //click on Online Bookings and confirm
        page.waitForSelector("text=Online Bookings", new Page.WaitForSelectorOptions().setTimeout(10000));
        Locator onlineBookings = page.locator("text=Online Bookings");
        if (onlineBookings.count() > 0 && onlineBookings.first().isVisible()) {
            onlineBookings.click();
            page.waitForTimeout(8000);
            System.out.println("Clicked on 'Online Bookings'");
            takeScreenshot(page, "Screenshot", "08_Clicked_on_OnlineBooking.png");
        } else {
            System.out.println("'Online Bookings' element not found or not visible.");
        }
              
        
        handlePreferredSitePopup(page);
        
        closeLocationAlert(page);

        page.waitForSelector("text=Sport Courts and Pitches", new Page.WaitForSelectorOptions().setTimeout(10000));
        page.locator("text=Sport Courts and Pitches").click();
        page.waitForTimeout(4000);
        takeScreenshot(page, "Screenshot", "09_Clicked_on_SportCourtsandPitches.png");
        System.out.println("Sport Courts and Pitches button successfully");

        page.fill("input[placeholder='Search activities']", "badminton");
        page.waitForTimeout(2000);
        takeScreenshot(page, "Screenshot", "10_Filtered_Badminton.png");
        page.click("#calendar");
        System.out.println("Clicked on the calendar successfully");
        takeScreenshot(page, "Screenshot", "11_Calendar.png");
        LocalDate targetDate = LocalDate.now(ZoneId.of("Europe/London")).plusDays(8);
        int targetDay = targetDate.getDayOfMonth();        
        selectCalendarDateByDayNumber(page, targetDay);
        page.waitForTimeout(8000);
        takeScreenshot(page, "Screenshot", "12_Selection_of_DesiredDate.png");
        
        

        if (timeNow.isAfter(LocalTime.of(5, 39)) && timeNow.isBefore(LocalTime.of(6, 30))) {
            clickSelectCourtByTime(page, "06:30");
        } else if (timeNow.isAfter(LocalTime.of(6, 39)) && timeNow.isBefore(LocalTime.of(7, 30))) {
            clickSelectCourtByTime(page, "07:30");
        } else if (timeNow.isAfter(LocalTime.of(7, 39)) && timeNow.isBefore(LocalTime.of(8, 30))) {
            clickSelectCourtByTime(page, "08:30");
        } else if (timeNow.isAfter(LocalTime.of(8, 39)) && timeNow.isBefore(LocalTime.of(9, 30))) {
            clickSelectCourtByTime(page, "09:30");
        } else if (timeNow.isAfter(LocalTime.of(9, 39)) && timeNow.isBefore(LocalTime.of(10, 30))) {
            clickSelectCourtByTime(page, "10:30");
        } else if (timeNow.isAfter(LocalTime.of(10, 39)) && timeNow.isBefore(LocalTime.of(11, 30))) {
            clickSelectCourtByTime(page, "11:30");
        } else if (timeNow.isAfter(LocalTime.of(11, 39)) && timeNow.isBefore(LocalTime.of(12, 30))) {
            clickSelectCourtByTime(page, "12:30");
        } else if (timeNow.isAfter(LocalTime.of(12, 39)) && timeNow.isBefore(LocalTime.of(13, 30))) {
            clickSelectCourtByTime(page, "13:30");
        } else if (timeNow.isAfter(LocalTime.of(13, 39)) && timeNow.isBefore(LocalTime.of(14, 30))) {
            clickSelectCourtByTime(page, "14:30");
        } else if (timeNow.isAfter(LocalTime.of(14, 39)) && timeNow.isBefore(LocalTime.of(15, 30))) {
            clickSelectCourtByTime(page, "15:30");
        } else if (timeNow.isAfter(LocalTime.of(15, 39)) && timeNow.isBefore(LocalTime.of(16, 30))) {
            clickSelectCourtByTime(page, "16:30");
        } else {
            String errorMessage = "No slot selection matched for the current time: " + timeNow;
            System.out.println(errorMessage);
            Assert.fail(errorMessage);
        }

        page.waitForTimeout(3000);

        int[] selectionOrder = {6, 8, 5, 4, 2, 1};
        for (int courtNumber : selectionOrder) {
            Locator radioButton = page.locator("input[type='radio'][value='" + courtNumber + "']");
            if (radioButton.isVisible()) {
                radioButton.click();
                page.waitForTimeout(4000);
                takeScreenshot(page, "Screenshot", "14_Court_selected.png");
                System.out.println("Selected the desired Court " + courtNumber);
                break;
            }
        }

        page.waitForTimeout(2000);
        
        WaitforExactTime();

        // Click on Add to Basket
        page.click("(//button[@class='xn-button xn-mute']/following-sibling::button)[3]");
        page.waitForTimeout(6000);
        takeScreenshot(page, "Screenshot", "15_Clicked_on_AddtoBasket.png");
        proceedToCheckout(page);
        
       
        page.waitForTimeout(2000);
        takeScreenshot(page, "Screenshot", "17_Ready_to_PayNow.png");
        page.click("text=Pay Now");
        
        System.out.println("Click on 'Pay Now' - is successful");
        page.waitForTimeout(6000);
        takeScreenshot(page, "Screenshot", "18_Clicked_PayNow.png");
        
        Locator confirmationText = page.locator("h1.xn-title");
        if (confirmationText.textContent().equals("Transaction Confirmation")) {
        	takeScreenshot(page, "Screenshot", "19_BookingSuccessful.png");
            System.out.println("Booking is successful");
        } else {
            System.out.println("Booking is NOT successful.");
        }

        browser.close();
    }

    private void handlePreferredSitePopup(Page page) {
        //Locator applyButton = page.locator("button.xn-button.xn-cta", new Page.LocatorOptions().setHasText("Apply"));
    	Locator applyButton = page.locator("xpath=//div[@class='xn-buttons']/button[text()='Apply' and contains(@class, 'xn-button') and contains(@class, 'xn-cta')]");
    	//Locator applyButton = page.locator("xpath=//button[text()='Apply']");
        if (applyButton.isVisible()) {
        	
        	// Locate the dropdown using its ID
        	Locator siteDropdown = page.locator("#xn-site-selector");
        	// Select the option by visible text
        	siteDropdown.selectOption(new SelectOption().setLabel("Macclesfield Leisure Centre"));
        	takeScreenshot(page, "Screenshot", "04_PreferredSitePopup.png");
        	System.out.println("Selected site: Macclesfield Leisure Centre");

            applyButton.click();
        }
    }

    private void handleCookiesPopup(Page page) throws InterruptedException {
        Locator yesRadio = page.locator("input[type='radio'][name='rbGoogle'][value='1']");
        
        if (yesRadio.count() > 0 && yesRadio.isVisible()) {
            yesRadio.check();
            takeScreenshot(page, "Screenshot", "02_Cookies_yes_radioButton_selected.png");
        }

        Locator acceptButton = page.locator("button.xn-button.xn-cta", new Page.LocatorOptions().setHasText("Accept"));
        Thread.sleep(3000);
        if (yesRadio.count() > 0 && yesRadio.isChecked()) {        	       	
            if (acceptButton.count() > 0 && acceptButton.isVisible()) {
                acceptButton.click();
                takeScreenshot(page, "Screenshot", "03_cookies_accpeted_closed.png");
                System.out.println("Accepted Cookies Popup");
            }
        }
    }

    private void WaitforExactTime() throws InterruptedException {
        List<LocalTime> targetTimes = Arrays.asList(
            LocalTime.of(6, 30, 1), // Target 06:30:01
            LocalTime.of(7, 30, 1),
            LocalTime.of(8, 30, 1),
            LocalTime.of(9, 30, 1),
            LocalTime.of(10, 30, 1),
            LocalTime.of(11, 30, 1), // Target 11:30:01
            LocalTime.of(12, 30, 1),
            LocalTime.of(13, 30, 1),
            LocalTime.of(14, 30, 1),
            LocalTime.of(15, 30, 1),
            LocalTime.of(16, 30, 1)
        );

        boolean timeMatched = false;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        while (!timeMatched) {
            LocalTime currentLocalTime = LocalTime.now(ZoneId.of("Europe/London"));
            String currentTimeStr = currentLocalTime.format(timeFormatter);
            System.out.println("Current UK time: " + currentTimeStr);

            for (LocalTime targetTime : targetTimes) {
                // Check if current time is at or after the target time, within a small window
                if (currentLocalTime.isAfter(targetTime.minusSeconds(2)) && currentLocalTime.isBefore(targetTime.plusSeconds(2))) {
                    timeMatched = true;
                    System.out.println("Time matched (within a 2-second window)! Proceeding...");
                    break; // Exit the for loop
                }
            }

            if (!timeMatched) {
                System.out.println("Waiting for the correct time...");
                Thread.sleep(500); // Reduce sleep to check more frequently
            }
        }
    }

    public static void clickSelectCourtByTime(Page page, String targetTime) {
        page.waitForTimeout(6000);
        String selector = String.format(
            "xpath=//li[.//div[@class='xn-booking-starttime' and contains(., '%s')]]//button[contains(., 'Select Court')]",
            targetTime
        );

        Locator selectCourtButton = page.locator(selector);
        if (selectCourtButton.count() > 0 && selectCourtButton.first().isVisible()) {
            selectCourtButton.first().click();
            page.waitForTimeout(4000);
            takeScreenshot(page, "Screenshot", "13_Clicked_on_SelectCourt_Button.png");
            System.out.println("Clicked 'Select Court' for time: " + targetTime);
        } else {
            System.out.println("'Select Court' button not found for time: " + targetTime);
            takeScreenshot(page, "Screenshot", "13_SelectCourtButtonNotFound.png");
        }
    }
    
    
      

        public static void selectCalendarDateByDayNumber(Page page, int targetDay) {
            String dayString = String.valueOf(targetDay);
            String daySpanXPath = String.format("//span[text()='%s']", dayString);
            Locator allTargetDaySpans = page.locator(daySpanXPath);
            Locator nextMonthButton = page.locator(".next-month");

            System.out.println("Attempting to select day: " + targetDay);

            // Scenario 1: Look for the target day within the current view
            // We'll iterate through all occurrences of the targetDay (e.g., two '28's)
            Locator selectedElement = null;

            for (int i = 0; i < allTargetDaySpans.count(); i++) {
                Locator currentDaySpan = allTargetDaySpans.nth(i);
                Locator parentTd = currentDaySpan.locator("xpath=.."); // Get the parent <td>

                // Ensure the element is visible before checking its attributes
                if (!currentDaySpan.isVisible()) {
                    System.out.println("Skipping invisible span for day: " + targetDay);
                    continue;
                }

                String parentClass = parentTd.getAttribute("class");
                boolean isDisabledOrNotInMonth = false;

                if (parentClass != null && (parentClass.contains("disabled") || parentClass.contains("not-in-month") || parentClass.contains("unavailable") || parentClass.contains("off"))) {
                    isDisabledOrNotInMonth = true;
                }

                System.out.println("Found day " + targetDay + " (occurrence " + (i + 1) + "). Parent class: '" + parentClass + "'. Is disabled/not-in-month: " + isDisabledOrNotInMonth);

                if (!isDisabledOrNotInMonth) {
                    // This is an enabled, in-month occurrence
                    selectedElement = currentDaySpan;
                    break; // Found a selectable element, no need to check further occurrences in this month
                }
            }

            if (selectedElement != null) {
                // Found an enabled date in the current calendar view
                selectedElement.click();
                System.out.println("Selected the date: " + targetDay + " in the current month view.");
            } else {
                // No enabled occurrence found in the current calendar view (either all were disabled or not found)
                System.out.println("Day " + targetDay + " not found as enabled in the current month view or all occurrences are disabled.");

                // Proceed to click next month button and retry
                if (nextMonthButton.count() > 0 && nextMonthButton.isVisible()) {
                    System.out.println("Clicking next month button to find day: " + targetDay);
                    nextMonthButton.click();
                    page.waitForTimeout(500); // Give a small pause for the calendar to update

                    // Re-locate all target day spans after navigating to the next month
                    allTargetDaySpans = page.locator(daySpanXPath);

                    boolean foundAndSelectedInNextMonth = false;
                    for (int i = 0; i < allTargetDaySpans.count(); i++) {
                        Locator currentDaySpan = allTargetDaySpans.nth(i);
                        Locator parentTd = currentDaySpan.locator("xpath=..");

                        if (!currentDaySpan.isVisible()) {
                            System.out.println("Skipping invisible span for day: " + targetDay + " in next month.");
                            continue;
                        }

                        String parentClass = parentTd.getAttribute("class");
                        boolean isDisabledOrNotInMonth = false;
                        if (parentClass != null && (parentClass.contains("disabled") || parentClass.contains("not-in-month") || parentClass.contains("unavailable") || parentClass.contains("off"))) {
                            isDisabledOrNotInMonth = true;
                        }

                        System.out.println("Found day " + targetDay + " in next month (occurrence " + (i + 1) + "). Parent class: '" + parentClass + "'. Is disabled/not-in-month: " + isDisabledOrNotInMonth);

                        if (!isDisabledOrNotInMonth) {
                            currentDaySpan.click();
                            System.out.println("Selected the date: " + targetDay + " in the next month.");
                            foundAndSelectedInNextMonth = true;
                            break;
                        }
                    }

                    if (!foundAndSelectedInNextMonth) {
                        System.out.println("Day " + targetDay + " not found as enabled in the next month either.");
                    }

                } else {
                    System.out.println("Next month button not found or not visible.");
                }
            }
        }
    
    
    
    
    public static void proceedToCheckout(Page page) {
        Locator ctaButton = page.locator("//a[@class='xn-button xn-cta']");

        if (ctaButton.isVisible()) {
            ctaButton.click();
            System.out.println("Clicked on 'Check-Out' button - is successful");
        } else {
            System.out.println("'Check-Out' button not visible initially. Trying to reveal...");

            // Take screenshot before attempting cart icon click
            takeScreenshot(page, "Screenshot", "16_Checkout_button_not_visible_initially.png");

            // Try to close or wait for interfering modal if known
            Locator overlay = page.locator("#xn-select-sublocation");
            if (overlay.isVisible()) {
                System.out.println("Blocking overlay detected. Waiting to disappear...");
                overlay.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.HIDDEN)
                    .setTimeout(5000));
            }

            // Click on cart icon forcefully to bypass intercept
            
            page.locator("//div[@class='xn-icon']")
            //page.locator("(//div[@data-bind='event: {keypress: toggleBasket}, escapePressed: handleEscapeKeyPressed()']//div)[1]")
                .click(new Locator.ClickOptions().setForce(true));

            // Optional wait to allow DOM update
            page.waitForTimeout(2000);

            // Wait for the CTA to appear and become visible
            ctaButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));

            if (ctaButton.isVisible()) {
                takeScreenshot(page, "Screenshot", "16_Checkout_cart_is_Visible.png");
                ctaButton.click();
                System.out.println("Hovered over the cart and clicked on 'Check-Out' button - is successful");
            } else {
                System.err.println("Check-Out button still not visible after cart interaction");
            }
        }
    }

    

    
    public static void closeLocationAlert(Page page) {
        Locator closeIcon = page.locator("(//div[@class='xn-close'])[2]");

        if (closeIcon.count() > 0 && closeIcon.first().isVisible()) {
        	takeScreenshot(page, "Screenshot", "05_LocationAlert.png");
            closeIcon.first().click();
            System.out.println("Closed the location alert updates");
            takeScreenshot(page, "Screenshot", "06_Closed_Location_Alert.png");
        } else {
            System.out.println("Location alert close icon not visible or not present.");
        }
    }
    
        
    public static void prepareScreenshotDirectory(String directoryName) {
        try {
            // Create directory "Screenshot" if it does not exist
            File screenshotDir = new File(directoryName);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
                System.out.println("Screenshot directory created: " + screenshotDir.getAbsolutePath());
            } else {
                // Delete all files in the directory
                File[] files = screenshotDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.exists()) {
                            boolean isDeleted = file.delete();
                            if (isDeleted) {
                                System.out.println("Deleted old file: " + file.getAbsolutePath());
                            } else {
                                System.err.println("Failed to delete old file: " + file.getAbsolutePath());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error while preparing screenshot directory: " + e.getMessage());
        }
    }

    
    
    public static void takeScreenshot(Page page, String directoryName, String fileName) {
        try {
            // Create the file path
            File screenshotFile = new File(directoryName, fileName);

            // Take and save the screenshot
            page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(screenshotFile.getAbsolutePath()))
                .setFullPage(true));

            System.out.println("Screenshot saved to: " + screenshotFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }

    
    
    
}