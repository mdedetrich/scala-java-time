/*
 * Copyright (c) 2008-2012, Stephen Colebourne & Michael Nascimento Santos
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
package javax.time.calendrical;

import static javax.time.calendrical.LocalDateTimeUnit.MONTHS;
import static javax.time.calendrical.LocalDateTimeUnit.WEEKS;

import javax.time.DateTimes;

/**
 * Mock DateTimeField that returns null.
 */
public enum MockFieldNoValue implements DateTimeField {

    INSTANCE;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public DateTimeValueRange getValueRange() {
        return DateTimeValueRange.of(1, 20);
    }

    @Override
    public PeriodUnit getBaseUnit() {
        return WEEKS;
    }

    @Override
    public PeriodUnit getRangeUnit() {
        return MONTHS;
    }

    @Override
    public long get(CalendricalObject calendrical) {
        return 0;
    }

    @Override
    public int compare(CalendricalObject calendrical1, CalendricalObject calendrical2) {
        return DateTimes.safeCompare(get(calendrical1), get(calendrical2));
    }

    //-----------------------------------------------------------------------
    @Override
    public DateTimeValueRange range(CalendricalObject calendrical) {
        return DateTimeValueRange.of(1, 20);
    }

    @Override
    public CalendricalObject set(CalendricalObject calendrical, long newValue) {
        return null;
    }

    @Override
    public CalendricalObject roll(CalendricalObject calendrical, long roll) {
        return null;
    }

    //-----------------------------------------------------------------------
    @Override
    public boolean resolve(DateTimeBuilder dateTimeBuilder, long value) {
        return false;
    }

}