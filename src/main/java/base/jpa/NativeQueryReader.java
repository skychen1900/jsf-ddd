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
package base.jpa;

/**
 * SQLファイルを読み込む機能を提供します.
 * <p>
 * {@code resources/sql} 配下の InjectしたRepositoryImplクラスのパッケージと同じフォルダに 格納している sqlファイルを読み込みます.<br>
 * ファイルが無い場合は {@code resources/sql}直下を参照します.
 *
 * @author Yamashita,Takahiro
 */
public interface NativeQueryReader {

    /**
     * SQLファイルを読み込みます.
     * <P>
     * {@code resources/sql} 配下の InjectしたRepositoryImplクラスのパッケージと同じフォルダに 格納している sqlファイルを読み込みます.<br>
     * ファイルが無い場合は {@code resources/sql}直下を参照します.
     *
     * @param sqlFileName 参照対象のSQLファイル名
     * @return 読み込んだSQL
     */
    public String read(String sqlFileName);

}
