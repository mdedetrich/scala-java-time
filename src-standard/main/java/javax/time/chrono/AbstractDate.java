/*
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
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
package javax.time.chrono;

import javax.time.CalendricalException;
import javax.time.LocalDate;
import javax.time.builder.CalendricalObject;

/**
 * A date based on standard chronology rules.
 * <p>
 * This class is intended for applications that need to use a calendar system other than
 * ISO-8601, the <i>de facto</i> world calendar.
 */
public abstract class AbstractDate implements CalendricalObject {

    /**
     * Creates an instance.
     */
    protected AbstractDate() {
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    @Override
    public <T> T extract(Class<T> type) {
        if (type == LocalDate.class) {
            return (T) toLocalDate();
        } else if (type == Chrono.class) {
            return (T) getChrono();
        }
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value of the specified date field.
     * <p>
     * This method queries the value of the specified field.
     * The field specified is chronology-neutral.
     * The same set of fields are used to describe all chronologies.
     *
     * @param field  the field to query, not null
     * @return the value of the field
     */
    public int get(ChronoField field) {
        switch (field) {
            case DAY_OF_WEEK: return getDayOfWeekValue();
            case DAY_OF_MONTH: return getDayOfMonth();
            case DAY_OF_YEAR: return getDayOfYear();
            case MONTH_OF_YEAR: return getMonthOfYear();
            case YEAR_OF_ERA: return getYearOfEra();
            case PROLEPTIC_YEAR: return getProlepticYear();
            case ERA: return getEra().getValue();
        }
        throw new CalendricalException("Unknown field");
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the calendar system in use for this date.
     * <p>
     * The {@code Chrono} represents the calendar system.
     * The fields of this date are all expressed relative to this.
     * 
     * @return the calendar system, not null
     */
    public abstract Chrono getChrono();

    /**
     * Gets the era, as defined by the calendar system.
     * <p>
     * The era is, conceptually, the largest division of the time-line.
     * Most calendar systems have a single epoch dividing the time-line into two eras.
     * However, some have multiple eras, such as one for the reign of each leader.
     * The exact meaning is determined by the chronology according to the following constraints.
     * <p>
     * The era in use at 1970-01-01 (ISO) must have the value 1.
     * Later eras must have sequentially higher values.
     * Earlier eras must have sequentially lower values.
     * Each chronology must refer to an enum or similar singleton to provide the era values.
     * <p>
     * All correctly implemented {@code Era} classes are singletons, thus it
     * is valid code to write {@code date.getEra() == SomeEra.ERA_NAME)}.
     *
     * @return the era, of the correct type for this chronology, not null
     */
    public abstract Era getEra();

    /**
     * Gets the year-of-era, as defined by the calendar system.
     * <p>
     * The year-of-era is a value representing the count of years within the era.
     * The exact meaning is determined by the chronology according to the following constraints.
     * <p>
     * The year-of-era value must be positive.
     *
     * @return the year-of-era, within the valid range for the chronology
     */
    public abstract int getYearOfEra();

    /**
     * Gets the proleptic-year, as defined by the calendar system.
     * <p>
     * The proleptic-year is a single value representing the year.
     * It combines the era and year-of-era, and increases uniformly as time progresses.
     * The exact meaning is determined by the chronology according to the following constraints.
     * <p>
     * The proleptic-year has a small, or negative, value in the past.
     * Later years have sequentially higher values.
     * Where possible, the proleptic-year will be the same as the year-of-era
     * for the era that is active on 1970-01-01 however this is not guaranteed.
     *
     * @return the proleptic-year, within the valid range for the chronology
     */
    public abstract int getProlepticYear();

    /**
     * Gets the month-of-year, as defined by the calendar system.
     * <p>
     * The month-of-year is a value representing the count of months within the year.
     * The exact meaning is determined by the chronology according to the following constraints.
     * <p>
     * The month-of-year value must be positive.
     *
     * @return the month-of-year, within the valid range for the chronology
     */
    public abstract int getMonthOfYear();

    /**
     * Gets the day-of-month, as defined by the calendar system.
     * <p>
     * The day-of-month is a value representing the count of days within the month.
     * The exact meaning is determined by the chronology according to the following constraints.
     * <p>
     * The day-of-month value must be positive.
     *
     * @return the day-of-month, within the valid range for the chronology
     */
    public abstract int getDayOfMonth();

    /**
     * Gets the day-of-year, as defined by the calendar system.
     * <p>
     * The day-of-year is a value representing the count of days within the year.
     * The exact meaning is determined by the chronology according to the following constraints.
     * <p>
     * The day-of-year value must be positive.
     * The number of days in a year may vary.
     *
     * @return the day-of-year, within the valid range for the chronology
     */
    public abstract int getDayOfYear();

    /**
     * Gets the day-of-week value for the calendar system.
     * <p>
     * The day-of-week is a value representing the count of days within the week.
     * The exact meaning is determined by the chronology according to the following constraints.
     * <p>
     * The day-of-week value must be positive.
     * The number of days in a week may vary.
     * 
     * @return the day-of-week value
     */
    protected abstract int getDayOfWeekValue();

    //-----------------------------------------------------------------------
    /**
     * Checks if the year is a leap year, as defined by the calendar system.
     * <p>
     * A leap-year is a year of a longer length than normal.
     * The exact meaning is determined by the chronology according to the following constraints.
     * <p>
     * A leap-year must imply a year-length longer than a non leap-year.
     *
     * @return true if this date is in a leap year, false otherwise
     */
    public boolean isLeapYear() {
        return getChrono().isLeapYear(getProlepticYear());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this date with the specified field altered.
     * <p>
     * This method returns a new date based on this date with a new value for the specified field.
     * This can be used to change any field, for example to set the year, month of day-of-month.
     * The field specified is chronology-neutral.
     * The same set of fields are used to describe all chronologies.
     * <p>
     * In some cases, changing the specified field can cause the resulting date to become invalid.
     * If this occurs, then other fields, typically the day-of-month, will be adjusted to ensure
     * that the result is valid. Typically this will select the last valid day of the month.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param field  the field to set in the returned date, not null
     * @param newValue  the new value of the field in the returned date, not null
     * @return a date based on this one with the specified field set, not null
     */
    public abstract AbstractDate with(ChronoField field, int newValue);

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this date with the specified period in years added.
     * <p>
     * This adds the specified period in years to the date.
     * In some cases, adding years can cause the resulting date to become invalid.
     * If this occurs, then the day-of-month will be adjusted to the last valid day of the month.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param years  the years to add, may be negative
     * @return a date based on this one with the years added, not null
     * @throws CalendricalException if the result exceeds the supported date range
     */
    public abstract AbstractDate plusYears(long years);

    /**
     * Returns a copy of this date with the specified period in months added.
     * <p>
     * This adds the specified period in months to the date.
     * In some cases, adding months can cause the resulting date to become invalid.
     * If this occurs, then the day-of-month will be adjusted to the last valid day of the month.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param months  the months to add, may be negative
     * @return a date based on this one with the months added, not null
     * @throws CalendricalException if the result exceeds the supported date range
     */
    public abstract AbstractDate plusMonths(long months);

    /**
     * Returns a copy of this date with the specified period in weeks added.
     * <p>
     * This adds the specified period in weeks to the date.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param weeks  the weeks to add, may be negative
     * @return a date based on this one with the weeks added, not null
     * @throws CalendricalException if the result exceeds the supported date range
     */
    public abstract AbstractDate plusWeeks(long weeks);

    /**
     * Returns a copy of this date with the specified number of days added.
     * <p>
     * This adds the specified period in days to the date.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param days  the days to add, may be negative
     * @return a date based on this one with the days added, not null
     * @throws CalendricalException if the result exceeds the supported date range
     */
    public abstract AbstractDate plusDays(long days);

    //-----------------------------------------------------------------------
    /**
     * Converts this date to the standard epoch-day from 1970-01-01 (ISO).
     * <p>
     * This converts this date to the equivalent standard ISO date.
     * The conversion ensures that the date is accurate at midday.
     * 
     * @return the equivalent date, not null
     */
    public abstract long toEpochDay();

    /**
     * Converts this date to the standard {@code LocalDate}.
     * <p>
     * This converts this date to the equivalent standard ISO date.
     * The conversion ensures that the date is accurate at midday.
     * 
     * @return the equivalent date, not null
     */
    public LocalDate toLocalDate() {
        return LocalDate.ofEpochDay(toEpochDay());
    }

    //-----------------------------------------------------------------------
    /**
     * Outputs this date as a {@code String}, such as {@code 1723AD-13-01 (Gregorian)}.
     * <p>
     * The output will be in the format {@code {year}{era}-{month}-{day} ({chrono})}.
     *
     * @return the formatted date, not null
     */
    @Override
    public String toString() {
        int yearValue = getYearOfEra();
        int monthValue = getMonthOfYear();
        int dayValue = getDayOfMonth();
        int absYear = Math.abs(yearValue);
        StringBuilder buf = new StringBuilder(12);
        if (absYear < 1000) {
            buf.append(yearValue + 10000).deleteCharAt(0);
        } else {
            buf.append(yearValue);
        }
        return buf.append(getEra())
            .append(monthValue < 10 ? "-0" : "-").append(monthValue)
            .append(dayValue < 10 ? "-0" : "-").append(dayValue)
            .append(" (").append(getChrono().getName()).append(')')
            .toString();
    }

}