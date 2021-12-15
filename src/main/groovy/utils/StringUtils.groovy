package utils

class StringUtils {

    static final String COLON = ":"
    static final String HYPHEN = "-"

    private static final String UNDERLINE = "_"


    static String snakeCaseToCamelCase(String input) {
        StringBuilder builder = new StringBuilder()
        List<String> words = input.split(UNDERLINE)
        builder.append(words.first().toLowerCase())
        for (int i = 1; i < words.size(); i++) {
            builder.append(words[i].substring(0, 1).toUpperCase())
            builder.append(words[i].substring(1).toLowerCase())
        }

        return builder.toString()
    }
}
