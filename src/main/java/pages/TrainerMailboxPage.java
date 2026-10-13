package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * TrainerMailboxPage POM for Trainer internal messages and assignment notifications (/trainer/mail).
 */
public class TrainerMailboxPage extends BasePage {

    private final By pageTitle = By.cssSelector(".page-title");
    private final By messageCards = By.cssSelector(".content-card .p-3.border-bottom");
    private final By markAllReadBtn = By.xpath("//button[contains(text(), 'Mark All as Read')]");
    private final By emptyMailboxMsg = By.xpath("//div[contains(@class, 'text-center')]//h6[contains(text(), 'Your mailbox is empty')]");

    public TrainerMailboxPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return getCurrentUrl().contains("/mail") && isElementDisplayed(pageTitle);
    }

    public int getMessagesCount() {
        if (isElementDisplayed(emptyMailboxMsg)) {
            return 0;
        }
        return findElements(messageCards).size();
    }

    public List<String> getAllMessageTexts() {
        List<String> texts = new ArrayList<>();
        List<WebElement> cards = findElements(messageCards);
        for (WebElement c : cards) {
            texts.add(c.getText().trim());
        }
        return texts;
    }

    public boolean hasMessageContaining(String snippet) {
        for (String text : getAllMessageTexts()) {
            if (text.toLowerCase().contains(snippet.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void markAllAsRead() {
        if (isElementDisplayed(markAllReadBtn)) {
            click(markAllReadBtn);
        }
    }
}
