import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FourWalls {

    private FourWalls() {
    }

    public static final String place = "Novi Sad";
    public static final String priceFrom = "180";
    public static final String priceTo = "350";
    public static final String m2To = "50";

    public static Set<String> getApartments() throws InterruptedException, IOException {

        //ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless", "--window-size=1920,1200");

        WebDriver chromeDriver = new ChromeDriver(/*options*/);

        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));

        chromeDriver.manage().window().maximize();
        chromeDriver.get("https://www.4zida.rs/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[for='mat-radio-3-input']")));
        chromeDriver.findElement(By.cssSelector("[for='mat-radio-3-input']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#mat-mdc-chip-list-input-0")));
        chromeDriver.findElement(By.cssSelector("input#mat-mdc-chip-list-input-0")).sendKeys(place);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#mat-option-5")));
        chromeDriver.findElement(By.cssSelector("#mat-option-5")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[type='submit']")));
        chromeDriver.findElement(By.cssSelector("[type='submit']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#mat-input-0")));
        chromeDriver.findElement(By.cssSelector("#mat-input-0")).sendKeys(priceFrom);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#mat-input-1")));
        chromeDriver.findElement(By.cssSelector("#mat-input-1")).sendKeys(priceTo);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#mat-input-3")));
        chromeDriver.findElement(By.cssSelector("#mat-input-3")).sendKeys(m2To);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[type='submit']")));
        chromeDriver.findElement(By.cssSelector("[type='submit']")).click();

        Set<String> noCopies = new HashSet<>();

        while (true) {
            boolean breakIt = false;

            try {
                chromeDriver.findElement(By.cssSelector(".pagination-container .ng-star-inserted:nth-child(7)"));
            } catch (NoSuchElementException e) {
                breakIt = true;
            }

            List<WebElement> perPage = chromeDriver.findElements(By.cssSelector("div[waintersectionthreshold='0.25'] app-ad-search-preview a:first-child"));

            for (int i = 0; i < perPage.size(); i++) {
                Thread.sleep(100);
                perPage = chromeDriver.findElements(By.cssSelector("div[waintersectionthreshold='0.25'] app-ad-search-preview a:first-child"));
                noCopies.add(perPage.get(i).getAttribute("href"));
            }

            if (breakIt) {
                chromeDriver.close();
                break;
            }
            chromeDriver.findElement(By.cssSelector(".pagination-container .ng-star-inserted:nth-child(7)")).click();
            Thread.sleep(500);
        }

        filterSet(ExcelOperations.readFromExcelFile(), noCopies);
        ExcelOperations.writeInExcelFile(noCopies);

        return noCopies;

    }

    public static void filterSet(List<String> list, Set<String> set) {
        for (String e : new HashSet<>(set)) {
            if (list.contains(e)) {
                set.remove(e);
            }
        }
    }
}
