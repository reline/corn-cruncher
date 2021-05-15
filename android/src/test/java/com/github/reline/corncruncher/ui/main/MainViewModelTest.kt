package com.github.reline.corncruncher.ui.main

import com.github.reline.corncruncher.settings.SettingsRepository
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class MainViewModelTest {

//    @get:JvmField
//    @Rule
//    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        val repo = mock<SettingsRepository> {
            on { tankSize } doReturn emptyFlow()
            on { desiredEthanol } doReturn emptyFlow()
            on { actualEthanolOne } doReturn emptyFlow()
            on { actualEthanolTwo } doReturn emptyFlow()
        }
        viewModel = MainViewModel(repo)
    }

    @Test
    fun increaseContent() {
        viewModel.onCalculateClicked(
                12.4f,
                20f,
                27f,
                30f,
                10f,
                52f,
        )
//        assertEquals(viewModel.resultOne.value)
    }

    @Test
    fun decreaseContent() {
        viewModel.onCalculateClicked(
                12.4f,
                20f,
                33f,
                30f,
                10f,
                52f,
        )
    }

    @Test
    fun maintainContent() {
        viewModel.onCalculateClicked(
                12.4f,
                20f,
                30f,
                30f,
                10f,
                52f,
        )
    }
}
