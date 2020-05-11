/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package droid.game.android.support.multitype;


import static droid.game.android.support.multitype.Preconditions.checkNotNull;

/**
 * @author drakeet
 */
class OneToManyBuilder<T> implements OneToManyFlow<T>, OneToManyEndpoint<T> {

  private final  MultiTypeAdapter adapter;
  private final  Class<? extends T> clazz;
  private ItemViewBinder<T, ?>[] binders;


  OneToManyBuilder( MultiTypeAdapter adapter,  Class<? extends T> clazz) {
    this.clazz = clazz;
    this.adapter = adapter;
  }


  @Override  @SafeVarargs
  public final  OneToManyEndpoint<T> to( ItemViewBinder<T, ?>... binders) {
    checkNotNull(binders);
    this.binders = binders;
    return this;
  }


  @Override
  public void withLinker( Linker<T> linker) {
    checkNotNull(linker);
    doRegister(linker);
  }


  @Override
  public void withClassLinker( ClassLinker<T> classLinker) {
    checkNotNull(classLinker);
    doRegister(ClassLinkerWrapper.wrap(classLinker, binders));
  }


  private void doRegister( Linker<T> linker) {
    for (ItemViewBinder<T, ?> binder : binders) {
      adapter.register(clazz, binder, linker);
    }
  }
}
