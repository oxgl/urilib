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
| p.isAbsolute      | true                                          |
| p.device          | ""                                            |


Let's normalize this path and check the values:
```
val n = p.normalized
```

| property          | value                                         |
|-------------------|-----------------------------------------------|
| p.complete        | **"/first/second/fourth/myfile.html"**        |
| p.file            | "myfile.html"                                 |
| p.fileName        | "myfile"                                      |
| p.fileExtension   | "html"                                        |
| p.directory       | **"/first/second/fourth/"**                   |
| p.isAbsolute      | true                                          |
| p.device          | ""                                            |

Normalized path is a subclass of `Path`, so it's easy to check whether path object is normalized:
```
if (p is NormalizedPath) ...
``` 
This does not mean that Path object can't contain normalized path, but you can be
sure that NormalizedPath object **must** contain normalized path. 

Library was primarily created for **c4k** web crawling framework, but it's a standalone library...


... and I almost forgot: I'm new to Kotlin :) 