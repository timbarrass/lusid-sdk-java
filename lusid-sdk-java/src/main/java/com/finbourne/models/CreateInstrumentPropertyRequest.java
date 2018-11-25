/**
 * Copyright © 2018 FINBOURNE TECHNOLOGY LTD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package com.finbourne.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The CreateInstrumentPropertyRequest model.
 */
public class CreateInstrumentPropertyRequest {
    /**
     * The property key of the property, e.g, 'Instrument/default/Isin'.
     */
    @JsonProperty(value = "instrumentPropertyKey")
    private String instrumentPropertyKey;

    /**
     * The value of the property, which must not be empty or null. e.g,
     * 'US0378331005'.
     */
    @JsonProperty(value = "property")
    private PropertyValue property;

    /**
     * Get the property key of the property, e.g, 'Instrument/default/Isin'.
     *
     * @return the instrumentPropertyKey value
     */
    public String instrumentPropertyKey() {
        return this.instrumentPropertyKey;
    }

    /**
     * Set the property key of the property, e.g, 'Instrument/default/Isin'.
     *
     * @param instrumentPropertyKey the instrumentPropertyKey value to set
     * @return the CreateInstrumentPropertyRequest object itself.
     */
    public CreateInstrumentPropertyRequest withInstrumentPropertyKey(String instrumentPropertyKey) {
        this.instrumentPropertyKey = instrumentPropertyKey;
        return this;
    }

    /**
     * Get the value of the property, which must not be empty or null. e.g, 'US0378331005'.
     *
     * @return the property value
     */
    public PropertyValue property() {
        return this.property;
    }

    /**
     * Set the value of the property, which must not be empty or null. e.g, 'US0378331005'.
     *
     * @param property the property value to set
     * @return the CreateInstrumentPropertyRequest object itself.
     */
    public CreateInstrumentPropertyRequest withProperty(PropertyValue property) {
        this.property = property;
        return this;
    }

}
