package Account;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
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
            throws InterruptedException {
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
        oauthForm oa = new oauthForm();
        Scene s = oa.getScene();
        Assert.assertNotNull(s);

    }

    @Test
    public void addUI() throws TwitterException {
        GridPane gp1 = new GridPane();
        GridPane gp2 = new GridPane();
        oauthForm oa = new oauthForm(null, null, null);
        oa.addUI(gp1);
        Assert.assertNotSame(gp1, gp2);
    }
}

