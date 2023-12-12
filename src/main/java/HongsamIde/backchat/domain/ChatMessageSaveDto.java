package HongsamIde.backchat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class ChatMessageSaveDto {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    private String roomId;

    private String sender;

    private String message;

    private String date;

    private String time;

    public ChatMessageSaveDto() {
    }

    public ChatMessageSaveDto( String roomId, String sender, String message, String date, String time) {
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.date = date;
        this.time = time;
    }
}
