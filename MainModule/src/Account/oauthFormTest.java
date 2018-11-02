package Account;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import twitter4j.TwitterException;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class oauthFormTest {

    @BeforeClass
    public static void initToolkit()
            throws InterruptedException
    {
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            new JFXPanel(); // initializes JavaFX environment
            latch.countDown();
        });

        // That's a pretty reasonable delay... Right?
        if (!latch.await(5L, TimeUnit.SECONDS))
            throw new ExceptionInInitializerError();
    }

    @Test
    public void getScene() {
        try {
            oauthForm oa = new oauthForm(null,null,null);
            Scene s = oa.getScene();
            Assert.assertNotNull(s);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void createPane() {
    }

    @Test
    public void addUI() {
    }

    @Test
    public void launchOauth() {
    }

    @Test
    public void enterCode() {
    }

    @Test
    public void updateTokens() {
    }
}