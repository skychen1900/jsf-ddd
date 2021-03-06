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

import ee.logging.LogConsoleFormatter;
import ee.logging.LogConsoleHandler;
import ee.logging.LogFileCloser;
import ee.logging.LogFileFormatter;
import ee.logging.LogFileHandler;
import ee.logging.LogStoreFileHandler;
import ee.logging.LoggerInitializer;
import java.util.Collections;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import spec.logger.LoggerName;

/**
 * Loggerの生成と破棄を扱う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
@ApplicationScoped
public class LoggerLifecycleHandler {

    private static final Logger rootLogger = Logger.getLogger(LoggerName.ROOT_NAME);
    private static final Logger storeLogger = Logger.getLogger(LoggerName.ROOT_NAME + "." + LoggerName.LOGGER_STORE_SUB_NAME);

    public void startUp(@Observes @Initialized(ApplicationScoped.class) Object event) {
        System.out.println(">> Startup:Initialize RootLogger >>");
        LoggerInitializer.builder()
                .rootLogger(rootLogger)
                .propertiesFilePath("/logging.properties")
                .consoleHandlerClass(LogConsoleHandler.class, LogConsoleFormatter.class)
                .fileHandlerClass(LogFileHandler.class, LogFileFormatter.class)
                .execute();

        LoggerInitializer.builder()
                .rootLogger(storeLogger)
                .propertiesFilePath("/logging.properties")
                .consoleHandlerClass(LogConsoleHandler.class, LogConsoleFormatter.class)
                .fileHandlerClass(LogStoreFileHandler.class, LogFileFormatter.class)
                .execute();

    }

    public void shutdown(@Observes @Destroyed(ApplicationScoped.class) Object event) {
        System.out.println("<< Cleanup:Closing logging file <<");
        LogFileCloser logFileCloser = new LogFileCloser();

        Collections.list(LogManager.getLogManager().getLoggerNames()).stream()
                .filter(name -> name.startsWith(LoggerName.ROOT_NAME))
                .forEach(logFileCloser::close);

    }

}
