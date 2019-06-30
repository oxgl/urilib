package com.oxyggen.io

class NormalizedPath internal constructor(device: String, folder: List<String>, file: String, isAbsolute: Boolean, delimiter: String) : Path(device, folder, file, isAbsolute, delimiter)
