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
package ee.jpa.nativequery;

import base.jpa.NativeQueryReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Yamashita,Takahiro
 */
public class NativeQueryReaderImpl implements NativeQueryReader {

    private final String sqlFolderPath;

    public NativeQueryReaderImpl(Class<?> injectionPointClazz) {
        String classPath = injectionPointClazz.getName();
        String className = injectionPointClazz.getSimpleName();
        String _classFolderPath = classPath.substring(0, classPath.lastIndexOf(className));
        this.sqlFolderPath = "sql/" + _classFolderPath.replaceAll("\\.", "/");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String read(String sqlFileName) {

        URL url = NativeQueryReaderImpl.class.getClassLoader().getResource(this.sqlFolderPath + sqlFileName);
        try {
            Path path = Paths.get(url.toURI());
            List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));
            return String.join(" ", lines);
        } catch (URISyntaxException | IOException ex) {
            throw new UnsupportedOperationException("SQL file does not exist.", ex);
        }
    }

}
