package com.example.navalfight;


import androidx.annotation.NonNull;

public enum AIDifficulty {
    Ease("Лёгкий"),
    Normal("Нормальный"),
    Master("Стратег"),
    Cheating("Нечестный");

    public String getTitle() {
        return title;
    }

    private final String title;

    AIDifficulty(String title) {
        this.title = title;
    }

    public static AIDifficulty fromTitle(String title){
        switch (title){
            case "Лёгкий":
                return Ease;
            case "Нормальный":
                return Normal;
            case "Стратег":
                return Master;
            case "Нечестный":
                return Cheating;
            default:
                throw new IllegalArgumentException("Неизвестный тип ИИ: " + title);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "AI{" + title +  '}';
    }
}
