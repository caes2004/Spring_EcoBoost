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

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        // 1. Ir al home
        webDriver.get("http://localhost:8080");
        pausa(1000);

        // 2. Click en ícono de usuario
        WebElement userIcon = wait.until(ExpectedConditions.elementToBeClickable(By.id("userIcon")));
        userIcon.click();
        pausa(2000);

        // 3. Saltar el video (ENTER)
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("video_eco_boost")));
        new Actions(webDriver).sendKeys(Keys.ENTER).perform();
        pausa(1500);

        // 4. Login
        wait.until(ExpectedConditions.urlContains("/login"));
        webDriver.findElement(By.name("document")).sendKeys("000");
        pausa(500);
        webDriver.findElement(By.name("password")).sendKeys("000");
        pausa(500);
        webDriver.findElement(By.className("btn")).click();
        pausa(2000);

        // 5. Aceptar SweetAlert
        WebElement botonOk = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".swal2-confirm")));
        botonOk.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".swal2-popup")));

        // 6. Agregar producto al carrito
        WebElement cantidadInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("cantidad")));
        cantidadInput.clear();
        cantidadInput.sendKeys("1");
        WebElement botonAgregar = webDriver.findElement(By.name("agregar"));
        botonAgregar.click();
        pausa(3000);

        // 7. Ir al carrito
        webDriver.get("http://localhost:8080/comprador/carrito");
        pausa(2000);

        // 8. Proceder al pago
        WebElement btnPago = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Proceder al Pago")));
        btnPago.click();
        pausa(2000);

        // 9. Confirmar compra
        WebElement btnConfirmar = wait.until(ExpectedConditions.elementToBeClickable(By.id("facturaBtn")));
        btnConfirmar.click();
        pausa(3000);

        // 10. Validar mensaje de éxito
        WebElement mensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//*[contains(text(),'¡Compra realizada con éxito!')]")
        ));
        assert mensaje.getText().contains("¡Compra realizada con éxito!");

        // 11. Simular clic en "Descargar PDF"
        WebElement btnDescargar = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Descargar PDF')]")
        ));
        btnDescargar.click();
        pausa(3000);

        System.out.println("✅ Test E2E completo: compra finalizada y recibo generado.");
    }

    @After
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
