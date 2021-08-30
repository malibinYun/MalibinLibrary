package com.malibin.library.util

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * @author Malibin
 *
 * Created on 2021 02 18
 * Updated on 2021 02 18
 *
 * @sample
 * companion object {
 *     @JvmField
 *     @RegisterExtension
 *     val instantTaskExecutorExtension = InstantTaskExecutorExtension()
 * }
 *
 * testImplementation 'androidx.arch.core:core-testing:2.1.0' 의존성에 구현되어있는
 * Junit4의 InstantTaskExecutorRule()를
 * Junit5에서 사용하기 위해 만든 유틸 클래스입니다.
 *
 * 내부 구현 형태는 InstantTaskExecutorRule와 같습니다.
 *
 */

class InstantTaskExecutorExtension : BeforeEachCallback, AfterEachCallback {
    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}
