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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

/**
 * FileLog出力時のフォーマットを扱うクラスです.
 *
 * @author Yamashita,Takahiro
 */
public class LogFileFormatter extends Formatter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS");

    private static final Map<Level, String> levelMsgMap = Collections.unmodifiableMap(new HashMap<Level, String>() {
        private static final long serialVersionUID = 1L;

        {
            put(Level.SEVERE, "SEVERE");
            put(Level.WARNING, "WARN");
            put(Level.INFO, "INFO");
            put(Level.CONFIG, "CONF");
            put(Level.FINE, "FINE");
            put(Level.FINER, "FINE");
            put(Level.FINEST, "FINE");
        }
    });

    private final AtomicInteger nameColumnWidth = new AtomicInteger(16);

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder(200);

        sb.append(this.timeStamp(record));
        sb.append(" ");

        sb.append(levelMsgMap.get(record.getLevel()));
        sb.append(" ");

        int width = nameColumnWidth.intValue();
        String category = adjustCategoryLength(baseCategory(record), width);
        sb.append("[[");
        sb.append(category);
        sb.append("]] ");

        this.updateNameColumnWidth(width, category.length());

        sb.append(super.formatMessage(record));

        sb.append(System.lineSeparator());
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            try (PrintWriter pw = new PrintWriter(sw)) {
                record.getThrown().printStackTrace(pw);
            }
            sb.append(sw.toString());
        }

        return sb.toString();
    }

    private String timeStamp(LogRecord record) {
        Instant instant = Instant.ofEpochMilli(record.getMillis());
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return formatter.format(ldt);
    }

    private String baseCategory(LogRecord record) {
        if (record.getSourceClassName() == null) {
            return record.getLoggerName();
        }

        String className = record.getSourceClassName();
        if (record.getSourceMethodName() == null) {
            return className;
        }

        return className + "#" + record.getSourceMethodName();
    }

    private String adjustCategoryLength(String packageName, int aimLength) {

        int overflowWidth = packageName.length() - aimLength;

        String[] fragment = packageName.split(Pattern.quote("."));
        for (int i = 0; i < fragment.length - 1; i++) {
            if (1 < fragment[i].length() && 0 < overflowWidth) {

                int cutting = (fragment[i].length() - 1) - overflowWidth;
                cutting = (cutting < 0) ? (fragment[i].length() - 1) : overflowWidth;

                fragment[i] = fragment[i].substring(0, fragment[i].length() - cutting);
                overflowWidth -= cutting;
            }
        }

        String result = String.join(".", fragment);

        int cnt = aimLength - result.length();
        if (cnt <= 0) {
            return result;
        }
        String blank = new String(new char[cnt]).replace("\0", " ");
        return result + blank;
    }

    private void updateNameColumnWidth(int width, int categoryLength) {
        if (width < categoryLength) {
            nameColumnWidth.compareAndSet(width, categoryLength);
        }

    }

}
