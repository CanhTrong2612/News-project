package com.example.news.XmlPullParser

import android.util.Log
import android.util.Xml
import com.example.news.model.News
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream


class HandelXmlPullParser {
    val ns: String? = null

    @Throws(XmlPullParserException::class, IOException::class)
    fun Pasers(`in`: InputStream): List<News?>? {
        return try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(`in`, null)
            parser.nextTag()
            readRss(parser)
        } finally {
            `in`.close()
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readRss(parser: XmlPullParser): List<News?>? {
        Log.i("-----", "read rss")
        val list: MutableList<News> = ArrayList<News>()
        parser.require(XmlPullParser.START_TAG, ns, "rss")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name1 = parser.name
            if (name1 == "channel") {
                parser.require(XmlPullParser.START_TAG, ns, "channel")
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    val name = parser.name
                    if (name == "item") {
                        list.add(readItem(parser))
                    } else {
                        skip(parser)
                    }
                }
            }
        }
        return list
    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun readItem(parser: XmlPullParser): News {
        Log.i("-----", "read Item")
        parser.require(XmlPullParser.START_TAG, ns, "item")
        var title: String? = null
        var link: String? = null
        var pubDate: String? = null
        var img: String? = null
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            if (name == "title") {
                title = readTitle(parser)
                Log.i("-------title ", title!!)
            } else if (name == "link") {
                link = readLink(parser)
                Log.i("-------link ", link!!)
            } else if (name == "description") {
                img = readImgLink(parser)
                Log.i("-------link img", img!!)
            } else if (name == "pubDate") {
                pubDate = readDate(parser)
                Log.i("-------pubDate ", pubDate!!)
            } else {
                skip(parser)
            }
        }
        return News(title!!, link!!, pubDate!!, img!!)
    }


    private fun getLinkFromText(a: String): String {
        var a = a
        val link = ""
        if (a.contains("img")) {
            val posImg = a.indexOf("img") // lấy vị trí chuỗi "img" trong chỗi
            val posDong = a.indexOf("/>")
            a = a.substring(posImg, posDong)
            a = a.substring(a.indexOf("\"") + 1, a.length - 2)
        }
        return a
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readImgLink(parser: XmlPullParser): String? {
        Log.i("-----", "read description" + parser.text)
        var imageLink = ""
        parser.require(XmlPullParser.START_TAG, ns, "description")
        while (parser.next() != XmlPullParser.END_TAG) {
            val name = parser.text
            imageLink = getLinkFromText(name)
            Log.i("-----", "read description IMG SRC: $imageLink")
        }
        return imageLink
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        check(parser.eventType == XmlPullParser.START_TAG)
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readText(parser: XmlPullParser): String? {
        //Log.i("-----", "read text");
        var rs: String? = ""
        if (parser.next() == XmlPullParser.TEXT) {
            rs = parser.text
            parser.nextTag()
        }
        return rs
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTitle(parser: XmlPullParser): String? {
        Log.i("-----", "read title")
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readLink(parser: XmlPullParser): String? {
        Log.i("-----", "read link")
        parser.require(XmlPullParser.START_TAG, ns, "link")
        val link = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "link")
        return link
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readDate(parser: XmlPullParser): String? {
        Log.i("-----", "read date")
        parser.require(XmlPullParser.START_TAG, ns, "pubDate")
        val pubDate = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "pubDate")
        return pubDate
    }
}