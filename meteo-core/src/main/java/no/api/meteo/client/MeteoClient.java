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

package no.api.meteo.client;

import java.net.URI;

public interface MeteoClient {

    /**
     * @param uri The MET API uri.
     * @return Response object containing the MET data.
     * @throws MeteoClientException If invalid url or content couldn't be fetched.
     */
    MeteoResponse fetchContent(URI uri) throws MeteoClientException;

    /**
     * Set proxy settings for the client.
     * 
     * @param hostName The proxy hostname to be used.
     * @param port The proxy port to be used.
     */
    void setProxy(String hostName, int port);

    /**
     * Set the connection timeout to be used when fetching data from the MET API.
     *
     * @param timeout The timeout in seconds.
     */
    void setTimeout(int timeout);

    /**
     * When HttpClient instance is no longer needed, shut down the client to ensure immediate deallocation
     * of all system resources.
     */
    void shutdown();
}
