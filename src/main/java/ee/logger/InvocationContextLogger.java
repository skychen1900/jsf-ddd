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
 *  Copyright © 2019 Yamashita,Takahiro
 */
package ee.logger;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.interceptor.InvocationContext;

/**
 * InterceptorのInvocationContextを使用したLogger出力を扱う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
public class InvocationContextLogger {

    private final String actionClass;
    private final String actionMethod;
    private final Logger logger;

    private InvocationContextLogger(Logger logger, String actionClass, String actionMethod) {
        this.logger = logger;
        this.actionClass = actionClass;
        this.actionMethod = actionMethod;
    }

    public static InvocationContextLogger getLogger(InvocationContext ic) {
        String actionClass = ic.getTarget().getClass().getSuperclass().getName();
        String actionMethod = ic.getMethod().getName();
        Logger logger = LoggerFactory.getLogger(actionClass);
        return new InvocationContextLogger(logger, actionClass, actionMethod);
    }

    public void severe(String msg) {
        logger.logp(Level.SEVERE, actionClass, actionMethod, msg);
    }

    public void severe(Supplier<String> msgSupplier) {
        logger.logp(Level.SEVERE, actionClass, actionMethod, msgSupplier);
    }

    public void warning(String msg) {
        logger.logp(Level.WARNING, actionClass, actionMethod, msg);
    }

    public void warning(Supplier<String> msgSupplier) {
        logger.logp(Level.WARNING, actionClass, actionMethod, msgSupplier);
    }

    public void info(String msg) {
        logger.logp(Level.INFO, actionClass, actionMethod, msg);
    }

    public void info(Supplier<String> msgSupplier) {
        logger.logp(Level.INFO, actionClass, actionMethod, msgSupplier);
    }

    public void config(String msg) {
        logger.logp(Level.CONFIG, actionClass, actionMethod, msg);
    }

    public void config(Supplier<String> msgSupplier) {
        logger.logp(Level.CONFIG, actionClass, actionMethod, msgSupplier);
    }

    public void fine(String msg) {
        logger.logp(Level.FINE, actionClass, actionMethod, msg);
    }

    public void fine(Supplier<String> msgSupplier) {
        logger.logp(Level.FINE, actionClass, actionMethod, msgSupplier);
    }

    public void finer(String msg) {
        logger.logp(Level.FINER, actionClass, actionMethod, msg);
    }

    public void finer(Supplier<String> msgSupplier) {
        logger.logp(Level.FINER, actionClass, actionMethod, msgSupplier);
    }

    public void finest(String msg) {
        logger.logp(Level.FINEST, actionClass, actionMethod, msg);
    }

    public void finest(Supplier<String> msgSupplier) {
        logger.logp(Level.FINEST, actionClass, actionMethod, msgSupplier);
    }

}
