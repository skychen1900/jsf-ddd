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

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;

/**
 * {@code logging.properties}からLoggerに付与する情報を取得する機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
class PropertiesItem {

    private final Properties properties;

    PropertiesItem(String propertiesPath) {
        try (InputStream inputStream = PropertiesItem.class.getResourceAsStream(propertiesPath)) {
            if (inputStream == null) {
                throw new IllegalStateException("logging properties file could not read. target path is [" + propertiesPath + "]");
            }

            Properties prop = new Properties();
            prop.load(inputStream);

            this.properties = prop;

        } catch (IOException ex) {
            throw new UncheckedIOException("logging properties counld not read.", ex);
        }
    }

    //
    FileHandler createFileHandler(Class<? extends FileHandler> fileHandlerClass) {
        LogManager defaultLogManager = LogManager.getLogManager();
        String defaultClassName = "java.util.logging.FileHandler";
        String className = fileHandlerClass.getName();

        String pattern = this.properties
                .getProperty(className + ".pattern", defaultLogManager.getProperty(defaultClassName + ".pattern"));
        int limit = Integer.parseInt(this.properties
                .getProperty(className + ".limit", defaultLogManager.getProperty(defaultClassName + ".limit")));
        int count = Integer.parseInt(this.properties
                .getProperty(className + ".count", defaultLogManager.getProperty(defaultClassName + ".count")));
        boolean append = Boolean.parseBoolean(this.properties
                .getProperty(className + ".append", defaultLogManager.getProperty(defaultClassName + ".append")));

        FileHandler fileHandler;
        try {

            try {
                fileHandler = fileHandlerClass
                        .getDeclaredConstructor(String.class, Integer.class, Integer.class, String.class)
                        .newInstance(pattern, limit, count, append);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                     | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                fileHandler = new FileHandler(pattern, limit, count, append);
            }

        } catch (IOException ex) {
            throw new UncheckedIOException("FileHandler counld not instance.", ex);
        }

        return fileHandler;
    }

}
