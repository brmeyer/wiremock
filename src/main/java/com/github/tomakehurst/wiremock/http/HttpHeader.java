/*
 * Copyright (C) 2011 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.http;

import com.github.tomakehurst.wiremock.mapping.ValuePattern;
import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Lists.newArrayList;

public class HttpHeader {

    private final String key;
    private final List<String> values;

    public HttpHeader(String key, String... values) {
        this.key = key;
        this.values = newArrayList(values);
    }

    public HttpHeader(String key, Collection<String> values) {
        this.key = key;
        this.values = newArrayList(values);
    }

    public static HttpHeader httpHeader(String key, String... values) {
        return new HttpHeader(key, values);
    }

    public static HttpHeader absent(String key) {
        return new HttpHeader(key);
    }

    public static HttpHeader copyOf(HttpHeader header) {
        return new HttpHeader(header.key(), header.values());
    }

    public boolean isPresent() {
        return values.size() > 0;
    }

    public String key() {
        return key;
    }

    public String firstValue() {
        checkState(isPresent());
        return values.get(0);
    }

    public List<String> values() {
        checkState(isPresent());
        return values;
    }

    public boolean isSingleValued() {
        return values.size() == 1;
    }

    public boolean containsValue(String expectedValue) {
        return values.contains(expectedValue);
    }

    public boolean hasValueMatching(final ValuePattern valuePattern) {
        return any(values, new Predicate<String>() {
            public boolean apply(String headerValue) {
                return valuePattern.isMatchFor(headerValue);
            }
        });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String value: values) {
            sb.append(key).append(": ").append(value).append("\n");
        }

        return sb.toString();
    }

}