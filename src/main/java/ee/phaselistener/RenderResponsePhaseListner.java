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
package ee.phaselistener;

import ee.jsf.messages.ClientComplementManager;
import ee.jsf.messages.InputFieldColorHandler;
import javax.enterprise.inject.spi.CDI;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * 入力フィールドの背景色を変更するためのPhaseListnerです.
 *
 * @author Yamashita,Takahiro
 */
public class RenderResponsePhaseListner implements PhaseListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void afterPhase(PhaseEvent phaseEvent) {
    }

    @Override
    public void beforePhase(PhaseEvent phaseEvent) {
        FacesContext context = phaseEvent.getFacesContext();

        InputFieldColorHandler fieldColorHandler = CDI.current().select(InputFieldColorHandler.class).get();
        ClientComplementManager clientComplementManager = CDI.current().select(ClientComplementManager.class).get();
        fieldColorHandler.updateErrorFieldColor(context, clientComplementManager);
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

}
