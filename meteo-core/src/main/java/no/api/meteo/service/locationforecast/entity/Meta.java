/*
 * Copyright (c) 2011 A-pressen Digitale Medier
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

package no.api.meteo.service.locationforecast.entity;

import net.sf.oval.constraint.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Meta {

    @NotNull
    private URL licenseUrl;

    private List<Model> models = new ArrayList<Model>();

    public URL getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(URL licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        if (models != null) {
            this.models = models;
        }
    }
}