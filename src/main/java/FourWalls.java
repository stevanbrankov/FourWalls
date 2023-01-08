import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.sql.SQLOutput;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FourWalls {

    private FourWalls() {
    }

    public static final String place = "Novi Sad";
    public static final String priceFrom = "180";
    public static final String priceTo = "350";
    public static final String m2To = "50";

    public static List<List<String>> getApartmentsFromPage() throws InterruptedException {

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

        List<List<String>> apartmentDataList = new ArrayList<>();

        while (true) {
            boolean breakIt = false;

            try {
                chromeDriver.findElement(By.cssSelector(".pagination-container .ng-star-inserted:nth-child(7)"));
            } catch (NoSuchElementException e) {
                breakIt = true;
            }

            List<WebElement> allElementsPerPageParents = chromeDriver.findElements(By.cssSelector("div[waintersectionthreshold='0.25'] app-ad-search-preview a:first-child"));
            List<WebElement> allElementsPerPageChild = chromeDriver.findElements(By.cssSelector("div[waintersectionthreshold='0.25'] app-ad-search-preview a:first-child>div>span[class='mb-2 block text-2xl font-medium']"));

            for (int i = 0; i < allElementsPerPageParents.size(); i += 2) {
                //create list for one apartment data
                List<String> oneApartment = new ArrayList<>();
                //refresh
                Thread.sleep(100);
                allElementsPerPageParents = chromeDriver.findElements(By.cssSelector("div[waintersectionthreshold='0.25'] app-ad-search-preview a:first-child"));
                allElementsPerPageChild = chromeDriver.findElements(By.cssSelector("div[waintersectionthreshold='0.25'] app-ad-search-preview a:first-child>div>span[class='mb-2 block text-2xl font-medium']"));
                //extracting data
                String link = allElementsPerPageParents.get(i).getAttribute("href");
                String price = allElementsPerPageChild.get(i == 0 ? 0 : (i / 2)).getText();
                oneApartment.add(link);
                oneApartment.add(price);
                apartmentDataList.add(oneApartment);
            }

            if (breakIt) {
                chromeDriver.close();
                return apartmentDataList;
            }
            chromeDriver.findElement(By.cssSelector(".pagination-container .ng-star-inserted:nth-child(7)")).click();
            Thread.sleep(500);
        }
    }

    public static List<List<String>> getPriceChangedData(List<List<String>> fromExcelData, List<List<String>> fromPageData) {
        List<List<String>> retVal = new ArrayList<>();
        for (List<String> elementOfExcelData : fromExcelData) {
            for (List<String> elementOfFromPageData : new ArrayList<>(fromPageData)) {
                if (elementOfExcelData.get(0).equals(elementOfFromPageData.get(0))) {
                    if (!elementOfExcelData.get(1).equals(elementOfFromPageData.get(1))) {
                        retVal.add(elementOfFromPageData);
                    }
                }
            }
        }
        return retVal;
    }

    public static List<List<String>> getNewData(List<List<String>> fromExcelData, List<List<String>> fromPageData) {
        List<List<String>> newData = new ArrayList<>(fromPageData);
        for (List<String> elementOfExcelData : fromExcelData) {
            for (List<String> elementOfFromPageData : fromPageData) {
                if (elementOfExcelData.get(0).equals(elementOfFromPageData.get(0)))
                    newData.remove(elementOfFromPageData);
            }
        }
        return newData;
    }
}
