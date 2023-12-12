package HongsamIde.backchat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK
    }

    private MessageType type;

    private String roomId;

    private String sender;

    private String message;

    private String date;

    private String time;

    private String uuid;

    public ChatMessage(ChatMessageSaveDto chatMessageSaveDto) {
        this.roomId = chatMessageSaveDto.getRoomId();
        this.sender = chatMessageSaveDto.getSender();
        this.message = chatMessageSaveDto.getMessage();
        this.date = chatMessageSaveDto.getDate();
        this.time = chatMessageSaveDto.getTime();
    }
}

