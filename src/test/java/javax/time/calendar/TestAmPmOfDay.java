/*
 * Copyright (c) 2008-2010, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time.calendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.io.Serializable;
import java.util.Locale;

import javax.time.calendar.AmPmOfDay;
import javax.time.calendar.IllegalCalendarFieldValueException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test AmPmOfDay.
 *
 * @author Michael Nascimento Santos
 * @author Stephen Colebourne
 */
@Test
public class TestAmPmOfDay {

    @BeforeMethod
    public void setUp() {
    }

    //-----------------------------------------------------------------------
    public void test_interfaces() {
        assertTrue(Enum.class.isAssignableFrom(AmPmOfDay.class));
        assertTrue(Serializable.class.isAssignableFrom(AmPmOfDay.class));
        assertTrue(Comparable.class.isAssignableFrom(AmPmOfDay.class));
    }

    //-----------------------------------------------------------------------
    public void test_factory_int_singleton() {
        for (int i = 0; i <= 1; i++) {
            AmPmOfDay test = AmPmOfDay.of(i);
            assertEquals(test.getValue(), i);
            assertSame(AmPmOfDay.of(i), test);
        }
    }

    @Test(expectedExceptions=IllegalCalendarFieldValueException.class)
    public void test_factory_int_valueTooLow() {
        AmPmOfDay.of(-1);
    }

    @Test(expectedExceptions=IllegalCalendarFieldValueException.class)
    public void test_factory_int_valueTooHigh() {
        AmPmOfDay.of(2);
    }

//    //-----------------------------------------------------------------------
//    // get()
//    //-----------------------------------------------------------------------
//    public void test_get() {
//        assertEquals(AmPmOfDay.AM.get(ISOChronology.amPmOfDayRule()), AmPmOfDay.AM);
//        assertEquals(AmPmOfDay.PM.get(ISOChronology.amPmOfDayRule()), AmPmOfDay.PM);
//        
//        assertEquals(AmPmOfDay.AM.get(ISOChronology.hourOfAmPmRule()), null);
//    }

    //-----------------------------------------------------------------------
    // getShortText()
    //-----------------------------------------------------------------------
    public void test_getShortText() {
        assertEquals(AmPmOfDay.AM.getShortText(Locale.US), "AM");
        assertEquals(AmPmOfDay.PM.getShortText(Locale.US), "PM");
    }

//    public void test_getShortText_noText() {
//        assertEquals(AmPmOfDay.AM.getShortText(new Locale("", "")), "0");
//        assertEquals(AmPmOfDay.PM.getShortText(new Locale("", "")), "1");
//    }

    //-----------------------------------------------------------------------
    // getText()
    //-----------------------------------------------------------------------
    public void test_getText() {
        assertEquals(AmPmOfDay.AM.getText(Locale.US), "AM");
        assertEquals(AmPmOfDay.PM.getText(Locale.US), "PM");
    }

//    public void test_getText_noText() {
//        assertEquals(AmPmOfDay.AM.getText(new Locale("", "")), "0");
//        assertEquals(AmPmOfDay.PM.getText(new Locale("", "")), "1");
//    }

    //-----------------------------------------------------------------------
    // isAM()
    //-----------------------------------------------------------------------
    public void test_isAm() {
        assertEquals(AmPmOfDay.AM.isAm(), true);
        assertEquals(AmPmOfDay.PM.isAm(), false);
    }

    public void test_isPm() {
        assertEquals(AmPmOfDay.AM.isPm(), false);
        assertEquals(AmPmOfDay.PM.isPm(), true);
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    public void test_toString() {
        assertEquals(AmPmOfDay.AM.toString(), "AM");
        assertEquals(AmPmOfDay.PM.toString(), "PM");
    }

    //-----------------------------------------------------------------------
    // generated methods
    //-----------------------------------------------------------------------
    public void test_enum() {
        assertEquals(AmPmOfDay.valueOf("AM"), AmPmOfDay.AM);
        assertEquals(AmPmOfDay.values()[0], AmPmOfDay.AM);
    }

}
