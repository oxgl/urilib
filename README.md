# UriLib by Oxyggen
Path and URI (URL) parsing library for Kotlin by Oxyggen. 
## Paths
The path parsing part is (probably) finished. 
To create a Path object call the Path.parse method:
```
val p = Path.parse("/first/second/third/../fourth/")
```  
 
You can set an optional parameter `pathSeparator`. The default value is `"/"` (Linux, URL, etc...). 
Set value `"\\"` if you want to parse Windows style paths:
```
val w = Path.parse("C:\\temp\\abc.txt", pathSeparator = "\\")
```

Because it is designed to parse URL paths few simple few simple rules were introduced:
1) directory is the substring before last separator and file is the substring after 
last separator
2) path `""` is the same as `"/"`
3) file name and extension separator is `"."` 

I had to introduce rules 1) and 3) because these are not real or local paths, so it's 
not possible to check whether given path points to a file or a directory. 
Always add a separator at the end, if the path points to directory! So `/dev/etc/` is directory
but `/dev/etc` is file.
 
### Examples
#### Parsing
```
val p = Path.parse("/first/second/third/../fourth/myfile.html")
```

| property          | value                                         |
|-------------------|-----------------------------------------------|
| p.complete        | "/first/second/third/../fourth/myfile.html"   |
| p.file            | "myfile.html"                                 |
| p.fileName        | "myfile"                                      |
| p.fileExtension   | "html"                                        |
| p.directory       | "/first/second/third/../fourth/"              |

#### Normalization
Let's normalize this path and check the values:
```
val n = p.normalized
```

| property          | value                                         |
|-------------------|-----------------------------------------------|
| n.complete        | **"/first/second/fourth/myfile.html"**        |
| n.file            | "myfile.html"                                 |
| n.fileName        | "myfile"                                      |
| n.fileExtension   | "html"                                        |
| n.directory       | **"/first/second/fourth/"**                   |

Normalized path is a subclass of `Path`, so it's easy to check whether path object is normalized:
```
if (p is NormalizedPath) ...
``` 
This does not mean that Path object can't contain normalized path, but you can be
sure that NormalizedPath object **must** contain normalized path.

#### Resolving relative paths
You can also resolve relative paths. Let's create relative `r` path and 
resolve it to absolute `a` using the original path `p`. Check also the normalized
values from `a` (`a.normalized`):
```
val r = Path.parse("../anotherfile.html")
val a = p.resolve(r)
```
Result:

| property        | a.(property)                                          | a.normalized.(property)
|-----------------|-------------------------------------------------------|-------------------------------------------------------|                                                                           
| complete        | "/first/second/third/../fourth/../anotherfile.php"    | "/first/second/anotherfile.php"                       |
| file            | "anotherfile.php"                                     | "anotherfile.php"                                     |
| fileName        | "anotherfile"                                         | "anotherfile"                                         |
| fileExtension   | "php"                                                 | "php"                                                 |
| directory       | "/first/second/third/../fourth/../"                   | "/first/second/"                                      |

## URI and URL...



Library was primarily created for **c4k** web crawling framework, but it's a standalone library...


... and I almost forgot: I'm new to Kotlin :) 