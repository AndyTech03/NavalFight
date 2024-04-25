package com.example.navalfight;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public enum AIDifficulty {

    Ease(R.string.easeAI),
    Normal(R.string.normalAI),
    Master(R.string.hardAI),
    Cheating(R.string.cheatingAI);


    private final @StringRes int id;

    AIDifficulty(@StringRes int id) {
        this.id = id;
    }

    public @StringRes int getID() {
        return id;
    }

    public static AIDifficulty fromID(@StringRes int id) {
        if (id == R.string.easeAI)
            return Ease;
        if (id == R.string.normalAI)
            return Normal;
        if (id == R.string.hardAI)
            return Master;
        if (id == R.string.cheatingAI)
            return Cheating;
        throw new IllegalArgumentException("Неизвестный тип ИИ: " + id);

    }

    @NonNull
    @Override
    public String toString() {
        return "AI{" + id + '}';
    }
}
