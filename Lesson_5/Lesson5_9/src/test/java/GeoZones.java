import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class GeoZones {


    public WebDriver driver;
    public WebDriverWait wait;

    List<WebElement> countriesList = new ArrayList<WebElement>();
    List<WebElement> zonesList = new ArrayList<WebElement>();

//Подтягиваем драйвер
    public GeoZones(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver,10);
    }

//Используемые селекторы
    private final By countries = By.xpath("//tr[@class='row']");
    private final By countryName = By.tagName("td");
    private final By zoneName = By.xpath("//td[3]/select/option[@selected]");

    @FindBy(id = "box-login")
    WebElement signInForm;

    private final By adminPage = By.xpath("//li[@id='app-']/a/span[contains(text(), 'Appearence')]");
    private final By loginField = By.xpath("//input[@name='username']");
    private final By passwordField = By.xpath("//input[@name='password']");
    private final By loginButton = By.xpath("//button[@name='login']");



//Открываем страницу авторизации
    public void logIn(String login, String password){
        driver.get("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");
        wait.until(ExpectedConditions.visibilityOf(signInForm));
        driver.findElement(loginField).sendKeys(login);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.elementToBeClickable(adminPage));
    }


    public void findGeoZones() {

//проверка алфавитного поярдка стран
        countriesList = driver.findElements(countries);
        for (int i = 0; i < countriesList.size(); i++) {
            String country = countriesList.get(i).findElements(countryName).get(2).getText();

//проверка алфавитного порядка зон
            driver.findElement(By.linkText(country)).click();
            zonesList = driver.findElements(zoneName);
            for (int j = 0; j < zonesList.size() - 1; j++) {
                String zone = zonesList.get(j).getText();
                String nextZone = zonesList.get(j + 1).getText();
                Assert.assertTrue(nextZone.compareTo(zone) > 0);
            }
            driver.navigate().back();
            countriesList = driver.findElements(countries);
        }
    }

}

