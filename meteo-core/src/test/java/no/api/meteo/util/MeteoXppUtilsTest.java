/*
 * Copyright (c) 2011-2015 Amedia Utvikling AS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package no.api.meteo.util;

import org.junit.Assert;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MeteoXppUtilsTest {

    private String testXml = "<test><entry string=\"foo\" integer=\"7\" double=\"1.2\"" +
            " boolean=\"true\" date=\"2011-05-06T05:09:00Z\" simple=\"2011-02-03\"/></test>";

    @Test
    public void testConstructor() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
            InstantiationException {
        Constructor c = MeteoXppUtils.class.getDeclaredConstructor();
        Assert.assertFalse(c.isAccessible());
        c.setAccessible(true);
        c.newInstance();
    }

    @Test
    public void testCreateNewPullParser() throws Exception {
        XmlPullParser xpp = MeteoXppUtils.createPullParser(testXml);
        Assert.assertNotNull(xpp);
    }

    @Test
    public void testGetAttributeValue() throws Exception {
        XmlPullParser xpp = MeteoXppUtils.createPullParser(testXml);
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals("entry")) {
                    // String
                    Assert.assertEquals("foo", MeteoXppUtils.getString(xpp, "string"));
                    Assert.assertNull(MeteoXppUtils.getString(xpp, "foo"));
                    // Integer
                    Assert.assertEquals(new Integer(7), MeteoXppUtils.getInteger(xpp, "integer"));
                    Assert.assertNull(MeteoXppUtils.getInteger(xpp, "string"));
                    Assert.assertNull(MeteoXppUtils.getInteger(xpp, "foo"));
                    // Double
                    Assert.assertEquals(1.2, MeteoXppUtils.getDouble(xpp, "double"), 0.0);
                    Assert.assertNull(MeteoXppUtils.getDouble(xpp, "string"));
                    Assert.assertNull(MeteoXppUtils.getDouble(xpp, "foo"));
                    // Boolean
                    Assert.assertEquals(Boolean.TRUE, MeteoXppUtils.getBoolean(xpp, "boolean"));
                    Assert.assertFalse(MeteoXppUtils.getBoolean(xpp, "foo"));
                    // Simple date
                    Date simpleDate = MeteoXppUtils.getSimpleDate(xpp, "simple");
                    Assert.assertNull(MeteoXppUtils.getSimpleDate(xpp, "foo"));
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(simpleDate);
                    Assert.assertEquals(2011, calendar.get(Calendar.YEAR));
                    Assert.assertEquals(3, calendar.get(Calendar.DAY_OF_MONTH));
                    Assert.assertEquals(1, calendar.get(Calendar.MONTH));
                    // Full date 2011-05-06T05:00:00Z
                    Date date = MeteoXppUtils.getDate(xpp, "date");
                    Assert.assertNull(MeteoXppUtils.getDate(xpp, "foo"));
                    calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    Assert.assertEquals(9, calendar.get(Calendar.MINUTE));
                }
            }
            eventType = xpp.next();
        }
    }

}
