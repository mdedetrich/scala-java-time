/*
 * Copyright (c) 2008-2011, Stephen Colebourne & Michael Nascimento Santos
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

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.time.CalendricalException;

/**
 * A set of date-time fields.
 * <p>
 * Instances of this class store a map of field-value pairs.
 * Together these specify constraints on the dates and times that match.
 * For example, if an instance stores 'DayOfMonth=13' and 'DayOfWeek=Friday'
 * then it represents and matches only dates of Friday the Thirteenth.
 * <p>
 * All the values will be within the valid range for the field.
 * However, there is no cross validation between fields.
 * Thus, it is possible for the date-time represented to never exist.
 * For example, if an instance stores 'DayOfMonth=31' and 'MonthOfYear=February'
 * then there will never be a matching date.
 *
 * @author Michael Nascimento Santos
 * @author Stephen Colebourne
 */
public final class DateTimeFields
        implements Calendrical,
            CalendricalMatcher, Iterable<DateTimeField>, Serializable {

    /**
     * A singleton empty {@code DateTimeFields}, placing no restrictions on the date-time.
     */
    public static final DateTimeFields EMPTY = new DateTimeFields(Collections.<DateTimeField>emptyList());

    /**
     * A serialization identifier for this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The list of fields which never contains the same rule twice (as in a map), never null, may be empty.
     */
    private final List<DateTimeField> fields;

    /**
     * Obtains a {@code DateTimeFields} from a rule and value.
     * <p>
     * The parameters represent the two parts of a phrase like 'MonthOfYear 12'.
     *
     * @param rule  the rule defining the field, not null
     * @param value  the value of the rule, not necessarily valid for the rule
     * @return the date-time fields, never null
     */
    public static DateTimeFields of(DateTimeFieldRule rule, int value) {
        return of(DateTimeField.of(rule, value));
    }

    /**
     * Obtains a {@code DateTimeFields} from two rule-value pairs.
     * <p>
     * The pair of parameters represent the two parts of a phrase like 'MonthOfYear 12'.
     *
     * @param rule1  the first rule defining the field, not null
     * @param value1  the first value of the rule, not necessarily valid for the rule
     * @param rule2  the second rule defining the field, not null
     * @param value2  the second value of the rule, not necessarily valid for the rule
     * @return the date-time fields, never null
     * @throws IllegalArgumentException if any rule is duplicated
     */
    public static DateTimeFields of(DateTimeFieldRule rule1, int value1, DateTimeFieldRule rule2, int value2) {
        DateTimeField field1 = DateTimeField.of(rule1, value1);
        DateTimeField field2 = DateTimeField.of(rule2, value2);
        return of(Arrays.asList(field1, field2));
    }

    /**
     * Obtains a {@code DateTimeFields} from a {@code DateTimeField}.
     * <p>
     * The parameters represent the two parts of a phrase like 'MonthOfYear 12'.
     *
     * @param rule  the rule defining the field, not null
     * @param value  the value of the rule, not necessarily valid for the rule
     * @return the date-time fields, never null
     */
    public static DateTimeFields of(DateTimeField field) {
        ISOChronology.checkNotNull(field, "DateTimeField must not be null");
        return new DateTimeFields(Collections.singletonList(field));
    }

    /**
     * Obtains a {@code DateTimeFields} from an array of fields.
     * <p>
     * Each field represents a phrase like 'MonthOfYear 12'.
     * ,p>
     * The array must provide fields with no duplicate rules.
     *
     * @param fieldsIterable  the iterable providing fields
     * @return the date-time fields, never null
     * @throws IllegalArgumentException if any rule is duplicated
     */
    public static DateTimeFields of(DateTimeField... fields) {
        ISOChronology.checkNotNull(fields, "Array must not be null");
        return of(Arrays.asList(fields));
    }

    /**
     * Obtains a {@code DateTimeFields} from a collection of fields.
     * <p>
     * Each field represents a phrase like 'MonthOfYear 12'.
     * ,p>
     * The iterable must provide fields with no duplicate rules.
     *
     * @param fieldsIterable  the iterable providing fields
     * @return the date-time fields, never null
     * @throws IllegalArgumentException if any rule is duplicated
     */
    public static DateTimeFields of(Iterable<DateTimeField> fieldsIterable) {
        ISOChronology.checkNotNull(fieldsIterable, "Iterable must not be null");
        Set<DateTimeFieldRule> rules = new HashSet<DateTimeFieldRule>();
        List<DateTimeField> created = new ArrayList<DateTimeField>();
        for (DateTimeField field : fieldsIterable) {
            ISOChronology.checkNotNull(field, "DateTimeField must not be null");
            if (rules.add(field.getRule()) == false) {
                throw new IllegalArgumentException("Duplicate rules are not allowed");
            }
            created.add(field);
        }
        if (created.size() == 0) {
            return EMPTY;
        }
        Collections.sort(created, Collections.reverseOrder());
        return new DateTimeFields(created);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     *
     * @param assignedFields  the list of fields, which is assigned, not null
     */
    private DateTimeFields(List<DateTimeField> assignedFields) {
        fields = assignedFields;
    }

    /**
     * Ensure EMPTY singleton.
     *
     * @return the resolved instance
     * @throws ObjectStreamException if an error occurs
     */
    private Object readResolve() throws ObjectStreamException {
        return fields.isEmpty() ? EMPTY : this;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the size of the map of fields to values.
     * <p>
     * This method returns the number of field-value pairs stored.
     *
     * @return number of field-value pairs, zero or greater
     */
    public int size() {
        return fields.size();
    }

    /**
     * Iterates through all the fields.
     * <p>
     * This method fulfills the {@link Iterable} interface and allows looping
     * around the fields using the for-each loop.
     *
     * @return an iterator over the fields, never null
     */
    public Iterator<DateTimeField> iterator() {
        return fields.iterator();
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if this set of fields contains a mapping for the specified rule.
     * <p>
     * This method returns true if a value can be obtained for the specified rule.
     *
     * @param rule  the rule to query, null returns false
     * @return true if these fields contain the rule
     */
    public boolean contains(DateTimeFieldRule rule) {
        for (DateTimeField field : fields) {
            if (field.getRule().equals(rule)) {
                return true;
            }
        }
        return false;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value of the specified calendrical rule.
     * <p>
     * This method queries the value of the specified calendrical rule.
     * If the value cannot be returned for the rule from this instance then
     * an attempt is made to derive the value.
     * If that fails, {@code null} will be returned.
     *
     * @param rule  the rule to use, not null
     * @return the value for the rule, null if the value cannot be returned
     */
    public <T> T get(CalendricalRule<T> rule) {
        ISOChronology.checkNotNull(rule, "CalendricalRule must not be null");
        if (rule instanceof DateTimeFieldRule) {
            return rule.reify(get((DateTimeFieldRule) rule));
        }
        return rule.deriveValueFrom(this);
    }

    /**
     * Gets the value for the specified rule throwing an exception if the
     * rule is not present.
     * <p>
     * No attempt is made to derive values. The result is simply based on
     * the contents of the set of fields. If you want to derive a
     * value then use {@link #derive} or a {@link CalendricalMerger}.
     *
     * @param rule  the rule to query, not null
     * @return the field with the specified rule, null if not found
     */
    public DateTimeField get(DateTimeFieldRule rule) {
        ISOChronology.checkNotNull(rule, "DateTimeFieldRule must not be null");
        for (DateTimeField field : fields) {
            if (field.getRule().equals(rule)) {
                return field;
            }
        }
        return null;
    }

    /**
     * Gets the value for the specified rule.
     * <p>
     * The returned value may not be valid according to the minimum and maximum
     * values of the rule.
     * <p>
     * No attempt is made to derive values.
     * The result is simply based on the content of the stored field.
     *
     * @return the value of the rule
     * @throws CalendricalException if the field is not present
     */
    public int getValue(DateTimeFieldRule rule) {
        DateTimeField field = get(rule);
        if (field == null) {
            throw new CalendricalRuleException("Rule not found: " + rule, rule);
        }
        return field.getValue();
    }

    /**
     * Gets the value for the specified rule ensuring it is valid.
     * <p>
     * A valid value is one where the value is appropriate for the rule.
     * <p>
     * No attempt is made to derive values.
     * The result is simply based on the content of the stored field.
     *
     * @return the value of the rule, checked to ensure it is valid
     * @throws CalendricalException if the field is not present or is invalid
     */
    public int getValidValue(DateTimeFieldRule rule) {
        int value = getValue(rule);
        rule.checkValue(value);
        return value;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this {@code DateTimeFields} with the specified field.
     * <p>
     * This replaces the value of the rule if the rule is present, or adds the field
     * if the rule is not present.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param rule  the rule to alter, not null
     * @param value  the value to use
     * @return a {@code DateTimeFields} based on this fields with the specified field updated, never null
     */
    public DateTimeFields with(DateTimeFieldRule rule, int value) {
        return with(DateTimeField.of(rule, value));
    }

    /**
     * Returns a copy of this {@code DateTimeFields} with the specified field.
     * <p>
     * This replaces the value of the rule if the rule is present, or adds the field
     * if the rule is not present.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param fields  the fields to add to the returned object, not null
     * @return a {@code DateTimeFields} based on this fields with the specified field updated, never null
     */
    public DateTimeFields with(DateTimeField field) {
        ISOChronology.checkNotNull(fields, "DateTimeField must not be null");
        List<DateTimeField> newFields = new ArrayList<DateTimeField>(fields);
        for (ListIterator<DateTimeField> it = newFields.listIterator(); it.hasNext(); ) {
            DateTimeField itField = it.next();
            if (itField.getRule().equals(field.getRule())) {
                if (itField.getValue() == field.getValue()) {
                    return this;
                } else {
                    it.set(field);
                    return new DateTimeFields(newFields);
                }
            }
        }
        newFields.add(field);
        Collections.sort(newFields, Collections.reverseOrder());
        return new DateTimeFields(newFields);
    }

    /**
     * Returns a copy of this {@code DateTimeFields} with the specified field removed.
     * <p>
     * This removes the specified rule from those in the returned fields.
     * No error occurs if the rule is not present.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param rule  the field to remove from the returned fields, not null
     * @return a {@code DateTimeFields} based on this fields with the specified rule removed, never null
     */
    public DateTimeFields without(DateTimeFieldRule rule) {
        ISOChronology.checkNotNull(rule, "DateTimeFieldRule must not be null");
        List<DateTimeField> newFields = new ArrayList<DateTimeField>(fields);
        for (Iterator<DateTimeField> it = newFields.iterator(); it.hasNext(); ) {
            if (it.next().getRule().equals(rule)) {
                it.remove();
                if (newFields.size() == 0) {
                    return EMPTY;
                }
                return new DateTimeFields(newFields);
            }
        }
        return this;
    }

    //-----------------------------------------------------------------------
//    /**
//     * Returns a copy of this {@code DateTimeFields} with the value of the specified rule rolled.
//     * <p>
//     * This rolls the value of one rule by the specified amount.
//     * Rolling is the process of adding or subtracting the value of the field rolling
//     * around within the range. For example, 'MonthOfYear' is defined as having 12 months.
//     * Adding 3 to 'MonthOfYear 11' rolls around to result in 'MonthOfYear 2'.
//     * <p>
//     * This {@code DateTimeFields} is used as the context for the roll.
//     * For example, 'DayOfMonth' has a value-range between 28 and 31 depending on the month and year.
//     * If both the month and year can be derived from this object, then the value-range
//     * used in the roll will be accurate, otherwise the best estimate will be used.
//     * <p>
//     * This instance is immutable and unaffected by this method call.
//     *
//     * @param rule  the field to roll which must be present, not null
//     * @param amountToRollBy  the amount to roll by, positive or negative
//     * @return a {@code DateTimeFields} based on this fields with the specified amount rolled, never null
//     * @throws CalendricalException if the field is not present or is invalid
//     */
//    public DateTimeFields roll(DateTimeFieldRule rule, long amountToRollBy) {
//        long value = getValidValue(rule);
//        long rolled = rule.roll(value, amountToRollBy, this);
//        return with(rule, rolled);
//    }  // TODO

    //-----------------------------------------------------------------------
    /**
     * Checks if the fields in this object match those in the specified calendrical.
     * <p>
     * This implementation checks that all calendrical fields in this object match.
     *
     * @param calendrical  the calendrical to match, not null
     * @return true if the calendrical fields match, false otherwise
     */
    public boolean matchesCalendrical(Calendrical calendrical) {
        ISOChronology.checkNotNull(calendrical, "Calendrical must not be null");
        for (DateTimeField field : fields) {
            DateTimeField calField = field.getRule().getValue(calendrical);
            if (calField != null && calField.equals(field) == false) {  // TODO check logic
                return false;
            }
        }
        return true;
    }
    //-----------------------------------------------------------------------
    /**
     * Checks if this {@code DateTimeFields} is equal to the specified set of fields.
     * <p>
     * The comparison is based on the complete set of fields.
     *
     * @param other  the other fields to compare to, null returns false
     * @return true if this is equal to the specified fields
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof DateTimeFields) {
            DateTimeFields otherFields = (DateTimeFields) other;
            return fields.equals(otherFields.fields);
        }
        return false;
    }

    /**
     * A hash code for this set of fields.
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return fields.hashCode();
    }

    //-----------------------------------------------------------------------
    /**
     * Outputs this fields as a {@code String}, such as {@code [MonthOfYear 12, DayOfMonth 3]}.
     *
     * @return a descriptive representation of this field, not null
     */
    @Override
    public String toString() {
        return fields.toString();
    }

}
