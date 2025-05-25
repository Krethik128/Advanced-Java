package com.gevernova.feedbackaggregator.services;

public class BasicSentimentAnalyzer implements SentimentAnalyzer {
    @Override
    public String analyzeSentiment(String comment) {
        String lower = comment.toLowerCase();
        if (lower.contains("bad") || lower.contains("poor")) {
            return "Negative";
        } else if (lower.contains("good") || lower.contains("excellent")) {
            return "Positive";
        }
        return "Neutral";
    }
}

