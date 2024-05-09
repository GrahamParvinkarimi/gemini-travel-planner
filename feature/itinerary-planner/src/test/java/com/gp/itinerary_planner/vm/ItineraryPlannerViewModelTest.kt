package com.gp.itinerary_planner.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.gp.itinerary_planner.domain.model.LocationResult
import com.gp.itinerary_planner.domain.model.NetworkResult
import com.gp.itinerary_planner.domain.usecase.GenerateItineraryUseCase
import com.gp.itinerary_planner.domain.usecase.ValidateLocationUseCase
import com.gp.itinerary_planner.viewstate.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.timeout
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ItineraryPlannerViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val generateItineraryUseCase: GenerateItineraryUseCase = mock()
    private val validateLocationUseCase: ValidateLocationUseCase = mock()

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private val generateContentResponse: GenerateContentResponse = mock()

    private lateinit var viewModel: ItineraryPlannerViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = ItineraryPlannerViewModel(generateItineraryUseCase, validateLocationUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sendPrompt sets uiState to Loading`() = runTest {
        // Given
        val location = "New York City"
        val dateRange = "July 10th - July 15th"

        whenever(validateLocationUseCase.invoke(location)).thenReturn(
            NetworkResult.Success(
                LocationResult(
                    foundMatch = true,
                    city = "New York City",
                    country = "United States"
                )
            )
        )

        // When
        viewModel.sendPrompt(location, dateRange)

        // Then
        assertEquals(viewModel.uiState.first(), UiState.Loading)
    }

    @Test
    fun `sendPrompt calls validateLocationUseCase and generateItineraryUseCase`() = runTest {
        // Given
        val location = "New York City"
        val dateRange = "July 10th - July 15th"

        whenever(validateLocationUseCase.invoke(location)).thenReturn(
            NetworkResult.Success(
                LocationResult(
                    foundMatch = true,
                    city = "New York City",
                    country = "United States"
                )
            )
        )

        whenever(generateItineraryUseCase.invoke(any())).thenReturn(
            NetworkResult.Success(generateContentResponse)
        )

        // When
        viewModel.sendPrompt(
            location, dateRange
        )

        // Then
        verify(validateLocationUseCase).invoke(location)
        verify(generateItineraryUseCase, timeout(1000)).invoke(any())
    }

    @Test
    fun `sendPrompt sets uiState to Success when generateItineraryUseCase returns Success`() =
        runTest {
            // Given
            val location = "New York City"
            val dateRange = "July 10th - July 15th"

            whenever(validateLocationUseCase.invoke(location)).thenReturn(
                NetworkResult.Success(
                    LocationResult(
                        foundMatch = true,
                        city = "New York City",
                        country = "United States"
                    )
                )
            )


            whenever(generateItineraryUseCase.invoke(any())).thenReturn(
                NetworkResult.Success(generateContentResponse)
            )

            // When
            viewModel.sendPrompt(location, dateRange)

            // Then
            assertTrue(viewModel.uiState.first() is UiState.Success)
        }

    @Test
    fun `sendPrompt sets uiState to Error when generateItineraryUseCase returns Error`() = runTest {
        // Given
        val location = "New York City"
        val dateRange = "July 10th - July 15th"

        whenever(validateLocationUseCase.invoke(location)).thenReturn(
            NetworkResult.Success(
                LocationResult(
                    foundMatch = true,
                    city = "New York City",
                    country = "United States"
                )
            )
        )

        whenever(generateItineraryUseCase.invoke(any())).thenReturn(
            NetworkResult.Error("Error message")
        )

        // When
        viewModel.sendPrompt(location, dateRange)

        // Then
        assertTrue(viewModel.uiState.first() is UiState.Error)
        assertTrue((viewModel.uiState.first() as UiState.Error).errorMessage == "Error message")
    }

    @Test
    fun `sendPrompt sets uiState to Error when validateLocationUseCase returns Error`() = runTest {
        // Given
        val location = "New York City"
        val dateRange = "July 10th - July 15th"

        whenever(validateLocationUseCase.invoke(location)).thenReturn(
            NetworkResult.Error("Error message")
        )

        // When
        viewModel.sendPrompt(location, dateRange)

        // Then
        assertTrue(viewModel.uiState.first() is UiState.Error)
    }
}