/*
 * Copyright © Wynntils 2022.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.core.config.upfixers.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.wynntils.core.config.upfixers.ConfigUpfixer;

public class CustomCommandKeybindSlashStartUpfixer implements ConfigUpfixer {
    private static final String CUSTOM_COMMAND_OBJECT_NAME = "customCommandKeybindsFeature.keybindCommand";

    @Override
    public boolean apply(JsonObject configObject) {
        // There are 6 custom commands in the config, and they all start the same way.
        for (int i = 1; i <= 6; i++) {
            String name = CUSTOM_COMMAND_OBJECT_NAME + i;

            if (!configObject.has(name)) continue;

            JsonPrimitive configValue = configObject.getAsJsonPrimitive(name);

            if (!configValue.isString()) continue;

            configObject.addProperty(name, configValue.getAsString().replaceFirst("/", ""));
        }

        return true;
    }

    @Override
    public String getUpfixerName() {
        return "custom_command_keybind_slash_start_remove";
    }
}
