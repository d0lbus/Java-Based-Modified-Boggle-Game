package Java.Utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WordValidator {
    private Set<String> validWords;

    public WordValidator(String wordFilePath) {
        validWords = new HashSet<>();
        loadWords(wordFilePath);
    }

    private void loadWords(String wordFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(wordFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                validWords.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error reading word file: " + e.getMessage());
        }
    }

    public boolean isWordValid(String word) {
        return validWords.contains(word.toLowerCase());
    }
}

