package leisureCenter;

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
        Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(true));
        Page page = browser.newPage();

        page.navigate("https://portal.everybody.org.uk/LhWeb/en/members/home/");
        page.waitForLoadState(LoadState.LOAD);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);

        handleCookiesPopup(page);
        handlePreferredSitePopup(page);

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
        //String targetDateString = String.valueOf(targetDay);
        
        selectCalendarDateByDayNumber(page, targetDay);
        
        //page.click("span.day-number:text('" + targetDateString + "')");

        

        LocalTime timeNow = LocalTime.now(ZoneId.of("Europe/London"));
        System.out.println("Current UK Time now is: " + timeNow);

        if ((timeNow.equals(LocalTime.of(9, 01)) || timeNow.isAfter(LocalTime.of(9, 01))) && timeNow.isBefore(LocalTime.of(9, 30))) {
            clickSelectCourtByTime(page, "09:30");
        } else if ((timeNow.equals(LocalTime.of(10, 01)) || timeNow.isAfter(LocalTime.of(10, 01))) && timeNow.isBefore(LocalTime.of(10, 30))) {
            clickSelectCourtByTime(page, "10:30");
        } else if ((timeNow.equals(LocalTime.of(11, 01)) || timeNow.isAfter(LocalTime.of(11, 01))) && timeNow.isBefore(LocalTime.of(11, 30))) {
            clickSelectCourtByTime(page, "11:30");
        } else if ((timeNow.equals(LocalTime.of(12, 01)) || timeNow.isAfter(LocalTime.of(12, 01))) && timeNow.isBefore(LocalTime.of(12, 30))) {
            clickSelectCourtByTime(page, "12:30");
        } else if ((timeNow.equals(LocalTime.of(13, 01)) || timeNow.isAfter(LocalTime.of(13, 01))) && timeNow.isBefore(LocalTime.of(13, 30))) {
            clickSelectCourtByTime(page, "13:30");
        } else if ((timeNow.equals(LocalTime.of(14, 01)) || timeNow.isAfter(LocalTime.of(14, 01))) && timeNow.isBefore(LocalTime.of(14, 30))) {
            clickSelectCourtByTime(page, "14:30");
        } else if ((timeNow.equals(LocalTime.of(15, 01)) || timeNow.isAfter(LocalTime.of(15, 01))) && timeNow.isBefore(LocalTime.of(15, 30))) {
            clickSelectCourtByTime(page, "15:30");
        } else if ((timeNow.equals(LocalTime.of(16, 30)) || timeNow.isAfter(LocalTime.of(16, 30))) && timeNow.isBefore(LocalTime.of(17, 30))) {
            clickSelectCourtByTime(page, "16:30");
        } else {
            String errorMessage = "No slot selection matched for the current time: " + currentTime;
            System.out.println(errorMessage);
            Assert.fail(errorMessage);
        }

        page.waitForTimeout(3000);

        int[] selectionOrder = {7, 8, 6, 5, 3, 4, 2, 1};
        for (int courtNumber : selectionOrder) {
            Locator radioButton = page.locator("input[type='radio'][value='" + courtNumber + "']");
            if (radioButton.isVisible()) {
                radioButton.click();
                System.out.println("Clicked on Court " + courtNumber);
                break;
            }
        }

        page.waitForTimeout(4000);
        
        WaitforExactTime();

        // Click on Add to Basket
        page.click("(//button[@class='xn-button xn-mute']/following-sibling::button)[3]");
        
        
        Locator ctaButton = page.locator("//a[@class='xn-button xn-cta']");
        if (ctaButton.isVisible()) {
            ctaButton.click();
        } else {
            page.click("(//div[@data-bind='event: {keypress: toggleBasket}, escapePressed: handleEscapeKeyPressed()']//div)[1]");
            ctaButton.click();
        }


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
        List<String> validTimes = Arrays.asList("09:30", "10:30", "11:30", "12:30", "13:30", "14:30", "15:30", "16:30");
        boolean timeMatched = false;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        while (!timeMatched) {
            LocalTime currentLocalTime = LocalTime.now(ZoneId.of("Europe/London"));
            String currentTimeStr = currentLocalTime.format(timeFormatter);
            System.out.println("Current UK time: " + currentTimeStr);

            if (validTimes.contains(currentTimeStr)) {
                timeMatched = true;
                System.out.println("Time matched! Proceeding...");
            } else {
                System.out.println("Waiting for the correct time...");
                Thread.sleep(1000);
            }
        }
    }

    public static void clickSelectCourtByTime(Page page, String targetTime) {
        page.waitForTimeout(3000);
        String selector = String.format(
            "xpath=//li[.//div[@class='xn-booking-starttime' and contains(., '%s')]]//button[contains(., 'Select Court')]",
            targetTime
        );

        Locator selectCourtButton = page.locator(selector);
        if (selectCourtButton.count() > 0 && selectCourtButton.first().isVisible()) {
            selectCourtButton.first().click();
            System.out.println("Clicked 'Select Court' for time: " + targetTime);
        } else {
            System.out.println("'Select Court' button not found for time: " + targetTime);
        }
    }
    
    
    
    public static void selectCalendarDateByDayNumber(Page page, int targetDay) {
        String dayString = String.valueOf(targetDay);
        String dayXPath = String.format("//span[text()='%s']", dayString);
        Locator targetDayElement = page.locator(dayXPath);
        Locator parentDayElement = targetDayElement.locator("xpath=.."); // Get the parent <td>
        Locator nextMonthButton = page.locator(".next-month");

        if (targetDayElement.count() > 0 && targetDayElement.first().isVisible()) {
            String parentClass = parentDayElement.first().getAttribute("class");
            boolean isDisabled = false;
            if (parentClass != null && (parentClass.contains("disabled") || parentClass.contains("not-in-month") || parentClass.contains("unavailable") || parentClass.contains("off"))) {
                isDisabled = true;
            }
            if (!isDisabled) {
                targetDayElement.first().click();
                System.out.println("Clicked on day: " + targetDay + " in the current month.");
            } else {
                System.out.println("Day " + targetDay + " appears disabled in the current month.");
                nextMonthButton.click();
                System.out.println("Clicked next month. Retrying for day: " + dayString);

                // Re-locate the target day element using XPath after navigating to the next month
                targetDayElement = page.locator(String.format("//span[text()='%s']", dayString));
                parentDayElement = targetDayElement.locator("xpath=..");

                // Wait for the target day to be visible in the next month
                page.waitForSelector(String.format("//span[text()='%s']", dayString), new Page.WaitForSelectorOptions().setTimeout(3000));

                if (targetDayElement.count() > 0 && targetDayElement.first().isVisible()) {
                    String nextMonthParentClass = parentDayElement.first().getAttribute("class");
                    boolean isNextMonthDisabled = false;
                    if (nextMonthParentClass != null && (nextMonthParentClass.contains("disabled") || nextMonthParentClass.contains("not-in-month") || nextMonthParentClass.contains("unavailable") || nextMonthParentClass.contains("off"))) {
                        isNextMonthDisabled = true;
                    }
                    if (!isNextMonthDisabled) {
                        targetDayElement.first().click();
                        System.out.println("Selected the date: " + dayString + " in the next month.");
                    } else {
                        System.out.println("Day " + dayString + " found but still disabled in the next month.");
                    }
                } else {
                    System.out.println("Day " + dayString + " not found in the next month.");
                }
            }
        } else {
            System.out.println("Day " + targetDay + " is either not present or not visible in the current month.");
        }
    }


    
    
}