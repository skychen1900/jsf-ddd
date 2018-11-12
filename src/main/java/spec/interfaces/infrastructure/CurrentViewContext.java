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
package spec.interfaces.infrastructure;

import java.util.Locale;

/**
 * クライアントページのURLを扱います
 *
 * @author Yamashita,Takahiro
 */
public interface CurrentViewContext {

    /**
     * 現在のクライアントURL（コンテキストパスを除く）を返却します.
     *
     * @return 現在のクライアントURL（コンテキストパスを除く）
     */
    public String currentViewId();

    /**
     * レスポンス用の編集をしたURLを返却します.
     * <P>
     * レスポンスするURLは常にリダイレクトをするように編集をしています.
     *
     * @param <T> 引数の型
     * @param baseUrl コンテキストパスを除いた URL情報
     * @return リダイレクト編集をした URL（コンテキストパスを除く）
     */
    public <T extends Object> Object responseViewId(T baseUrl);

    /**
     * 現在のクライアントのLocaleを返却します.
     *
     * @return クライアントのLocale
     */
    public Locale clientLocate();

}
