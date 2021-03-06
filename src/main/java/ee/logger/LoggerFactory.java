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
package ee.logger;

import java.util.logging.Logger;
import spec.logger.LoggerName;

/**
 * Logger(JUL)Factory
 * <p>
 * ルートとなるLogger配下に属するLoggerとすることで統一的な制御を行えるようにしています.
 *
 * @author Yamashita,Takahiro
 */
class LoggerFactory {

    private LoggerFactory() {
    }

    static Logger getLogger(String name) {
        return Logger.getLogger(LoggerName.ROOT_NAME + "." + name);
    }

}
