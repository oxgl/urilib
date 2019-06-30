package com.oxyggen.io

open class Path protected constructor(
        val device: String,
        val folder: List<String>,
        val file: String,
        val isAbsolute: Boolean,
        val delimiter: String = "/") {

    companion object {
        const val DIRECTORY_CURRENT = "."
        const val DIRECTORY_PARENT = ".."
        const val FILE_EXTENSION_DELIMITER = "."

        fun parse(path: String, delimiter: String = "/"): Path {
            val allParts = path.split(delimiter)

            var folderIndexFrom = 0
            var folderIndexTo = allParts.size - 1

            // Get the first element:
            //  if it's empty -> path begins with delimiter (/dev/sda)
            //  if it's a device name -> windows absolute path (C:\temp)
            val first = allParts.first()

            val device = if ("^[a-zA-Z]:".toRegex().matches(first)) first else ""
            if (device.isNotBlank()) folderIndexFrom++

            val isAbsolute = device.isNotBlank() || first.isBlank()
            if (isAbsolute) folderIndexFrom++


            // Get the last element
            //  it's a directory if ends with delimiter (/dev/)
            //  it's a directory if it ends with "parent" (/dev/..)
            val last = allParts.last()

            val containsFile = last != DIRECTORY_CURRENT && last != DIRECTORY_PARENT && last != ""

            val file = if (containsFile) last else ""

            //if (containsFile) folderIndexTo--

            return Path(
                    device = device,
                    folder = allParts.subList(folderIndexFrom, folderIndexTo),
                    file = file,
                    isAbsolute = isAbsolute,
                    delimiter = delimiter)
        }
    }

    private fun determineNormalizedFolders(folders: List<String>, delimiter: String = "/"): List<String> {
        var normalizedFolders = mutableListOf<String>()

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

    open val fileName = if (file.contains(FILE_EXTENSION_DELIMITER)) file.substringBeforeLast(FILE_EXTENSION_DELIMITER) else file

    open val fileExtension = if (file.contains(FILE_EXTENSION_DELIMITER)) file.substringAfterLast(FILE_EXTENSION_DELIMITER) else ""

    open val directory = (if (isAbsolute) delimiter else "") + folder.joinToString(delimiter) + (if (folder.isNotEmpty()) delimiter else "")

    open val normalizedPath = if (this is NormalizedPath) this else NormalizedPath(device, determineNormalizedFolders(folder, delimiter), file, isAbsolute, delimiter)

    open val full = device + directory + file

    fun resolve(anotherPath: Path): Path =
            if (anotherPath.isAbsolute || anotherPath.delimiter != delimiter) {
                anotherPath
            } else {
                Path(
                        device = this.device,
                        folder = this.folder + anotherPath.folder,
                        file = anotherPath.file,
                        isAbsolute = this.isAbsolute,
                        delimiter = this.delimiter)
            }

    fun resolve(anotherPath: String) = resolve(parse(anotherPath))

    fun resolveNormalized(anotherPath: Path): NormalizedPath = resolve(anotherPath).normalizedPath

    fun resolveNormalized(anotherPath: String) = resolveNormalized(parse(anotherPath))


/*
    val path = parts.joinToString(delimiter.toString())

    val normalizedParts by lazy { determineNormalizedParts(parts, delimiter) }

    val normalized = normalizedParts.joinToString(delimiter.toString())

    val filename = normalizedParts.last()

    val hasFilename = filename.isNotBlank()

    val directory = if (pathString.endsWith(delimiter)) {
        pathString
    } else {
        pathString.substringBeforeLast(delimiter) + delimiter
    }

    val isAbsolute = pathString.startsWith(delimiter)

    val isRelative = !isAbsolute

    fun resolve(anotherPath: Path): Path =
            if (anotherPath.isAbsolute || anotherPath.delimiter != delimiter) {
                anotherPath
            } else {
                Path(pathParts = determineNormalizedParts(this.parts + anotherPath.parts), delimiter = delimiter)
            }

    fun resolve(anotherPathString: String) = resolve(Path(anotherPathString, delimiter = this.delimiter))
    */


}