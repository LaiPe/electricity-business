package com.humanbooster.electricity_business.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ModelUtils {
    public static <T> void copierChamps(T source, T destination) {
        // Raise exception if entities are null
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Les objets source et destination ne peuvent pas être null");
        }

        // Raise exception if objects aren't same type
        if (!source.getClass().equals(destination.getClass())) {
            throw new IllegalArgumentException("Les objets doivent être du même type exact");
        }

        try {
            // class' fields list
            Field[] champs = source.getClass().getDeclaredFields();

            // for each field of the class
            for (Field champ : champs) {
                // if field is static or final -> ignore field
                if (Modifier.isStatic(champ.getModifiers()) ||
                        Modifier.isFinal(champ.getModifiers())) {
                    continue;
                }

                // if field is the id -> ignore field
                if (champ.getName().equals("id")) {
                    continue;
                }

                champ.setAccessible(true);
                Object valeur = champ.get(source);

                // Ignore fields of value null
                if (valeur != null || champ.getType().isPrimitive()) {
                    champ.set(destination,valeur);
                }
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Erreur lors de l'accès aux champs par réflexion", e);
        }
    }
}
