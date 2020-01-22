package com.oxyggen.matcher

class GlobMatcher(globPattern: String, ignoreCase: Boolean = true) : RegexMatcher(globToRegexPattern(globPattern), ignoreCase) {

    companion object {
        /**
         * Converts glob pattern to regular expression
         * Java source found on https://stackoverflow.com/questions/1247772/is-there-an-equivalent-of-java-util-regex-for-glob-type-patterns
         * created by Neil Traft, so I've converted to Kotlin code
         * @param globPattern the glob pattern
         * @return regular expression string
         */

        private fun globToRegexPattern(globPattern: String): String {
            var result = ""

            var index = 0
            val groupIndex = mutableListOf<Int>()
            val classIndex = mutableListOf<Int>()

            while (index < globPattern.length) {
                val ch = globPattern[index]

                result += when (ch) {
                    '\\' ->
                        if (++index >= globPattern.length) {  // If it's last character, not an escape char
                            ch
                        } else {
                            val nch = globPattern[index]
                            when (nch) {
                                ',' -> nch                 // Escape character not needed
                                'Q', 'E' -> "\\\\$nch"     // \Q \E are special delimiters in regex for quoting literals
                                else -> "\\$nch"           // Escape any other character
                            }
                        }

                    '*' ->
                        if (classIndex.isEmpty()) {
                            ".*"
                        } else {
                            "*"
                        }

                    '?' ->
                        if (classIndex.isEmpty()) {
                            "."
                        } else {
                            "?"
                        }

                    '[' -> {
                        classIndex.add(index)
                        "["
                    }

                    ']' -> {
                        classIndex.dropLast(1)
                        "]"
                    }

                    '{' -> {
                        groupIndex.add(index)
                        "("
                    }

                    '}' -> {
                        groupIndex.dropLast(1)
                        ")"
                    }

                    '.', '(', ')', '+', '|', '^', '$', '@', '%' ->
                        if (classIndex.isEmpty() || (classIndex.last() + 1 == index && ch == '^')) {
                            "\\$ch"
                        } else {
                            ch
                        }

                    '!' ->
                        if (classIndex.isNotEmpty() && classIndex.last() + 1 == index) {
                            "^"
                        } else {
                            "!"
                        }

                    ',' ->
                        if (groupIndex.isNotEmpty()) {
                            "|"
                        } else {
                            ","
                        }

                    else -> ch
                }

                index++
            }

            return result
        }
    }

}