/*
 * Copyright © Wynntils 2022.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.mc.utils;

import com.wynntils.mc.objects.Location;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LocationUtils {
    private static final Pattern COORDINATE_PATTERN =
            Pattern.compile("(?<x>[-+]?\\d+)([^0-9+-]{1,5}(?<y>[-+]?\\d+))?[^0-9+-]{1,5}(?<z>[-+]?\\d+)");

    private static final Pattern STRICT_COORDINATE_PATTERN =
            Pattern.compile("([-+]?\\d{1,5})([,\\s]{1,2}([-+]?\\d{1,4}))?[,\\s]{1,2}([-+]?\\d{1,5})");

    public static Optional<Location> parseFromString(String locString) {
        Matcher matcher = COORDINATE_PATTERN.matcher(locString);

        if (matcher.matches()) {
            int x = Integer.parseInt(matcher.group("x"));
            String yString = matcher.group("y");
            int y = yString != null ? Integer.parseInt(yString) : 0;
            int z = Integer.parseInt(matcher.group("z"));

            return Optional.of(new Location(x, y, z));
        }

        return Optional.empty();
    }

    public static Matcher strictCoordinateMatcher(String string) {
        return STRICT_COORDINATE_PATTERN.matcher(string);
    }

    public static void shareLocation(String target) {
        String locationString = "My location is at [" + (int) McUtils.player().position().x + ", "
                + (int) McUtils.player().position().y + ", "
                + (int) McUtils.player().position().z + "]";

        LocationUtils.sendShareMessage(target, locationString);
    }

    public static void shareCompass(String target, Location compass) {
        String locationString =
                "My compass is at [" + (int) compass.x + ", " + (int) compass.y + ", " + (int) compass.z + "]";

        LocationUtils.sendShareMessage(target, locationString);
    }

    private static void sendShareMessage(String target, String locationString) {
        if (target.equals("guild")) {
            McUtils.sendCommand("g " + locationString);
        } else if (target.equals("party")) {
            McUtils.sendCommand("p " + locationString);
        } else {
            McUtils.sendCommand("msg " + target + " " + locationString);
        }
    }
}
