GStreamer 1.x Java Core (gst1-java-core)
========================================

This is a set of Java bindings for [GStreamer 1.x][gstreamer], an open-source 
multimedia framework. The bindings are a fork of the original [GStreamer-Java][gstreamer-java]
bindings for GStreamer 0.10. 

## Status and support

Releases are available via Maven Central (under the `org.freedesktop.gstreamer` group ID),
or can be downloaded from the GitHub [release page][gst1-releases].

The bindings are currently at version 0.9.x as we progress to a 1.0 release. However,
they are actively in use in a number of open-source and commercial projects, and 
should generally be considered stable (they are *more* stable than the 0.10 bindings).
The version number reflects the fact that some work remains to be done to complete the 
fork from the 0.10 bindings, which might result in minimal breaking changes.

The current lead maintainer of the bindings is Neil C Smith on behalf of the [Praxis LIVE][praxislive]
project. The original 0.10 project was started by Wayne Meissner, and numerous other
people have made valuable contributions to the original project and the 1.x fork over the years.

Initial help on getting started, and support for open-source projects, can be obtained from the
[GStreamer-Java Google Group][gstreamer-java-group].

Commercial support is available, and sponsorship of features is welcome (it all 
helps us get to 1.0!) - please email support@praxislive.org for more information.

## Requirements

The bindings have been tested on Linux, Windows and OSX. You will need to have GStreamer 1.x
available in your path in order to use the bindings. Windows and OSX installers for GStreamer are
available from the [GStreamer project itself][gstreamer-download]. Linux users should be
able to get GStreamer from their distribution repository if it isn't already installed.

It is possible to ship GStreamer with your application if you not wish your users
to have to install it separately. [Check it here](https://gstreamer.freedesktop.org/documentation/deploying/index.html)

The minimum supported version of GStreamer is 1.8.x.

You will also need the [JNA (Java Native Access)][jna] library, minimum version 4.4.0.

## Usage

See the examples repository for usage. More documentation will follow. Please use the
[GStreamer-Java Google Group][gstreamer-java-group] to discuss usage or ask questions.

Please note: this is not an easy-to-use multimedia framework for beginners. It currently
requires people to both know the Java language and be familiar with the GStreamer framework
(or be prepared to apply things from tutorials on GStreamer programming in other languages
(e.g. python or C#) to the Java bindings).


[gstreamer]: https://gstreamer.freedesktop.org/
[gstreamer-download]: https://gstreamer.freedesktop.org/download/
[gstreamer-java]: https://github.com/gstreamer-java/gstreamer-java
[gst1-releases]: https://github.com/gstreamer-java/gst1-java-core/releases
[gstreamer-java-group]: https://groups.google.com/forum/#!forum/gstreamer-java
[jna]: https://github.com/java-native-access/jna
[praxislive]: http://www.praxislive.org
