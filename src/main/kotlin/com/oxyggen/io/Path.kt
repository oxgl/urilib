package com.oxyggen.io

class Path(val pathString: String, val delimiter: Char = '/') {

    private fun normalizePathString(pathString: String, delimiter: Char = '/'): String {
        val parts = pathString.split(delimiter)

        var normalizedParts = mutableListOf<String>()

        for (i in parts.indices)
            when (val part = parts[i]) {
                "" -> if (normalizedParts.size == 0 || !normalizedParts.last().isBlank()) normalizedParts.add(part)
                "." -> if (i == 0) normalizedParts.add(part) else if (i == parts.size - 1) normalizedParts.add("")
                ".." -> if ((normalizedParts.size > 1) || (normalizedParts.size == 1 && !normalizedParts[0].isBlank()))
                    normalizedParts.removeAt(normalizedParts.size - 1)
                else -> normalizedParts.add((part))
            }

        return normalizedParts.joinToString(delimiter.toString())
    }

    val normalized: String by lazy {
        normalizePathString(pathString, delimiter)
    }

    val file: String by lazy {
        if (normalized.endsWith(delimiter)) {
            ""
        } else {
            normalized.substringAfterLast(delimiter)

        }
    }

    val directory: String by lazy {
        if (pathString.endsWith(delimiter)) {
            pathString
        } else {
            pathString.substringBeforeLast(delimiter) + delimiter
        }
    }

    val isAbsolute: Boolean by lazy {
        normalized.startsWith(delimiter)
    }

    fun resolve(anotherPath: Path): Path =
            if (anotherPath.isAbsolute || anotherPath.delimiter != delimiter) {
                anotherPath
            } else {
                Path(normalizePathString(directory + anotherPath.pathString))
            }

    fun resolve(anotherPathString: String) = resolve(Path(anotherPathString, delimiter = this.delimiter))

}