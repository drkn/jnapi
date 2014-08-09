JNAPI
=====

Pure Java commandline subtitles downloader. Currently JNAPI uses napiprojekt.pl to download subtitles in Polish. JNAPI will try to guess subtiles format and convert it automatically to MPL2, SRT and MicroDVD formats for each video file.

Requirements
----

 - JDK7+

Installation
----

```sh
mvn package
cp scripts/jnapi ~/bin
cp target/jnapi.jar ~/bin
```
Note: This assumes you have bin directory in home home directory, which is also included in your PATH. You can use any other path directory to copy jnapi files

Usage
----

```
jnapi [path to your video file]
```
```
jnapi [path to a directory with video files]
```

License
----

MIT

**Free Software, Hell Yeah!**
