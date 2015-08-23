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

package no.api.freemarker.java8.time;

import freemarker.core.Environment;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.List;

import static no.api.freemarker.java8.time.DateTimeTools.METHOD_FORMAT;
import static no.api.freemarker.java8.time.DateTimeTools.METHOD_UNKNOWN_MSG;

/**
 * ZoneIdAdapter adds basic format support for {@link ZoneId} too FreeMarker 2.3.23 and above.
 */
public class ZoneIdAdapter extends AbstractAdapter<ZoneId>
        implements AdapterTemplateModel, TemplateScalarModel, TemplateHashModel {

    public ZoneIdAdapter(ZoneId obj) {
        super(obj);
    }

    @Override
    public String getAsString() throws TemplateModelException {
        return getObject().getDisplayName(TextStyle.FULL, Environment.getCurrentEnvironment().getLocale());
    }

    @Override
    public TemplateModel get(String s) throws TemplateModelException {
        if (METHOD_FORMAT.equals(s)) {
            return new ZoneIdFormatter(getObject());
        }
        throw new TemplateModelException(METHOD_UNKNOWN_MSG + s);
    }

    public class ZoneIdFormatter extends AbstractTextStyleLocaleFormatter<ZoneId> implements TemplateMethodModelEx {

        public ZoneIdFormatter(ZoneId obj) {
            super(obj);
        }

        @Override
        public Object exec(List list) throws TemplateModelException {
            return getObject().getDisplayName(findTextStyle(list), findLocale(list));
        }

    }
}
