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

import base.logger.LoggerStore;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.interceptor.InvocationContext;
import spec.logger.LoggerName;

/**
 * 実行開始から終了までのトレース情報を出力する機能を提供します.
 * <P>
 * {@link base.annotation.presentation.controller.Action} の開始時点の情報を取得し、実行時例外発生時にログを出力するための情報を保持します.
 * デフォルトとして、実行時例外を出力しますが、任意のタイミングでログ出力することもできます.
 * デフォルトを実行時例外時に出力することとしているため、Functionによる遅延評価の必要は無いという前提で実装をしています.
 * 詳細のデバッグ情報を取得することを目的としているため、拡張しやすいように 取得元情報を極力取得するようにしています.
 * <br>
 * 本バージョン（{@code 0.1.0}） では、Interceptorおよび、ExceptionHandlerだけで使用する事を前提としているため 各クラス内で appendすることを想定しないため、
 * そのためのメソッドは準備していません.
 * そのようなメソッドを準備する場合、Logger.logの実装を参考にリフレクションを使用して Caller情報を取得する実装を行うか、Callerから必要となるパラメータを
 * 指定するか、どちらかの実装をすることになると想定しています.
 *
 * @since 0.1.0
 *
 * @author Yamashita,Takahiro
 */
@RequestScoped
public class LoggerStoreImpl implements LoggerStore {

    private Logger logger;
    private LoggerStoreItems items;
    private Class<?> triggerActionClass;
    private String startTitle;
    private String tearDownTitle;

    @PostConstruct
    @Override
    public void init() {
        this.items = new LoggerStoreItems();
        this.startTitle = ">> Trace Logging Start >>";
        this.tearDownTitle = "<< Trace Logging  End  <<";
    }

    @Override
    public void setUp(InvocationContext ic) {
        this.triggerActionClass = ic.getTarget().getClass().getSuperclass();
        this.logger = LoggerFactory.getLogger(LoggerName.LOGGER_STORE_SUB_NAME + "." + triggerActionClass.getName());
        this.append(ic, startTitle);
        this.append(ic, ic.getTarget().toString());
    }

    @Override
    public void tearDown(Throwable throwable) {
        if (throwable != null) {
            Class<?> throwableClass = throwable.getClass();
            this.items.add(throwableClass, null, throwable, "");
        }
        this.items.add(triggerActionClass, null, null, tearDownTitle);
        this.items.logBy(logger);
    }

    @Override
    public void append(InvocationContext ic, String message) {
        Class<?> actionClass = ic.getTarget().getClass().getSuperclass();
        Method actionMethod = ic.getMethod();
        this.items.add(actionClass, actionMethod, null, message);
    }

}
