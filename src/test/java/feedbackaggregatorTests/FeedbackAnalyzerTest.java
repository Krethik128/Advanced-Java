package feedbackaggregatorTests;

import static org.junit.jupiter.api.Assertions.*;

import com.gevernova.feedbackaggregator.model.Feedback;
import com.gevernova.feedbackaggregator.model.Product;
import com.gevernova.feedbackaggregator.services.BasicSentimentAnalyzer;
import com.gevernova.feedbackaggregator.services.FeedbackAnalyzer;
import com.gevernova.feedbackaggregator.services.SentimentAnalyzer;
import org.junit.jupiter.api.*;
import java.util.List;

public class FeedbackAnalyzerTest {
    private FeedbackAnalyzer analyzer;
    private Product productOne;
    private Product productTwo;

    @BeforeEach
    void setUp() {
        analyzer = new FeedbackAnalyzer();
        productOne = new Product("P101", "Smartphone");
        productTwo = new Product("P102", "Laptop");

        analyzer.addFeedback(productOne, new Feedback("Alice", 4, "Good phone"));
        analyzer.addFeedback(productOne, new Feedback("Bob", 2, "Bad battery"));
        analyzer.addFeedback(productTwo, new Feedback("Charlie", 5, "Excellent performance"));
    }

    @Test
    void testAverageRatingCalculation() {
        double average = analyzer.getAverageRating(productOne);
        assertEquals(3.0, average, 0.01);
    }

    @Test
    void testLowRatedProducts() {
        List<Product> lowRated = analyzer.getLowRatedProducts(3.5);
        assertTrue(lowRated.contains(productOne));
        assertFalse(lowRated.contains(productTwo));
    }

    @Test
    void testSentimentAnalyzerPositive() {
        SentimentAnalyzer sentimentAnalyzer = new BasicSentimentAnalyzer();
        String result = sentimentAnalyzer.analyzeSentiment("This is an excellent product");
        assertEquals("Positive", result);
    }

    @Test
    void testSentimentAnalyzerNegative() {
        SentimentAnalyzer sentimentAnalyzer = new BasicSentimentAnalyzer();
        String result = sentimentAnalyzer.analyzeSentiment("This product has bad quality");
        assertEquals("Negative", result);
    }

    @Test
    void testSentimentAnalyzerNeutral() {
        SentimentAnalyzer sentimentAnalyzer = new BasicSentimentAnalyzer();
        String result = sentimentAnalyzer.analyzeSentiment("This is a phone");
        assertEquals("Neutral", result);
    }
}
