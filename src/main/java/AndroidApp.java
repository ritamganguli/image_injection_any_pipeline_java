import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.util.Base64;
import java.util.List;

public class AndroidApp {

    String userName = "userName"; //Add username here
    String accessKey = "accessKey"; //Add accessKey here
    String app_id = "lt://APP10160381481702492104610988";
    String grid_url = System.getenv("LT_GRID_URL") == null ? "mobile-hub.lambdatest.com" : System.getenv("LT_GRID_URL");

    AppiumDriver driver;

    @Test
    @org.testng.annotations.Parameters(value = {"device", "version", "platform"})
    public void AndroidApp1(String device, String version, String platform) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost uploadFile = new HttpPost("https://mobile-mgm.lambdatest.com/mfs/v1.0/media/upload");
            uploadFile.addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((userName + ":" + accessKey).getBytes()));

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("type", "image", ContentType.TEXT_PLAIN);
            builder.addTextBody("custom_id", "SampleImage", ContentType.TEXT_PLAIN);
            builder.addBinaryBody("media_file", new File("download.jpg"), ContentType.APPLICATION_OCTET_STREAM, "ritam.jpg");

            uploadFile.setEntity(builder.build());

            HttpResponse response = client.execute(uploadFile);
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println("Raw JSON Response: " + responseString);
            JSONObject jsonResponse = new JSONObject(responseString);
            String mediaUrl = jsonResponse.getString("media_url");
            System.out.println("The media url is: " + mediaUrl);
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("deviceName", "OnePlus 9");
            capabilities.setCapability("platformVersion", "11");
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("isRealMobile", true);
            capabilities.setCapability("enableImageInjection", true);
            capabilities.setCapability("media",mediaUrl); // Replace with the
            capabilities.setCapability("app", "lt://APP10160381481702492104610988"); // Enter your app URL
            capabilities.setCapability("deviceOrientation", "PORTRAIT");
            capabilities.setCapability("build", "Java Vanilla - Android");
            capabilities.setCapability("name", "Sample Test Java");
            capabilities.setCapability("console", true);
            capabilities.setCapability("network", false);
            capabilities.setCapability("visual", true);
            capabilities.setCapability("devicelog", true);

            driver = new AppiumDriver(
                    new URL("https://" + userName + ":" + accessKey + "@mobile-hub.lambdatest.com/wd/hub"),
                    capabilities);

            WebDriverWait wait = new WebDriverWait(driver, 30); // Define the WebDriverWait object
            driver.findElementById("com.android.permissioncontroller:id/permission_allow_foreground_only_button").click();
            Thread.sleep(5000);
            driver.findElementById("com.android.permissioncontroller:id/permission_allow_button").click();
            Thread.sleep(5000);
            driver.findElementById("com.poc.sample:id/data_content").click();
            Thread.sleep(5000);
            driver.executeScript("lambda-image-injection=" + mediaUrl);
            Thread.sleep(7000);
            driver.findElementById("com.poc.sample:id/btn_capture").click();
            Thread.sleep(7000);
            driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.RelativeLayout[11]/android.widget.RelativeLayout/android.widget.FrameLayout[2]/android.widget.ImageView").click();
            Thread.sleep(7000);
            driver.findElementById("com.oneplus.camera:id/commit").click();
            Thread.sleep(20000);



            // you want to inject
            // driver.executeScript("lambda-image-injection=" + mediaUrl);

            // Close the driver after the test execution is complete
            driver.quit();
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
