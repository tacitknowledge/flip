/*
 * Copyright 2012 Tacit Knowledge.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tacitknowledge.flip.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author ssoloviov
 */
class DateAdapter extends XmlAdapter<String, Date> {
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}(\\s+\\d{1,2}:\\d{1,2}(\\s*[+-]?\\d{4})?)?");

    @Override
    public Date unmarshal(String value) throws Exception {
        if (value == null) {
            return null;
        }
        Matcher m = DATE_PATTERN.matcher(value);
        if (!m.matches()) {
            return null;
        }
        Date date = null;
        if (m.group(1) == null) {
            date = new SimpleDateFormat("yyyy-M-d").parse(value);
        } else if (m.group(2) == null) {
            date = new SimpleDateFormat("yyyy-M-d H:m").parse(value);
        } else {
            date = new SimpleDateFormat("yyyy-M-d H:m Z").parse(value);
        }
        return date;
    }

    @Override
    public String marshal(Date value) throws Exception {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm Z").format(value);
    }
    
}
