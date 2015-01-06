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

package no.api.meteo.service.sunrise.builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.api.meteo.entity.core.Location;
import no.api.meteo.entity.core.Meta;
import no.api.meteo.entity.core.service.sunrise.Sunrise;
import no.api.meteo.entity.core.service.sunrise.SunriseDate;
import no.api.meteo.util.EntityBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
public class SunriseBuilder implements EntityBuilder<Sunrise> {

    @Setter
    @Getter
    private Date created;

    @Setter
    @Getter
    private Meta meta;

    @Setter
    @Getter
    private Location location;

    @Setter
    @Getter
    private List<SunriseDate> dates = new ArrayList<>();

    @Override
    public Sunrise build() {
        return new Sunrise(getCreated(), getMeta(), getLocation(), getDates());
    }
}
