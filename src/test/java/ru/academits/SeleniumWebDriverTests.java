package ru.academits;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

public class SeleniumWebDriverTests {
    private static WebDriver driver = null;

    @BeforeAll
    public static void setUp() {
        String browser = System.getProperty("browser");

        if (browser.equals("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        } else if (browser.equals("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }

        driver.get("https://demoqa.com/automation-practice-form");
        driver.manage().window().maximize();
    }

    @Test
    public void demoqaFormFillingTest() {
        WebElement firstNameInput = driver.findElement(By.cssSelector("#firstName"));
        String firstName = "Egor";
        firstNameInput.sendKeys(firstName);

        WebElement lastNameInput = driver.findElement(By.cssSelector("#lastName"));
        String lastName = "Aleksandrov";
        lastNameInput.sendKeys(lastName);

        WebElement emailInput = driver.findElement(By.cssSelector("#userEmail"));
        String email = "egor@gmail.com";
        emailInput.sendKeys(email);

        WebElement setMaleGender = driver.findElement(By.cssSelector("[for='gender-radio-1']"));
        String maleGender = setMaleGender.getText();
        Actions mouseClick = new Actions(driver);
        mouseClick.moveToElement(setMaleGender).click().perform();

        WebElement mobileNumberInput = driver.findElement(By.cssSelector("#userNumber"));
        String mobileNumber = "9139999999";
        mobileNumberInput.sendKeys(mobileNumber);

        WebElement birthDateInput = driver.findElement(By.cssSelector("#dateOfBirthInput"));
        birthDateInput.click();

        WebElement year = driver.findElement(By.cssSelector(".react-datepicker__year-select"));
        Select yearSelect = new Select(year);
        String birthYear = "1983";
        yearSelect.selectByValue(birthYear);

        WebElement month = driver.findElement(By.cssSelector(".react-datepicker__month-select"));
        Select monthSelect = new Select(month);
        String birthMonth = "April";
        monthSelect.selectByVisibleText(birthMonth);

        WebElement birthDay = driver.findElement(By.cssSelector("[aria-label='Choose Friday, April 1st, 1983']"));
        mouseClick.moveToElement(birthDay).click().perform();
        WebElement getBirthdayInfo = driver.findElement(By.cssSelector("#dateOfBirthInput"));
        String birthDayInfo = getBirthdayInfo.getAttribute("value");

        WebElement subjectsInput = driver.findElement(By.cssSelector(".subjects-auto-complete__input [aria-autocomplete='list']"));
        String subjectMaths = "Maths";
        subjectsInput.sendKeys(subjectMaths);
        subjectsInput.sendKeys(Keys.RETURN);
        String subjectEnglish = "English";
        subjectsInput.sendKeys(subjectEnglish);
        subjectsInput.sendKeys(Keys.RETURN);

        WebElement checkboxMusic = driver.findElement(By.cssSelector("#hobbies-checkbox-3"));
        mouseClick.moveToElement(checkboxMusic).click().perform();

        WebElement checkboxReading = driver.findElement(By.cssSelector("#hobbies-checkbox-2"));
        mouseClick.moveToElement(checkboxReading).click().perform();

        File testPicture = new File("src/test/java/ru/academits/picture.jpg");
        WebElement pictureUploadButton = driver.findElement(By.cssSelector("#uploadPicture"));
        pictureUploadButton.sendKeys(testPicture.getAbsolutePath());

        String currentAddress = "Novosibirsk Tsvetnoi Proezd 6, 7";
        WebElement currentAddressInput = driver.findElement(By.cssSelector("#currentAddress"));
        currentAddressInput.sendKeys(currentAddress);

        WebElement stateSelect = driver.findElement(By.cssSelector("#react-select-3-input"));
        String state = "NCR";
        stateSelect.sendKeys(state);
        stateSelect.sendKeys(Keys.RETURN);

        WebElement citySelect = driver.findElement(By.cssSelector("#react-select-4-input"));
        String city = "Delhi";
        citySelect.sendKeys(city);
        citySelect.sendKeys(Keys.RETURN + "\n");

        WebDriverWait wait = new WebDriverWait(driver, 3, 300);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#example-modal-sizes-title-lg")));

        //Assert Student Name
        String submittedFormStudentName = driver.findElement(By.cssSelector("tr:nth-child(1) td:nth-child(2)")).getText();
        Assertions.assertEquals(firstName + " " + lastName, submittedFormStudentName);

        //Assert Student Email
        String submittedFormEmail = driver.findElement(By.cssSelector("tr:nth-child(2) td:nth-child(2)")).getText();
        Assertions.assertTrue(submittedFormEmail.contains("@"));
        Assertions.assertTrue(submittedFormEmail.contains("."));
        Assertions.assertEquals(email, submittedFormEmail);

        //Assert Gender
        String submittedFormGender = driver.findElement(By.cssSelector("tr:nth-child(3) td:nth-child(2)")).getText();
        Assertions.assertEquals(maleGender, submittedFormGender);

        //Assert Mobile Number
        String submittedFormMobileNumber = driver.findElement(By.cssSelector("tr:nth-child(4) td:nth-child(2)")).getText();
        Assertions.assertEquals(mobileNumber, submittedFormMobileNumber);
        Assertions.assertEquals(10, submittedFormMobileNumber.length());

        //Assert Date of Birth
        String submittedFormBirthDate = driver.findElement(By.cssSelector("tr:nth-child(5) td:nth-child(2)")).getText();
        String[] splitDateWords = birthDayInfo.split(" ");
        for (String splitDateWord : splitDateWords) {
            Assertions.assertTrue(submittedFormBirthDate.contains(splitDateWord));
        }

        //Assert Subjects
        String submittedFormSubjects = driver.findElement(By.cssSelector("tr:nth-child(6) td:nth-child(2)")).getText();
        Assertions.assertTrue(submittedFormSubjects.contains(subjectMaths));
        Assertions.assertTrue(submittedFormSubjects.contains(subjectEnglish));

        //Assert Hobbies
        String submittedFormHobbies = driver.findElement(By.cssSelector("tr:nth-child(7) td:nth-child(2)")).getText();
        Assertions.assertTrue(submittedFormHobbies.contains("Music"));
        Assertions.assertTrue(submittedFormHobbies.contains("Reading"));

        //Assert Picture Upload
        String submittedFormUploadPicture = driver.findElement(By.cssSelector("tr:nth-child(8) td:nth-child(2)")).getText();
        Assertions.assertEquals(testPicture.getName(), submittedFormUploadPicture);

        //Assert Address
        String submittedFormAddress = driver.findElement(By.cssSelector("tr:nth-child(9) td:nth-child(2)")).getText();
        Assertions.assertEquals(currentAddress, submittedFormAddress);

        //Assert State and City
        String submittedFormStateAndCity = driver.findElement(By.cssSelector("tr:nth-child(10) td:nth-child(2)")).getText();
        Assertions.assertTrue(submittedFormStateAndCity.contains(state));
        Assertions.assertTrue(submittedFormStateAndCity.contains(city));
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }
}