/**
 * Copyright (c) 2016-present, RxJava Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package io.reactivex.rxjava3.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicBoolean;

import org.reactivestreams.Subscriber;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.processors.FlowableProcessor;

/**
 * Wrapper for a FlowableProcessor that detects an incoming subscriber.
 * @param <T> the element type of the flow.
 * @since 3.0.0
 */
final class WindowSubscribeIntercept<T> extends Flowable<T> {

    final FlowableProcessor<T> window;

    final AtomicBoolean once;

    WindowSubscribeIntercept(FlowableProcessor<T> source) {
        this.window = source;
        this.once = new AtomicBoolean();
    }

    @Override
    protected void subscribeActual(Subscriber<? super T> s) {
        window.subscribe(s);
        once.set(true);
    }

    boolean tryAbandon() {
        return !once.get() && once.compareAndSet(false, true);
    }
}