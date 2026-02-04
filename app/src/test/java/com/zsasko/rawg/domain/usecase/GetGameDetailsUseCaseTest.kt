package com.zsasko.rawg.domain.usecase

import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg.domain.repository.GameRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetGameDetailsUseCaseTest {

    private val gameRepository: GameRepository = mockk()
    private val getGameDetailsUseCase = GetGameDetailsUseCase(gameRepository)

    @Test
    fun `getGameDetails returns expected response`() = runBlocking {
        val gameId = 123
        val expectedResponse =
            NetworkResponse.Success(GameDetailsResponse.createMinimalMock(gameId))

        coEvery { gameRepository.getGameDetails(gameId) } returns expectedResponse

        val result = getGameDetailsUseCase.getGameDetails(gameId)

        assertEquals(expectedResponse, result)
    }
}
