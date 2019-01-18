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

import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author Yamashita,Takahiro
 */
class LoggerStoreItem {

    private final long millis;
    private final Class<?> actionClass;
    private final Method actionMethod;
    private final Throwable throwable;
    private final String message;

    LoggerStoreItem(Class<?> actionClass, Method actionMethod, Throwable throwable, String message) {
        this.millis = System.currentTimeMillis();
        this.actionClass = actionClass;
        this.actionMethod = actionMethod;
        this.throwable = throwable;
        this.message = message;
    }

    void write(Consumer<LogRecord> logger) {
        LogRecord logRecord = new LogRecord(Level.SEVERE, message);
        logRecord.setMillis(this.millis);
        logRecord.setSourceClassName(this.actionClass != null ? this.actionClass.getName() : null);
        logRecord.setSourceMethodName(this.actionMethod != null ? this.actionMethod.getName() : null);
        logRecord.setThrown(this.throwable);

        logger.accept(logRecord);

    }

}
