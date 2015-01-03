/*
 * Copyright (c) 2011-2013 Amedia AS.
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

package no.api.meteo.entity.core.service.sunrise;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class Moon extends AbstractType {

    private final PhaseType phase;

    public Moon(Date rise, Date set, Boolean neverRise, Boolean neverSet,
                List<ErrorType> error, PhaseType phase) {
        super(rise, set, neverRise, neverSet, error);
        this.phase = phase;
    }
}
