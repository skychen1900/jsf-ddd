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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * FileLog出力時のフォーマットを扱うクラスです.
 *
 * @author Yamashita,Takahiro
 */
public class LogFileFormatter extends Formatter {

    /**
     * {@inheritDoc }
     * <P>
     * Interceptor内で生成したLoggerについては、
     * 出力内容については、messageを設定する際に事前に指定しているという前提として messageに設定した値を そのまま出力します.
     *
     * @param record the log record to be formatted.
     * @return the formatted log record, or log message.
     */
    @Override
    public String format(LogRecord record) {
        Instant instant = Instant.ofEpochMilli(record.getMillis());
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return LogRecordConverter
                .of(ldt,
                    record.getLevel(),
                    record.getSourceClassName(),
                    record.getSourceMethodName(),
                    record.getThrown(),
                    super.formatMessage(record))
                .toFile();
    }

}
