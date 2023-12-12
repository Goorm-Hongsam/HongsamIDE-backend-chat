package HongsamIde.backchat.service;

import HongsamIde.backchat.domain.ChatMessage;
import HongsamIde.backchat.domain.ChatMessageSaveDto;
import HongsamIde.backchat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final RedisTemplate<String, ChatMessage> redisTemplateMessage;

    // 메세지 저장
    public void saveMessage(ChatMessage message) {

        ChatMessageSaveDto chatMessageSaveDto = new ChatMessageSaveDto(message.getRoomId(), message.getSender(),
                message.getMessage(), message.getDate(), message.getTime());
        // DB에 저장
        chatRepository.save(chatMessageSaveDto);

        redisTemplateMessage.opsForList().leftPush(message.getRoomId(), message);

        redisTemplateMessage.expire(message.getRoomId(), 1, TimeUnit.MINUTES);

    }

    public List<ChatMessage> loadMessage(String roomId) {

        List<ChatMessage> messageList = new ArrayList<>();

        // redis 캐시에서 50개 가져오기
        List<ChatMessage> redisMessageList = redisTemplateMessage.opsForList().range(roomId, 0, 50);

        if (redisMessageList == null || redisMessageList.isEmpty()) {
            log.info("db에서 가져옴");
            // 캐시에 없을 때 db에서 가져오기
            List<ChatMessageSaveDto> dbMessageList = chatRepository.find50Message(roomId);

            for (ChatMessageSaveDto chatMessageSaveDto : dbMessageList) {
                ChatMessage chatMessage = new ChatMessage(chatMessageSaveDto);
                messageList.add(chatMessage);
                redisTemplateMessage.opsForList().rightPush(chatMessage.getRoomId(), chatMessage);
            }
        } else {
            log.info("redis에서 가져옴");
            messageList.addAll(redisMessageList);
        }

        Collections.reverse(messageList);

        return messageList;
    }

}

