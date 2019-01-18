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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 *
 * @author Yamashita,Takahiro
 */
public class LogRecordConverter {

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

    private static final AtomicInteger nameColumnWidth = new AtomicInteger(32);

    private final LocalDateTime dateTime;
    private final Level logLevel;
    private final String className;
    private final String methodName;
    private final Throwable throwable;
    private final String message;

    private LogRecordConverter(LocalDateTime dateTime, Level logLevel, String className, String methodName, Throwable throwable, String message) {
        this.dateTime = dateTime;
        this.logLevel = logLevel;
        this.className = className;
        this.methodName = methodName;
        this.throwable = throwable;
        this.message = message;
    }

    public static LogRecordConverter of(LocalDateTime dateTime, Level logLevel, String className, String methodName, Throwable throwable, String message) {
        return new LogRecordConverter(dateTime, logLevel, className, methodName, throwable, message);
    }

    public String toConsole() {
        StringBuilder sb = new StringBuilder(300);
        this.appendCommonRecordAndUpdateNameColumnWidth(sb);
        if (this.throwable != null) {
            sb.append(System.lineSeparator());
            this.appendThrown(sb);
        }
        return sb.toString();
    }

    public String toFile() {
        StringBuilder sb = new StringBuilder(300);
        this.appendCommonRecordAndUpdateNameColumnWidth(sb);
        sb.append(System.lineSeparator());
        if (this.throwable != null) {
            this.appendThrown(sb);
        }
        return sb.toString();
    }

    private String timeStamp() {
        return formatter.format(dateTime);
    }

    private String baseCategory() {
        return className == null
               ? ""
               : methodName == null
                 ? className
                 : className + "#" + methodName;
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

    private void appendCommonRecordAndUpdateNameColumnWidth(StringBuilder sb) {

        sb.append(this.timeStamp());
        sb.append(" ");

        sb.append(levelMsgMap.get(this.logLevel));
        sb.append(" ");

        int width = nameColumnWidth.intValue();
        String category = adjustCategoryLength(baseCategory(), width);
        sb.append("[[");
        sb.append(category);
        sb.append("]] ");

        this.updateNameColumnWidth(width, category.length());

        sb.append(message);
    }

    private void appendThrown(StringBuilder sb) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            this.throwable.printStackTrace(pw);
        }
        sb.append(sw.toString());
    }

}
