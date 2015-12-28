package com.letb.museek.Models;

import java.util.Random;

/**
 * Created by eugene on 28.12.15.
 */
public class ArtistNames {
    private static final Random generator = new Random();
    private static final String[] names = {
            "Justin Bieber",
            "Adele",
            "Red Hot Chili Peppers",
            "Pink Floyd",
            "Kanye West",
            "David Bowie",
            "Imagine Dragons"
    };

    public static String getRandomName() {
        return names[generator.nextInt(names.length)];
    }
}
