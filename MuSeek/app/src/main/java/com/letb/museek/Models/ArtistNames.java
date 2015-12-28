package com.letb.museek.Models;

import java.util.Random;

/**
 * Created by eugene on 28.12.15.
 */
public class ArtistNames {
    private static final Random generator = new Random();
    private static final String[] names = {
            "New Order",
            "Adele",
            "Dead Can Dance",
            "Pink Floyd",
            "Portishead",
            "Radiohead",
            "UNKLE",
            "Glen Porter",
            "Angelo Badalamenti",
            "I Are Droid",
            "The Doors",
            "Slowdive",
            "The Killers",
            "Piano Magic",
            "Kasabian",
            "5’nizza",
            "A-ha",
            "The Velvet Underground"

    };

    public static String getRandomName() {
        return names[generator.nextInt(names.length)];
    }
}
