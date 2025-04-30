package com.ohdelivery.service.consult.chat.presentation;

import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.consult.chat.application.ChatService;
import com.ohdelivery.service.consult.chat.domain.model.ChatMessage;
import com.ohdelivery.service.consult.chat.domain.model.Chat;
import com.ohdelivery.service.consult.chat.presentation.request.CreateChatRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/consults/chat")
@RestController
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public ApiResponse<Chat> getChatroom(@RequestParam UUID chatId){
        return ApiResponse.success(
            SuccessCode.GET_SUCCESS.getCode().toString(),
            SuccessCode.GET_SUCCESS.getMessage(),
            chatService.getChatRoom(chatId)
        );
    }

    @PostMapping
    public ApiResponse<Chat> createChat(@RequestBody CreateChatRequest chat){
        return ApiResponse.success(
            SuccessCode.CREATED_SUCCESS.getCode().toString(),
            SuccessCode.CREATED_SUCCESS.getMessage(),
            chatService.createChatroom(chat.getAgentId(), chat.getRiderId())
        );
    }

    @GetMapping("/history")
    public ApiResponse<List<ChatMessage>> getChatHistory(@RequestParam UUID chatId){
        return ApiResponse.success(
            SuccessCode.GET_SUCCESS.getCode().toString(),
            SuccessCode.GET_SUCCESS.getMessage(),
            chatService.getChatHistory(chatId)
        );
    }

}