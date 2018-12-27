/*
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
 *
 *  Copyright © 2018 Yamashita,Takahiro
 */
package ee.jsf.context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import spec.interfaces.infrastructure.CurrentViewContext;

/**
 * 現在のクライアントコンテキストを扱う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
@RequestScoped
public class CurrentViewContextImpl implements CurrentViewContext {

    private FacesContext context;

    @PostConstruct
    private void init() {
        context = FacesContext.getCurrentInstance();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String currentViewId() {
        return context.getViewRoot().getViewId();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public <T extends Object> Object responseViewId(T baseUrl) {
        String _baseUrl = baseUrl.toString();
        if (_baseUrl.contains("faces-redirect")) {
            return baseUrl;
        }
        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("faces-redirect", Arrays.asList("true"));
        return context.getExternalContext().encodeRedirectURL(_baseUrl, parameters);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Locale clientLocate() {
        return context.getViewRoot().getLocale();
    }

}
