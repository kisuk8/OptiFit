package com.example.optifit.ai

import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatGPT(private val apiKey: String) {

    private val client = OpenAI(
        token = apiKey,
    )

    suspend fun ask(question: String): String = withContext(Dispatchers.IO) {

        val response = client.chatCompletion(
            ChatCompletionRequest(
                model = ModelId("gpt-4o-mini"),
                messages = listOf(
                    ChatMessage(
                        role = ChatRole.User,
                        content = question
                    )
                )
            )
        )

        response.choices.first().message?.content ?: "No response"
    }
}