/*
 * Copyright © Wynntils 2022.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.features.user.tooltips;

import com.wynntils.core.components.Model;
import com.wynntils.core.components.Models;
import com.wynntils.core.config.Config;
import com.wynntils.core.features.UserFeature;
import com.wynntils.core.features.properties.FeatureCategory;
import com.wynntils.core.features.properties.FeatureInfo;
import com.wynntils.core.features.properties.FeatureInfo.Stability;
import java.util.List;

@FeatureInfo(stability = Stability.STABLE, category = FeatureCategory.TOOLTIPS)
public class ItemStatInfoFeature extends UserFeature {
    public static ItemStatInfoFeature INSTANCE;

    @Config
    public boolean showStars = true;

    @Config
    public boolean colorLerp = true;

    @Config
    public int decimalPlaces = 1;

    @Config
    public boolean perfect = true;

    @Config
    public boolean defective = true;

    @Config
    public float obfuscationChanceStart = 0.08f;

    @Config
    public float obfuscationChanceEnd = 0.04f;

    @Config
    public boolean reorderIdentifications = true;

    @Config
    public boolean groupIdentifications = true;

    @Override
    public List<Model> getModelDependencies() {
        return List.of(Models.GearItemStack);
    }
}
