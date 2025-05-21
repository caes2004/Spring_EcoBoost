
import java.time.Duration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class End2End {

    private WebDriver webDriver;

    @Before
    public void setUp() {
        WebDriverManager.edgedriver().setup();
        webDriver = new EdgeDriver();

    }

    private void pausa(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void end2endComprador() {

        webDriver.get("http://localhost:8080");

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        pausa(1000);

        WebElement userIcon = wait.until(ExpectedConditions.elementToBeClickable(By.id("userIcon")));
        userIcon.click();
        pausa(2000);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("video_eco_boost")));

        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.ENTER).perform();
        pausa(1500);

        wait.until(ExpectedConditions.urlContains("/login"));

        webDriver.findElement(By.name("document")).sendKeys("000");
        pausa(1000);
        webDriver.findElement(By.name("password")).sendKeys("000");
        pausa(1000);
        webDriver.findElement(By.className("btn")).click();
        pausa(20000);

        // Esperar a que aparezca el SweetAlert
        WebElement botonOk = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".swal2-confirm")));

        // Hacer clic en "OK"
        botonOk.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".swal2-popup")));

        WebElement cantidadInput = webDriver.findElement(By.name("cantidad"));
        cantidadInput.clear();
        cantidadInput.sendKeys("30");
        pausa(3000);
        WebElement botonAgregar = webDriver.findElement(By.name("agregar"));
        botonAgregar.click();
        pausa(4000);

        //Agregó correctamente al carrito

    }

   
}
