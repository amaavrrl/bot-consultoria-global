package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.Duration;

@SpringBootApplication
public class HolaSpringApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HolaSpringApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Configuración de ChromeDriver usando ClassPathResource para usar ruta relativa e independizar del sistema donde se corra
        String chromedriverPath;
        try {
            chromedriverPath = new ClassPathResource("chromedriver.exe").getFile().getAbsolutePath();
            System.setProperty("webdriver.chrome.driver", chromedriverPath);
        } catch (IOException e) {
            System.out.println("No se pudo encontrar el chromedriver: " + e.getMessage());
            return;
        }

        //Inicialización de Google Chrome con ChromeDriver
        WebDriver driver = new ChromeDriver();
        // Creación del objeto "wait" para esperar cargas de la pàgina hasta 10 segundos.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
        	// Ingreso a la web de Consultoría Global
            driver.get("https://www.consultoriaglobal.com.ar/cgweb/");
            System.out.println("Página cargada " + driver.getTitle());

            // Ingreso a la sección de Contacto
            WebElement contacto = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menu-item-1364")));
            contacto.click();
            System.out.println("Ingreso a la página de contacto");

            // Se rellena el campo nombre
            WebElement nombre = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("your-name")));
            nombre.sendKeys("Juan Pérez");
            System.out.println("Nombre cargado");

            // Se rellena el campo mail con testo que no es una dirección
            WebElement mail = driver.findElement(By.name("your-email"));
            mail.sendKeys("mail.erroneo");
            System.out.println("Mail cargado");

            // Se rellena el campo asunto
            WebElement asunto = driver.findElement(By.name("your-subject"));
            asunto.sendKeys("Solicitud de información");
            System.out.println("Asunto cargado");

            // Se rellena el campo mensaje
            WebElement mensaje = driver.findElement(By.name("your-message"));
            mensaje.sendKeys("Esto es un mensaje de prueba");
            System.out.println("Mensaje cargado");

            // Se presiona el botòn enviar
            WebElement enviar = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("wpcf7-submit")));
            enviar.click();
            System.out.println("Formulario enviado");

            // Se espera a que la página actualice luego de presionar el botón enviar
            // y se espera hasta que se encuentre al menos un elemento de error con clase
            // "wpcf7-not-valid-tip" según el código HTML de la web
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("wpcf7-not-valid-tip")));

            // Se busca el error que aparece en el campo de email y se imprime en consola.
            WebElement emailErrorContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".wpcf7-form-control-wrap.your-email")));
            WebElement emailError = emailErrorContainer.findElement(By.className("wpcf7-not-valid-tip"));
            System.out.println("Error de email: " + emailError.getText());

        } 
        // En caso de que ocurra algún error se dispara la siguiente excepción con impresión en consola
        catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        } finally {
            // Se cierra Google Chrome
        	driver.quit();
        }
    }
}
