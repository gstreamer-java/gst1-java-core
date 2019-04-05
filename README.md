GStreamer 1.x Java Core (gst1-java-core)
========================================

This is a set of Java bindings for [GStreamer 1.x][gstreamer], an open-source 
multimedia framework.

## Status and support

Releases are available via Maven Central (under the `org.freedesktop.gstreamer`
group ID), or can be downloaded from the GitHub [release page][gst1-releases].

The bindings are actively in use in a variety of commercial and open-source projects
and are functionally stable. Having now reached v1 they should also be considered
API stable. This does not apply to the lowlevel package which is _effectively_ non-public
and subject to change at any time.

The current lead maintainer of the bindings is Neil C Smith on behalf of the
[PraxisLIVE / PraxisCORE][praxislive] projects. The bindings are a fork of the
original [GStreamer-Java][gstreamer-java] bindings for GStreamer 0.10 started by
Wayne Meissner, and numerous other people have made valuable contributions to the 
original project and the 1.x fork over the years.

Initial help on getting started, and support for open-source projects, can be obtained
from the [GStreamer-Java Google Group][gstreamer-java-group].

Commercial support is available, and sponsorship of features is welcome - please
email support@praxislive.org to discuss.

## Requirements

The bindings have been tested on Linux, Windows and OSX. Windows and OSX installers
for GStreamer are available from the [GStreamer project itself][gstreamer-download].
Linux users should be able to get GStreamer from their distribution repository if it
isn't already installed.

You will need to have the GStreamer 1.x native library available in your system path
in order to use the bindings, and may also need to set up environment variables
depending on how GStreamer is installed. See [how PraxisCORE handles this][praxiscore-gstreamer],
including use of JNA Platform to set up the Windows `PATH`.

It is possible to ship GStreamer with your application should you not wish your users
to have to install it separately. There are various ways to achieve this - see the
[upstream documentation][gstreamer-deploy]. Advice is also available via the support
options above.

The minimum supported version of GStreamer is 1.8.x. If you require access to features
related to later GStreamer versions (eg. WebRTC support), make sure to request the
version you need when calling `Gst.init(..)`

You will also need the [JNA (Java Native Access)][jna] library, minimum version 5.2.0.

The minimum required Java version is now Java 8.

## Usage

See the examples repository for usage. More documentation will follow. Please use the
[GStreamer-Java Google Group][gstreamer-java-group] to discuss usage or ask questions.

Use the Javadoc! All classes are documented, and include links to the relevant
native documentation where appropriate.

Please note: this is not an easy-to-use multimedia framework for beginners. It currently
requires people to both know the Java language and be familiar with the GStreamer framework
(or be prepared to apply things from tutorials on GStreamer programming in other languages
to the Java bindings).

## Contributions

Contributions to the library are welcomed, either to fix / enhance current features,
or bring in new ones. There is also ongoing work to rework the lowlevel bindings.

**Before opening a Pull Request** please raise an issue or discuss your contribution on
the mailing list. New features must have tests, selectively applied if targeting
features in versions of GStreamer above 1.8. All Pull Requests will be automatically
tested via CI, and all tests must pass before merging will be considered.

If you are making a large contribution to benefit a commercial project, sponsorship
of integration and support time would be appreciated.


[gstreamer]: https://gstreamer.freedesktop.org/
[gstreamer-download]: https://gstreamer.freedesktop.org/download/
[gstreamer-deploy]: https://gstreamer.freedesktop.org/documentation/deploying/index.html
[gstreamer-java]: https://github.com/gstreamer-java/gstreamer-java
[gst1-releases]: https://github.com/gstreamer-java/gst1-java-core/releases
[gstreamer-java-group]: https://groups.google.com/forum/#!forum/gstreamer-java
[jna]: https://github.com/java-native-access/jna
[praxislive]: https://www.praxislive.org
[praxiscore-gstreamer]: https://github.com/praxis-live/praxis/blob/master/praxis.video.gstreamer/src/org/praxislive/video/gstreamer/components/GStreamerLibrary.java#L45