package ru.academits;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
        //Test Data
        String firstName = "Egor";
        String lastName = "Aleksandrov";
        String email = "egor@gmail.com";
        String gender = "Male";
        String mobileNumber = "9139999999";
        String birthDay = "01";
        String birthMonth = "April";
        String birthYear = "1983";
        String subject1 = "Maths";
        String subject2 = "English";
        boolean selectedHobbySport = false;
        boolean selectedHobbyMusic = false;
        boolean selectedHobbyReading = false;
        String pictureName = "picture.jpg";
        String currentAddress = "Novosibirsk Tsvetnoi Proezd 6, 7";
        String state = "NCR";
        String city = "Delhi";

        WebElement firstNameInput = driver.findElement(By.cssSelector("#firstName"));
        firstNameInput.sendKeys(firstName);

        WebElement lastNameInput = driver.findElement(By.cssSelector("#lastName"));
        lastNameInput.sendKeys(lastName);

        WebElement emailInput = driver.findElement(By.cssSelector("#userEmail"));
        emailInput.sendKeys(email);

        Actions mouseClick = new Actions(driver);

        if (gender.equals("Male")) {
            WebElement setMaleGender = driver.findElement(By.cssSelector("[for='gender-radio-1']"));
            mouseClick.moveToElement(setMaleGender).click().perform();
        }

        if (gender.equals("Female")) {
            WebElement setFemaleGender = driver.findElement(By.cssSelector("[for='gender-radio-2']"));
            mouseClick.moveToElement(setFemaleGender).click().perform();
        }

        if (gender.equals("Other")) {
            WebElement setOtherGender = driver.findElement(By.cssSelector("[for='gender-radio-3']"));
            mouseClick.moveToElement(setOtherGender).click().perform();
        }

        WebElement mobileNumberInput = driver.findElement(By.cssSelector("#userNumber"));
        mobileNumberInput.sendKeys(mobileNumber);

        WebElement birthDateInput = driver.findElement(By.cssSelector("#dateOfBirthInput"));
        birthDateInput.sendKeys(Keys.CONTROL + "a");
        birthDateInput.sendKeys(birthDay + " " + birthMonth + " " + birthYear);
        String fullBirthDate = birthDateInput.getAttribute("value");

        WebElement subjectsInput = driver.findElement(By.cssSelector(".subjects-auto-complete__input [aria-autocomplete='list']"));
        subjectsInput.sendKeys(subject1);
        subjectsInput.sendKeys(Keys.RETURN);
        subjectsInput.sendKeys(subject2);
        subjectsInput.sendKeys(Keys.RETURN);

        if (selectedHobbySport) {
            WebElement checkboxReading = driver.findElement(By.cssSelector("#hobbies-checkbox-1"));
            mouseClick.moveToElement(checkboxReading).click().perform();
        }

        if (selectedHobbyMusic) {
            WebElement checkboxMusic = driver.findElement(By.cssSelector("#hobbies-checkbox-3"));
            mouseClick.moveToElement(checkboxMusic).click().perform();
        }

        if (selectedHobbyReading) {
            WebElement checkboxReading = driver.findElement(By.cssSelector("#hobbies-checkbox-2"));
            mouseClick.moveToElement(checkboxReading).click().perform();
        }

        String checkboxSportLabel = driver.findElement(By.cssSelector("[for='hobbies-checkbox-1']")).getText();
        String checkboxReadingLabel = driver.findElement(By.cssSelector("[for='hobbies-checkbox-2']")).getText();
        String checkboxMusicLabel = driver.findElement(By.cssSelector("[for='hobbies-checkbox-3']")).getText();

        File testPicture = new File("src/test/java/ru/academits/" + pictureName);
        WebElement pictureUploadButton = driver.findElement(By.cssSelector("#uploadPicture"));
        pictureUploadButton.sendKeys(testPicture.getAbsolutePath());

        WebElement currentAddressInput = driver.findElement(By.cssSelector("#currentAddress"));
        currentAddressInput.sendKeys(currentAddress);

        WebElement stateSelect = driver.findElement(By.cssSelector("#react-select-3-input"));
        stateSelect.sendKeys(state);
        stateSelect.sendKeys(Keys.RETURN);

        WebElement citySelect = driver.findElement(By.cssSelector("#react-select-4-input"));
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
        Assertions.assertEquals(gender, submittedFormGender);

        //Assert Mobile Number
        String submittedFormMobileNumber = driver.findElement(By.cssSelector("tr:nth-child(4) td:nth-child(2)")).getText();
        Assertions.assertEquals(mobileNumber, submittedFormMobileNumber);
        Assertions.assertEquals(10, submittedFormMobileNumber.length());

        //Assert Date of Birth
        String submittedFormBirthDate = driver.findElement(By.cssSelector("tr:nth-child(5) td:nth-child(2)")).getText();
        String[] splitDateWords = fullBirthDate.split(" ");

        for (String splitDateWord : splitDateWords) {
            Assertions.assertTrue(submittedFormBirthDate.contains(splitDateWord));
        }

        //Assert Subjects
        String submittedFormSubjects = driver.findElement(By.cssSelector("tr:nth-child(6) td:nth-child(2)")).getText();
        Assertions.assertTrue(submittedFormSubjects.contains(subject1));
        Assertions.assertTrue(submittedFormSubjects.contains(subject2));

        //Assert Hobbies
        String submittedFormHobbies = driver.findElement(By.cssSelector("tr:nth-child(7) td:nth-child(2)")).getText();
        String emptyString = "";

        if (selectedHobbySport) {
            Assertions.assertTrue(submittedFormHobbies.contains(checkboxSportLabel));
        } else {
            Assertions.assertEquals(submittedFormHobbies, emptyString);
        }

        if (selectedHobbyMusic) {
            Assertions.assertTrue(submittedFormHobbies.contains(checkboxMusicLabel));
        } else {
            Assertions.assertEquals(submittedFormHobbies, emptyString);
        }

        if (selectedHobbyReading) {
            Assertions.assertTrue(submittedFormHobbies.contains(checkboxReadingLabel));
        } else {
            Assertions.assertTrue(submittedFormHobbies.equals(emptyString));
        }

        //Assert Picture Upload
        String submittedFormUploadPicture = driver.findElement(By.cssSelector("tr:nth-child(8) td:nth-child(2)")).getText();
        Assertions.assertEquals(pictureName, submittedFormUploadPicture);

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