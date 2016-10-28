/*
 * Copyright 2002-2015 the original author or authors.
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
 */

package org.nebula.builder;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.nebula.framework.core.Promise;

@Aspect
public class AsyncMethodAspect {

  @Pointcut("execution(@org.nebula.framework.annotation.Async * *(..))")
  public void executeAsyncMethods() {
  }

  @Around("executeAsyncMethods()")
  public Promise around(ProceedingJoinPoint pjp) throws Throwable {

    for (Object arg : pjp.getArgs()) {
      if (arg instanceof Promise && !((Promise) arg).isReady()) {
        return new Promise();
      }
    }

    return (Promise) pjp.proceed();
  }
}