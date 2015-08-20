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

package no.api.meteo.services;

import lombok.NonNull;
import no.api.meteo.MeteoException;
import no.api.meteo.entity.core.service.locationforecast.Forecast;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecastDay;
import no.api.meteo.entity.extras.MeteoExtrasLongTermForecast;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static no.api.meteo.util.MeteoDateUtils.cloneZonedDateTime;

/**
 * Helper class that will save you from the dirty job of interpreting the forecast data yourself.
 *
 * <p>Interpreting the location forecast data can be pretty hard. This is an implementation of how we at Amedia
 * Utvikling chooses to do it.</p>
 */
public final class LocationForecastHelper {

    private final LocationForecast locationForecast;

    private MeteoForecastIndexer indexer = null;

    private String title = null;

    private final ZoneId zoneId;

    /**
     * Construct a new instance of this helper
     *
     * @param locationForecast
     *         The location forecast this helper will work on.
     * @param title
     *         Set a title for the forecast. Eg: The name of the location. Like a city.
     */
    public LocationForecastHelper(@NonNull LocationForecast locationForecast, String title) {
        this.title = title;
        this.locationForecast = locationForecast;
        zoneId = ZoneId.of("Z");
        init();
    }

    /**
     * Construct a new instance of this helper without any title set.
     *
     * @param locationForecast
     *         The location forecast this helper will work on.
     */
    public LocationForecastHelper(@NonNull LocationForecast locationForecast) {
        this.locationForecast = locationForecast;
        zoneId = ZoneId.of("Z");
        init();
    }

    /**
     * Get the title
     *
     * @return The title as set upon construction of this helper.
     */
    public String getTitle() {
        return title;
    }


    /**
     * Get all point forecasts from now and to the given hours ahead.
     *
     * @param hoursAhead The number of hours to look ahead for point forecasts.
     * @return List of found forecasts.
     */
    public List<MeteoExtrasForecast> findHourlyPointForecastsFromNow(int hoursAhead) {
        List<MeteoExtrasForecast> pointExtrasForecasts = new ArrayList<>();
        ZonedDateTime now = getNow();
        for (int i = 0; i < hoursAhead; i++) {
            ZonedDateTime ahead = now.plusHours(i);
            PointForecast pointForecast = indexer.getPointForecast(ahead);
            if (pointForecast != null) {
                PeriodForecast periodForecast = indexer.getTightestFitPeriodForecast(pointForecast.getFromTime());
                pointExtrasForecasts.add(new MeteoExtrasForecast(pointForecast, periodForecast));
            }
        }
        return pointExtrasForecasts;
    }

    private ZonedDateTime getNow() {
        return ZonedDateTime.now(zoneId);
    }

    /**
     * Get what we consider the best forecast for a given period of time within the location forecast.
     *
     * @param from
     *         The start time.
     * @param to
     *         The end time.
     *
     * @return Optional containing a forecast or {@link Optional#empty()} if no forecast could be created.
     */
    public Optional<MeteoExtrasForecast> findBestForecastForPeriod(ZonedDateTime from, ZonedDateTime to) {

        // Make sure the given input dates are converted to UTC before moving on.
        PeriodForecast periodForecast = indexer.getBestFitPeriodForecast(
                from.withZoneSameInstant(zoneId),
                to.withZoneSameInstant(zoneId));

        if (periodForecast == null) {
            return Optional.empty();
        }
        PointForecast pointForecast = indexer.getPointForecast(periodForecast.getFromTime());
        if (pointForecast == null) {
            return Optional.empty();
        }
        return Optional.of(new MeteoExtrasForecast(pointForecast, periodForecast));
    }

    /**
     * Create a longterm forecast.
     *
     * @return A long term forecast, which is a week in our view of the world. But how many days you will get in this
     * forecast depends on the given location forecast. So from 0-7 can be expected.
     */
    public MeteoExtrasLongTermForecast createLongTermForecast() {
        List<MeteoExtrasForecastDay> forecastDays = new ArrayList<>();
        ZonedDateTime dt = getNow();
        for (int i = 0; i <= 6; i++) {
            addForecastForDay(dt.plusDays(i), forecastDays);
        }
        return new MeteoExtrasLongTermForecast(forecastDays);
    }

    /**
     * Create a longterm forecast, but only with a small subset of the weather data fields. Typically for use in simple
     * weather reports where you only show the predicted weather icon and temperature, and not all the weather details.
     *
     * @return A long term forecast, which is a week in our view of the world.
     */
    public MeteoExtrasLongTermForecast createSimpleLongTermForecast() throws MeteoException {
        List<MeteoExtrasForecastDay> forecastDays = new ArrayList<>();
        ZonedDateTime dt = getNow();
        for (int i = 0; i <= 6; i++) {
            addSimpleForecastForDay(dt.plusDays(i), forecastDays);
        }
        return new MeteoExtrasLongTermForecast(forecastDays);
    }

