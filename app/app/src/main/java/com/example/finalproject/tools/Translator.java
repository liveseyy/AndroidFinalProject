package com.example.finalproject.tools;

import java.util.HashMap;

public class Translator {
    HashMap<String, String> vocabulary = new HashMap<String, String>() {{
        put("dog", "собака");
        put("cow", "корова");
        put("camel", "верблюд");
        put("cat", "кошка");
        put("cock", "петух");
        put("chicken", "курица");
        put("crocodile", "крокодил");
        put("crow", "ворона");
        put("deer", "олень");
        put("dolphin", "дельфин");
        put("donkey", "осёл");
        put("frog", "лягушка");
        put("goat", "коза");
        put("goose", "гусь");
        put("hamster", "хомяк");
        put("horse", "лошадь");
        put("lion", "лев");
        put("monkey", "обезьяна");
        put("owl", "сова");
        put("pig", "свинья");
        put("ram", "баран");
    }};

    public String translateToRussian(String s) {
        return vocabulary.get(s);
    }
}
