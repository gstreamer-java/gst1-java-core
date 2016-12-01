GStreamer 1.x Java Core (gst1-java-core)
========================================

This is an unofficial set of Java bindings for [GStreamer 1.x][gstreamer], an open-source 
multimedia framework. The bindings are a fork of the original [GStreamer-Java][gstreamer-java]
bindings for GStreamer 0.10. 

These bindings are still under development. They are now mostly functional, but there has
not yet been a formal first release. Check under [releases][gst1-releases] for latest
test binaries, or build from source.

The bindings have been tested on Linux, Windows and OSX. You will need to have GStreamer 1.x
available in your path in order to use the bindings. Windows and OSX downloads of GStreamer are
available from the GStreamer project itself.

You will also need the [JNA (Java Native Access)][jna] library.

See the examples repository for usage. More documentation will follow. Please use the
[GStreamer-Java Google Group][gstreamer-java-group] to discuss usage or ask questions.

Please note: this is not an easy-to-use multimedia framework for beginners. It currently
requires people to both know the Java language and be familiar with the GStreamer framework
(or be prepared to apply things from tutorials on GStreamer programming in other languages
(e.g. python or C#) to the Java bindings).


[gstreamer]: https://gstreamer.freedesktop.org/
[gstreamer-java]: https://github.com/gstreamer-java/gstreamer-java
[gst1-releases]: https://github.com/gstreamer-java/gst1-java-core/releases
[gstreamer-java-group]: https://groups.google.com/forum/#!forum/gstreamer-java
[jna]: https://github.com/java-native-access/jna