import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.Random;

public class GittiGidiyor {
private WebDriver driver;
JavascriptExecutor js;
@Before
public void setUp(){
    System.setProperty("webdriver.chrome.driver" , "src/test/resources/drivers/chromedriver.exe");
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
}

@Test
public void Test() {
    String kullaniciAdi="deniz";
    String sifre="cerrah";
    String baseURL = "https://www.gittigidiyor.com/";

    //gittigidiyor açılır, anasayfa açıldı mı kontrol edilir
    driver.get(baseURL);
    Assert.assertEquals("Anasayfa açılmadı!", baseURL, driver.getCurrentUrl());

    //siteye login olunur, login kontrolü yapılır
    driver.findElement(By.id("L-UserNameField")).sendKeys(kullaniciAdi);
    driver.findElement(By.id("L-PasswordField")).sendKeys(sifre);
    driver.findElement(By.id("gg-login-enter")).click();
    Assert.assertEquals("Login yapılamadı!", baseURL, driver.getCurrentUrl());

    //arama çubuğuna "bilgisayar" yazılır ve aranır
    driver.findElement(By.className("sc-4995aq-0 sc-14oyvky-0 iYMTpq")).sendKeys("bilgisayar");
    driver.findElement(By.className("qjixn8-0 sc-1bydi5r-0 hKfdXF")).click();

    //arama sonuçları sayfasından 2.sayfa açılır, 2. sayfa olup olmadığı kontrol edilir
    driver.get(baseURL +"arama/?k=bilgisayar&sf=2");
    Assert.assertEquals("Arama sonuçlarından 2. sayfa açılamadı!" ,"2", driver.findElement(By.className("current")).getText());

    //rastgele ürün seçilir, seçilen ürünün sayfası açılır
    Random random = new Random();
    int randomPro = random.nextInt(49) + 1;
    driver.findElement(By.xpath("//div[@class='clearfix']/ul[@class='catalog-view clearfix products-container']/li["+randomPro+"]/a[@class='product-link']")).click();

    //ürün sepete eklenir, sayfadaki ürünün fiyatı bir değere atanır
    driver.findElement(By.id("add-to-basket")).click();
    String fiyat1= driver.findElement(By.id("sp-price-lowPrice")).getText();

    //sepete gidilir, sepetteki ürünün fiyatı bir değere atanır, ürün sayfasındaki ve sepetteki fiyat karşılaştırılır
    driver.get(baseURL +"sepetim");
    String fiyat2=driver.findElement(By.className("real-discounted-price")).getText();
    Assert.assertEquals("Sepetteki fiyat ürün sayfasındaki fiyata eşit değildir.", fiyat1,fiyat2);

    //ürün adedi arttırılarak 2 yapılır, ürün adedinin 2 olup olmadığı kontrol edilir
    new Select((driver.findElement(By.className("amount")))).selectByValue("2");
    Assert.assertEquals("Ürün adedi 2 değildir.", "2",driver.findElement(By.className("amount")).getText());

    //ürün sepetten silinir ve silinip silinmediği kontrol edilir
    driver.findElement(By.className("btn-delete btn-update-item hidden-m")).click();
    String sepetKontrol=driver.findElement(By.xpath("//*[@id='empty-cart-container']/div[1]/div[1]/div/div[2]/h2")).getText();
    Assert.assertEquals("Sepet boş değil!", "Sepetinizde ürün bulunmamaktadır.", sepetKontrol);
}

@After
public void tearDown(){
    driver.quit();
}

}
