# UriLib by Oxyggen

Path and URI parsing library for Kotlin. The path parsing part is (probably) finished. 
To create a Path object call the Path.parse method:

`val p = Path.parse("/first/second/third/../fourth/")`  
 
You can set an optional parameter `pathSeparator`. The default value is `"/"` (Linux, URL, etc...). 
Set value `"/"` if you want to parse Windows style paths. 

Because it is design to parse URL paths few simple rules are used:
* directory is the substring before last separator and file is the substring after 
last separator- I had to introduce this rule because it's not possible to check whether given 
path points to file or directory in case of URL
* path `""` is the same as `"/"`
* file name and extension separator is `"."` 
 
Few examples:

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

You can also resolve relative paths. Let's create relative path and resolve it (to absolute)
using the original path `p`
```
val r = Path.parse("../anotherfile.html")
val a = p.resolve(r)
```
Result:

| property          | value                                                 |
|-------------------|-------------------------------------------------------|
| a.complete        | "/first/second/third/../fourth/../anotherfile.php"    |
| a.file            | "anotherfile.php"                                     |
| a.fileName        | "anotherfile"                                         |
| a.fileExtension   | "php"                                                 |
| a.directory       | "/first/second/third/../fourth/../"                   |

Normalized:
```
val an = a.normalized
```
| property          | value                                                 |
|-------------------|-------------------------------------------------------|
| an.complete       | "/first/second/anotherfile.php"                       |
| an.file           | "anotherfile.php"                                     |
| an.fileName       | "anotherfile"                                         |
| an.fileExtension  | "php"                                                 |
| an.directory      | "/first/second/"                                      |


Library was primarily created for **c4k** web crawling framework, but it's a standalone library...


... and I almost forgot: I'm new to Kotlin :) 