    /**
     * Create a detailed forecast for a given date within this location forecast.
     *
     * @param dateTime
     *         The date to create the forecast for.
     *
     * @return The detailed forecast for the given date. Will be empty if data is not found.
     */
    public MeteoExtrasForecastDay createForcastForDay(ZonedDateTime dateTime) {
        ZonedDateTime dt = dateTime.withZoneSameInstant(zoneId);
        List<MeteoExtrasForecast> forecasts = new ArrayList<>();
        addForecastToList(findBestForecastForPeriod(dt.withHour(0), dt.withHour(6)), forecasts);
        addForecastToList(findBestForecastForPeriod(dt.withHour(6), dt.withHour(12)), forecasts);
        addForecastToList(findBestForecastForPeriod(dt.withHour(12), dt.withHour(18)), forecasts);
        addForecastToList(findBestForecastForPeriod(dt.withHour(18), dt.plusDays(1).withHour(0)), forecasts);
        return new MeteoExtrasForecastDay(dt.toLocalDate(), forecasts);
    }

    public MeteoExtrasForecastDay createSimpleForcastForDay(ZonedDateTime dateTime) {
        ZonedDateTime dt = dateTime.withZoneSameInstant(zoneId);
        List<MeteoExtrasForecast> forecasts = new ArrayList<>();
        addForecastToList(findNearestForecast(dt.withHour(14)), forecasts);
        return new MeteoExtrasForecastDay(dt.toLocalDate(), forecasts);
    }

    /**
     * Get the most accurate forecast for the current time.
     *
     * @return A forecast that best matches the time right now.
     */
    public Optional<MeteoExtrasForecast> getNearestForecast() {
        return findNearestForecast(getNow());
    }


    /**
     * Get the most accurate forecast for the given date.
     *
     * @param date The date to get the forecast for.
     * @return Optional containing the forecast if found, else {@link Optional#empty()}
     */
    public Optional<MeteoExtrasForecast> findNearestForecast(ZonedDateTime dateTime) {
        ZonedDateTime dt = dateTime.withZoneSameInstant(zoneId);
        PointForecast chosenForecast = null;
        for (Forecast forecast : locationForecast.getForecasts()) {
            if (forecast instanceof PointForecast) {
                PointForecast pointForecast = (PointForecast) forecast;
                if (isDateMatch(dt, cloneZonedDateTime(pointForecast.getFromTime()))) {
                    chosenForecast = pointForecast;
                    break;
                } else if (chosenForecast == null) {
                    chosenForecast = pointForecast;
                } else if (isNearerDate(pointForecast.getFromTime(), dt, chosenForecast.getFromTime())) {
                    chosenForecast = pointForecast;
                }
            }
        }
        return (chosenForecast == null ?
                Optional.<MeteoExtrasForecast>empty() :
                Optional.of(new MeteoExtrasForecast(chosenForecast,
                                                    indexer.getWidestFitPeriodForecast(chosenForecast.getFromTime()))));
    }

    private void init() {
        indexer = new MeteoForecastIndexer(locationForecast.getForecasts());
    }

    private void addForecastForDay(ZonedDateTime dt, List<MeteoExtrasForecastDay> lst) {
        if (indexer.hasForecastsForDay(dt)) {
            MeteoExtrasForecastDay mefd = createForcastForDay(dt);
            if (mefd != null && mefd.getForecasts().size() > 0) {
                lst.add(mefd);
            }
        }
    }

    private void addSimpleForecastForDay(ZonedDateTime dt, List<MeteoExtrasForecastDay> lst) throws MeteoException {
        if (indexer.hasForecastsForDay(dt)) {
            MeteoExtrasForecastDay mefd = createSimpleForcastForDay(dt);
            if (mefd != null && mefd.getForecasts().size() > 0) {
                lst.add(mefd);
            }
        }
    }

    private void addForecastToList(Optional<MeteoExtrasForecast> mef, List<MeteoExtrasForecast> lst) {
        if (mef.isPresent()) {
            lst.add(mef.get());
        }
    }

    private boolean isNearerDate(ZonedDateTime pointTime, ZonedDateTime dateTime, ZonedDateTime chosenTime) {
        return Math.abs(pointTime.toInstant().getEpochSecond() - dateTime.toInstant().getEpochSecond())
                < Math.abs(chosenTime.toInstant().getEpochSecond() - dateTime.toInstant().getEpochSecond());
    }

    private boolean isDateMatch(ZonedDateTime requestedDate, ZonedDateTime actualDate) {
        return requestedDate.getYear() == actualDate.getYear() &&
                requestedDate.getMonthValue() == actualDate.getMonthValue()
                && requestedDate.getDayOfMonth() == actualDate.getDayOfMonth() &&
                requestedDate.getHour() == actualDate.getHour();
    }


}
