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
package core.scope.conversation;

/**
 * Submitによる状態遷移を示す区分です.
 *
 * @author Yamashita,Takahiro
 */
public enum DoubleSubmitState {
    INIT {
        /**
         * {@inheritDoc }
         */
        @Override
        public boolean isSubmitted() {
            return false;
        }

        /**
         * {@inheritDoc }
         */
        @Override
        public DoubleSubmitState nextState() {
            return DoubleSubmitState.SUBMITED;
        }
    },
    SUBMITED {
        /**
         * {@inheritDoc }
         */
        @Override
        public boolean isSubmitted() {
            return true;
        }

        /**
         * {@inheritDoc }
         */
        @Override
        public DoubleSubmitState nextState() {
            return DoubleSubmitState.ERROR;
        }
    },
    ERROR {
        /**
         * {@inheritDoc }
         */
        @Override
        public boolean isSubmitted() {
            return true;
        }

        /**
         * {@inheritDoc }
         */
        @Override
        public DoubleSubmitState nextState() {
            return DoubleSubmitState.ERROR;
        }
    };

    /**
     * 実行有無を判定します.
     *
     * @return 実行済みの場合は true
     */
    public abstract boolean isSubmitted();

    /**
     * 次の状態区分を返却します.
     *
     * @return 遷移後の状態
     */
    public abstract DoubleSubmitState nextState();

}
