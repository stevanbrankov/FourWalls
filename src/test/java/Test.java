import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.List;

public class Test {

    @org.testng.annotations.Test
    public static void API() throws InterruptedException, TwitterException, IOException {

        Twitter twitter = TwitterAppBot.init();

        List<List<String>> apartmentsFromPage = FourWalls.getApartmentsFromPage();
        List<List<String>> apartmentsFromExcel = ExcelOperations.readFromExcelFile();
        List<List<String>> nonExistingApartments = FourWalls.getNewData(apartmentsFromExcel, apartmentsFromPage);

        if (!nonExistingApartments.isEmpty()) {
            //update excel with new apartments
            ExcelOperations.writeNewDataInExcelFile(nonExistingApartments);
            //send info
            System.out.println("There are some new apartments:");
            for (List<String> apartment : nonExistingApartments) {
                System.out.println(apartment);
                twitter.sendDirectMessage("FourWallsBro", apartment.get(0));
                Thread.sleep(1500);
            }
        }


        //find apartments where price has changed
        apartmentsFromExcel = ExcelOperations.readFromExcelFile();
        List<List<String>> priceChangedApartments = FourWalls.getPriceChangedData(apartmentsFromExcel, apartmentsFromPage);

        if (!priceChangedApartments.isEmpty()) {
            //update excel with price changed apartments
            ExcelOperations.writeExistingDataInExcelFile(priceChangedApartments);
            //send info
            System.out.println("There are some price changes:");
            for (List<String> priceChangedApartment : priceChangedApartments) {
                //find apartment's existing/old price
                String oldPrice = apartmentsFromExcel.stream().filter(ape -> ape.get(0).equals(priceChangedApartment.get(0))).findAny().orElse(null).get(1);
                String newPrice = priceChangedApartment.get(1);
                System.out.println(priceChangedApartment + " ->  from: " + oldPrice + " to: " + newPrice);
                twitter.sendDirectMessage("FourWallsBro", priceChangedApartment.get(0) + "\nfrom: " + oldPrice + " to: " + newPrice);
                Thread.sleep(1500);
            }
        }
    }
}
