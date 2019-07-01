package com.oxyggen.io

open class Path protected constructor(
        val device: String,
        val folder: List<String>,
        val file: String,
        val isAbsolute: Boolean,
        val pathSeparator: String = "/") {

    companion object {
        const val PATH_SEPARATOR = "/"
        const val FILE_EXTENSION_SEPARATOR = "."
        const val DIRECTORY_CURRENT = "."
        const val DIRECTORY_PARENT = ".."

        /**
         * Returns a new Path object for given [path].
         * Default [pathSeparator] is slash used in linux, URL, etc...
         * Empty path is same as root path: "/". Relative path to current directory should be "."
         * @return the new path object
         */
        fun parse(path: String, pathSeparator: String = PATH_SEPARATOR): Path {
            var device = ""
            var file = ""
            var isAbsolute = false

            val allParts = path.split(pathSeparator).toMutableList()

            if (allParts.size > 0) {
                // Get the first element:
                //  if it's empty -> path begins with pathSeparator (/dev/sda)
                //  if it's a device name -> windows absolute path (C:\temp)
                val first = allParts.first()

                // If the path begins with device "C:" then remember device
                device = if ("^[a-zA-Z]:".toRegex().matches(first)) first else ""

                // Check whether path is absolute (begins with device or blank)
                // If it's absolute, remove the first part ("C:" or blank - folder before first slash)
                isAbsolute = device.isNotBlank() || first.isBlank()
                if (isAbsolute) allParts.removeAt(0)
            }

            if (allParts.size > 0) {
                // Get the last element
                //  it's a directory if ends with pathSeparator (/dev/)
                //  it's a directory if it ends with "parent" (/dev/..)
                val last = allParts.last()

                val containsFile = last != DIRECTORY_CURRENT && last != DIRECTORY_PARENT && last != ""

                file = if (containsFile) last else ""

                if (containsFile || last.isBlank()) {
                    allParts.removeAt(allParts.size - 1)
                }
            }

            return Path(
                    device = device,
                    folder = allParts,
                    file = file,
                    isAbsolute = isAbsolute,
                    pathSeparator = pathSeparator)
        }
    }

    private fun determineNormalizedFolders(folders: List<String>): List<String> {
        val normalizedFolders = mutableListOf<String>()

        for (i in folders.indices)
            when (val part = folders[i]) {
                "" -> if (normalizedFolders.size == 0 || !normalizedFolders.last().isBlank()) normalizedFolders.add(part)
                DIRECTORY_CURRENT -> if (i == 0) normalizedFolders.add(part) /*else if (i == folders.size - 1) normalizedFolders.add("")*/
                DIRECTORY_PARENT -> if ((normalizedFolders.size > 1) || (normalizedFolders.size == 1 && normalizedFolders[0] != DIRECTORY_CURRENT))
                    normalizedFolders.removeAt(normalizedFolders.size - 1)
                else -> normalizedFolders.add((part))
            }

        return normalizedFolders
    }

    open val fileName by lazy {
        if (file.contains(FILE_EXTENSION_SEPARATOR)) file.substringBeforeLast(FILE_EXTENSION_SEPARATOR) else file
    }

    open val fileExtension by lazy {
        if (file.contains(FILE_EXTENSION_SEPARATOR)) file.substringAfterLast(FILE_EXTENSION_SEPARATOR) else ""
    }

    open val directory by lazy {
        (if (isAbsolute) pathSeparator else "") + folder.joinToString(pathSeparator) + (if (folder.isNotEmpty()) pathSeparator else "")
    }

    open val full by lazy {
        device + directory + file
    }

    open val normalized by lazy {
        if (this is NormalizedPath) this else NormalizedPath(device, determineNormalizedFolders(folder), file, isAbsolute, pathSeparator)
    }

    open fun resolve(anotherPath: Path): Path =
            if (anotherPath.isAbsolute || anotherPath.pathSeparator != pathSeparator) {
                anotherPath
            } else {
                Path(
                        device = this.device,
                        folder = this.folder + anotherPath.folder,
                        file = anotherPath.file,
                        isAbsolute = this.isAbsolute,
                        pathSeparator = this.pathSeparator)
            }

    open fun resolve(anotherPath: String) = resolve(parse(anotherPath))

    open fun resolveNormalized(anotherPath: Path): NormalizedPath = resolve(anotherPath).normalized

    open fun resolveNormalized(anotherPath: String) = resolveNormalized(parse(anotherPath))

    private val fullHashCode: Int by lazy { full.hashCode() }

    override fun hashCode(): Int = fullHashCode

    override fun equals(other: Any?): Boolean =
            if (other is Path) {
                other.hashCode() == this.hashCode() && other.full == this.full
            } else {
                false
            }

}