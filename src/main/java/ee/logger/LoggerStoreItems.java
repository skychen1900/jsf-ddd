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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 *
 * @author Yamashita,Takahiro
 */
class LoggerStoreItems {

    private final List<LoggerStoreItem> items;

    LoggerStoreItems() {
        this.items = new CopyOnWriteArrayList<>();
    }

    void add(Class<?> actionClass, Method actionMethod, Throwable throwable, String message) {
        LoggerStoreItem loggerStoreItem = new LoggerStoreItem(actionClass, actionMethod, throwable, message);
        this.items.add(loggerStoreItem);
    }

    void logBy(Logger logger) {
        this.items.forEach(item -> item.write(logRecord -> logger.log(logRecord)));
    }

}
