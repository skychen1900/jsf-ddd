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
package ee.logging;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Loggerの初期化を行う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
public class LoggerInitializer {

    public static interface RootLoggerBuilder {

        PropertiesFileBuilder rootLogger(Logger rootLogger);
    }

    public static interface PropertiesFileBuilder {

        Builder propertiesFilePath(String propertiesFilePath);
    }

    public static class Builder implements RootLoggerBuilder, PropertiesFileBuilder {

        private Logger rootLogger;
        private String propertiesFilePath;
        private Class<? extends FileHandler> fileHandlerClass;
        private Class<? extends Formatter> fileFormatterClass;
        private Formatter fileFormatter;

        private Class<? extends ConsoleHandler> consoleHandlerClass;
        private Class<? extends Formatter> consoleFormatterClass;
        private Formatter consoleFormatter;

        private Builder() {
        }

        @Override
        public PropertiesFileBuilder rootLogger(Logger rootLogger) {
            this.rootLogger = rootLogger;
            return this;
        }

        @Override
        public Builder propertiesFilePath(String propertiesFilePath) {
            this.propertiesFilePath = propertiesFilePath;
            return this;
        }

        public Builder fileHandlerClass(Class<? extends FileHandler> fileHandlerClass) {
            this.fileHandlerClass = fileHandlerClass;
            return this;
        }

        public Builder fileHandlerClass(Class<? extends FileHandler> fileHandlerClass, Class<? extends Formatter> fileFormatterClass) {
            this.fileHandlerClass = fileHandlerClass;
            this.fileFormatterClass = fileFormatterClass;
            return this;
        }

        public Builder consoleHandlerClass(Class<? extends ConsoleHandler> consoleHandlerClass) {
            this.consoleHandlerClass = consoleHandlerClass;
            return this;
        }

        public Builder consoleHandlerClass(Class<? extends ConsoleHandler> consoleHandlerClass, Class<? extends Formatter> consoleFormatterClass) {
            this.consoleHandlerClass = consoleHandlerClass;
            this.consoleFormatterClass = consoleFormatterClass;
            return this;
        }

        public Builder formatterClass(Class<? extends Formatter> formatter) {
            this.consoleFormatterClass = formatter;
            this.fileFormatterClass = formatter;
            return this;
        }

        public void execute() {

            this.initConsoleFormatter();
            this.setConsoleHander();

            this.initFileFormatter();
            this.setFileHander();

            this.rootLogger.setUseParentHandlers(false);
        }

        private void initConsoleFormatter() {

            if (this.consoleFormatterClass == null) {
                this.consoleFormatter = new SimpleFormatter();
                return;
            }

            try {
                this.consoleFormatter = this.consoleFormatterClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                     | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                System.err.println("custom consolelog formatter counld not instance.");
                this.consoleFormatter = new SimpleFormatter();
            }

        }

        private void initFileFormatter() {

            if (this.fileFormatterClass == null) {
                this.fileFormatter = new SimpleFormatter();
                return;
            }

            try {
                this.fileFormatter = this.fileFormatterClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                     | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                System.err.println("custom consolelog formatter counld not instance.");
                this.fileFormatter = new SimpleFormatter();
            }

        }

        private void setConsoleHander() {
            if (this.consoleHandlerClass == null) {
                return;
            }

            ConsoleHandler consoleHandler;
            try {
                consoleHandler = this.consoleHandlerClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                     | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                consoleHandler = new ConsoleHandler();
            }

            consoleHandler.setFormatter(this.consoleFormatter);
            this.rootLogger.addHandler(consoleHandler);
        }

        private void setFileHander() {
            if (this.fileHandlerClass == null) {
                return;
            }
            FileHandler fileHandler = new PropertiesItem(this.propertiesFilePath).createFileHandler(fileHandlerClass);
            fileHandler.setFormatter(this.fileFormatter);
            this.rootLogger.addHandler(fileHandler);
        }

    }

    public static RootLoggerBuilder builder() {
        return new Builder();
    }

}
