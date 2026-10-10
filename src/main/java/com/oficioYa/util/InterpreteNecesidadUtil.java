package com.oficioya.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * RF-11: convierte una necesidad en lenguaje natural ("alguien que arregle una gotera")
 * en términos de búsqueda ("plom"). Sin IA: normaliza, quita palabras vacías, recorta a una
 * raíz de 4 letras y expande sinónimos de problemas comunes hacia el oficio que los resuelve.
 */
public final class InterpreteNecesidadUtil {

    private static final int LARGO_RAIZ = 4;
    private static final int MAX_TERMINOS = 10;

    private static final Set<String> PALABRAS_VACIAS = Set.of(
            "alguien", "que", "quien", "necesito", "busco", "quiero", "para", "una", "uno", "unos", "unas",
            "el", "la", "los", "las", "del", "de", "en", "mi", "mis", "con", "por", "sin", "hay", "hacer",
            "arregle", "arreglar", "arreglo", "puede", "pueda", "favor", "urgente", "ayuda", "algun", "alguna",
            "casa", "mas", "muy", "como", "sea", "esta", "este", "tengo", "tiene", "hoy", "ahora");

    /** raíz del problema -> raíz del oficio que lo atiende. */
    private static final Map<String, String> SINONIMOS = Map.ofEntries(
            // Plomería
            Map.entry("gote", "plom"), Map.entry("fuga", "plom"), Map.entry("tube", "plom"),
            Map.entry("grif", "plom"), Map.entry("inod", "plom"), Map.entry("sani", "plom"),
            Map.entry("dest", "plom"), Map.entry("cane", "plom"),
            // Electricidad
            Map.entry("cabl", "elec"), Map.entry("ench", "elec"), Map.entry("toma", "elec"),
            Map.entry("bomb", "elec"), Map.entry("brea", "elec"), Map.entry("luz", "elec"),
            // Pintura
            Map.entry("pare", "pint"),
            // Cerrajería y carpintería
            Map.entry("cerr", "cerr"), Map.entry("chap", "cerr"), Map.entry("mueb", "carp"),
            Map.entry("made", "carp"),
            // Jardinería
            Map.entry("past", "jard"), Map.entry("plan", "jard"), Map.entry("poda", "jard"),
            // Mecánica
            Map.entry("vehi", "meca"), Map.entry("carr", "meca"), Map.entry("moto", "meca"),
            // Mascotas
            Map.entry("perr", "masc"), Map.entry("gato", "masc"),
            // Clases particulares
            Map.entry("clas", "prof"), Map.entry("tare", "prof"), Map.entry("mate", "prof"),
            // Costura
            Map.entry("ropa", "cost"), Map.entry("dobl", "cost"));

    private InterpreteNecesidadUtil() {
        // Clase utilitaria, no instanciable
    }

    /** @return términos de búsqueda sin repetir y sin acentos; lista vacía si el texto es nulo o no aporta nada. */
    public static List<String> extraerTerminos(String texto) {
        if (texto == null || texto.isBlank()) {
            return List.of();
        }
        String normalizado = normalizar(texto);
        Set<String> terminos = new LinkedHashSet<>();
        for (String token : normalizado.split("\\s+")) {
            if (token.length() < 3 || PALABRAS_VACIAS.contains(token)) {
                continue;
            }
            String raiz = token.length() > LARGO_RAIZ ? token.substring(0, LARGO_RAIZ) : token;
            String sinonimo = SINONIMOS.get(raiz);
            terminos.add(sinonimo != null ? sinonimo : raiz);
            if (terminos.size() >= MAX_TERMINOS) {
                break;
            }
        }
        return new ArrayList<>(terminos);
    }

    /** Minúsculas, sin acentos y solo letras y dígitos separados por espacios. */
    public static String normalizar(String texto) {
        String sinAcentos = Normalizer.normalize(texto.toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinAcentos.replaceAll("[^a-z0-9]+", " ").trim();
    }
}
