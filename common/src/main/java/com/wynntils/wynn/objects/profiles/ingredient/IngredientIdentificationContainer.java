/*
 * Copyright © Wynntils 2022.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.wynn.objects.profiles.ingredient;

import com.wynntils.wynn.objects.profiles.item.IdentificationModifier;

public class IngredientIdentificationContainer {
    private final int minimum;
    private final int maximum;
    IdentificationModifier type;
    boolean isFixed;

    public IngredientIdentificationContainer(IdentificationModifier type, int minimum, int maximum) {
        this.type = type;
        this.minimum = minimum;
        this.maximum = maximum;
        this.isFixed = minimum == maximum;
    }

    public int getMax() {
        return maximum;
    }

    public int getMin() {
        return minimum;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public boolean hasConstantValue() {
        return isFixed || minimum == maximum;
    }

    public IdentificationModifier getType() {
        return type;
    }
}
